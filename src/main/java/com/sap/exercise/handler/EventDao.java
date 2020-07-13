package com.sap.exercise.handler;

import com.sap.exercise.listeners.*;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.persistence.SessionProviderFactory;
import com.sap.exercise.persistence.Property;
import com.sap.exercise.persistence.TransactionBuilder;
import com.sap.exercise.services.SharedResourcesFactory;
import org.hibernate.Session;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class EventDao extends ListenableObject implements Dao<Event> {

    public EventDao() {
        addListener(ListenableEventType.CREATE, new EventCreationListener());
        addListener(ListenableEventType.UPDATE, new EventUpdateListener());
        addListener(ListenableEventType.DELETE, new EventDeletionListener());
        addListener(ListenableEventType.DELETE_IN_TIME_FRAME, new EventDeletionInTimeFrameListener());
    }

    @Override
    public <Y> Optional<Event> get(Property<Y> property) {
        Session session = SessionProviderFactory.getSessionProvider().getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);

        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get(property.getName()), property.getValue()))
                .distinct(true);

        return session.createQuery(criteriaQuery).uniqueResultOptional();
    }

    @Override
    public Collection<Event> getAll(CrudCondition condition) {
        Map<String, Object> optionParams = condition.parameters();

        EventsGetter eventsGetter = new EventsGetter(
                (String) optionParams.get("startDate"),
                (String) optionParams.get("endDate"),
                this::get);

        return eventsGetter.getEventsInTimeFrame();
    }

    @Override
    public void save(Event arg) {
        AtomicInteger id = new AtomicInteger();
        TransactionBuilder.newInstance()
                .addOperation(s -> id.set((Integer) s.save(arg)))
                .addOperation(s -> s.save(new CalendarEvents(id.get(), arg.getTimeOf())))
                .commit();

        if (arg.getToRepeat() != Event.RepeatableType.NONE) {
            SharedResourcesFactory.getAsyncExecutionsService()
                    .execute(new CreateMultipleEventEntriesJob(id.get(), arg));
        }
        notifyListeners(ListenableEventType.CREATE, arg, id.get());
    }

    @Override
    public void update(Event arg) {
        SharedResourcesFactory.getAsyncExecutionsService()
                .execute(() -> TransactionBuilder.newInstance()
                        .addOperation(s -> s.update(arg))
                        .commit());
        notifyListeners(ListenableEventType.UPDATE, arg);
    }

    @Override
    public void delete(Event arg, CrudCondition condition) {
        if (arg.getToRepeat() == Event.RepeatableType.NONE || condition.isToBeExecuted()) {
            SharedResourcesFactory.getAsyncExecutionsService()
                    .execute(() -> TransactionBuilder.newInstance()
                            .addOperation(s -> s.delete(arg))
                            .commit());
            notifyListeners(ListenableEventType.DELETE, arg);

            return;
        }
        Map<String, Object> optionParams = condition.parameters();

        SharedResourcesFactory.getAsyncExecutionsService()
                .execute(() -> {
                    Session session = SessionProviderFactory.getSessionProvider().getSession();
                    CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                    CriteriaDelete<CalendarEvents> criteriaDelete = criteriaBuilder.createCriteriaDelete(CalendarEvents.class);
                    Root<CalendarEvents> root = criteriaDelete.from(CalendarEvents.class);

                    criteriaDelete.where(criteriaBuilder.equal(root.get("eventId"), arg.getId()))
                            .where(condition.queryCondition());

                    TransactionBuilder.newInstance()
                        .addOperation(s -> s.createQuery(criteriaDelete).executeUpdate())
                        .commit();
                });
        notifyListeners(ListenableEventType.DELETE_IN_TIME_FRAME, arg,
                optionParams.get("startDate"),
                optionParams.get("endDate"));
    }
}
