package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.model.AbstractModel;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class CRUDOperations {

    public static <T extends AbstractModel> Serializable create(T obj) {
        return get(s -> s.save(obj));
    }

    static <T extends AbstractModel> void create(final Collection<T> collection) {
        process(s -> collection.forEach(s::save));
    }

    static <T extends AbstractModel> void update(T obj) {
        process(s -> s.update(obj));
    }

    static <T extends AbstractModel> void delete(T obj) {
        process(s -> s.delete(obj));
    }

    @SuppressWarnings("unchecked")
    public static <R extends AbstractModel> R getObject(R obj) {
        return get(s -> s.get((Class<R>)obj.getClass(), obj.getId()));
    }

    private static <T> T get(Function<Session, T> function) {
        try {
            return DatabaseUtilFactory.getDbClient().getObject(function);
        } catch (HibernateException e) {
            System.setProperty("db-instance", "false");
            throw e;
        }
    }

    private static void process(Consumer<Session> consumer) {
        try {
            DatabaseUtilFactory.getDbClient().processObject(consumer);
        } catch (HibernateException e) {
            System.setProperty("db-instance", "false");
            throw e;
        }
    }

    static Optional<Event> getEventByTitle(String title) {
        return DatabaseUtilFactory.getDbClient().getObject(s ->
                s.createNativeQuery("SELECT * FROM Eventt WHERE Title = \'" + title + "\' LIMIT 1;", Event.class)
                        .uniqueResultOptional());
    }

    static List<Event> getEventsInTimeFrame(String start, String end) {
        return DatabaseUtilFactory.getDbClient().getObject(s ->
                s.createNativeQuery("SELECT * FROM Eventt RIGHT JOIN CalendarEvents E on Eventt.Id = E.EventId " +
                                "WHERE Date >= \'" + start + "\' AND Date <= \'" + end + "\';", Event.class)
                        .getResultList());
    }

    public static List<CalendarEvents> getCalendarEventsByEventId(Integer id) {
        return DatabaseUtilFactory.getDbClient().getObject(s ->
                s.createNativeQuery("SELECT * FROM CalendarEvents WHERE EventId = " + id + " ;", CalendarEvents.class)
                .getResultList());
    }

    static void deleteEventsInTimeFrame(Event event, String start, String end) {
        DatabaseUtilFactory.getDbClient().processObject(s ->
                s.createNativeQuery("DELETE FROM CalendarEvents WHERE EventId = " + event.getId() +
                        " AND Date >= \'" + start + "\' AND Date <= \'" + end + "\';")
                        .executeUpdate());
    }
}
