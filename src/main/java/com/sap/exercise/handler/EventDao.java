package com.sap.exercise.handler;

import com.sap.exercise.listeners.*;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.persistence.HibernateUtilFactory;
import com.sap.exercise.persistence.TransactionBuilder;
import com.sap.exercise.persistence.TransactionBuilderFactory;
import com.sap.exercise.services.SharedResourcesFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class EventDao extends ListenableEvent implements Dao<Event> {

    public EventDao() {
        addListener(ListenableEventType.CREATE, new CreationListener());
        addListener(ListenableEventType.UPDATE, new UpdateListener());
        addListener(ListenableEventType.DELETE, new DeletionListener());
        addListener(ListenableEventType.DELETE_IN_TIME_FRAME, new DeletionInTimeFrameListener());
    }

    @Override
    public Optional<Event> get(Object property) { //need to add an adapter for the property -> title/id
        Session session = HibernateUtilFactory.getHibernateUtil().getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);

        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get("title"), property.toString()))
                .distinct(true);

        return session.createQuery(criteriaQuery).uniqueResultOptional();
    }

    @Override
    public Collection<Event> getAll(CrudOptions options) {
        Session session = HibernateUtilFactory.getHibernateUtil().getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);

        criteriaQuery.select(root).where(options.getPredicate());

        Query<Event> query = session.createQuery(criteriaQuery);

        EventsGetter getter = new EventsGetter();
        //to use the getter for the events
        //this method needn't be cluttered

        return query.getResultList();
    }

    @Override
    public void save(Event arg) {
        AtomicInteger id = new AtomicInteger();
        TransactionBuilderFactory.getTransactionBuilder()
                .addOperation(s -> id.set((Integer) s.save(arg)))
                .addOperation(s -> s.save(new CalendarEvents(id.get(), arg.getTimeOf())))
                .commit();

        if (arg.getToRepeat() != Event.RepeatableType.NONE) {
            SharedResourcesFactory.getAsyncExecutionsService()
                    .execute(new CreateMultipleEventEntries(id.get(), arg));
        }
        notifyListeners(ListenableEventType.CREATE, arg, id.get());
    }

    @Override
    public void update(Event arg) {
        SharedResourcesFactory.getAsyncExecutionsService()
                .execute(() -> TransactionBuilderFactory.getTransactionBuilder()
                        .addOperation(s -> s.update(arg))
                        .commit());
        notifyListeners(ListenableEventType.UPDATE, arg);
    }

    @Override
    public void delete(Event arg, CrudOptions options) {
        if (arg.getToRepeat() == Event.RepeatableType.NONE || options.getCondition()) {
            SharedResourcesFactory.getAsyncExecutionsService()
                    .execute(() -> TransactionBuilderFactory.getTransactionBuilder()
                            .addOperation(s -> s.delete(arg))
                            .commit());
            notifyListeners(ListenableEventType.DELETE, arg);
        } else {
            Map<String, Object> optionParams = options.getParameters();

            SharedResourcesFactory.getAsyncExecutionsService()
                    .execute(() -> {
                        Session session = HibernateUtilFactory.getHibernateUtil().getSession();
                        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                        CriteriaDelete<CalendarEvents> criteriaDelete =
                                criteriaBuilder.createCriteriaDelete(CalendarEvents.class);
                        Root<CalendarEvents> root = criteriaDelete.from(CalendarEvents.class);

                        criteriaDelete.where(criteriaBuilder.equal(root.get("eventId"), arg.getId()))
                                .where(options.getPredicate());

                        TransactionBuilder tb = TransactionBuilderFactory.getTransactionBuilder();
                        session.createQuery(criteriaDelete).executeUpdate();
                        tb.commit();
                    });
            notifyListeners(ListenableEventType.DELETE_IN_TIME_FRAME, arg,
                    optionParams.get("startDate"),
                    optionParams.get("endDate"));
        }
    }
}
