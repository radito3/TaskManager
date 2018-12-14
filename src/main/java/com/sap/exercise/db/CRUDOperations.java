package com.sap.exercise.db;

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

//will be deleted
@Deprecated
public class CRUDOperations {

    public static <T extends AbstractModel> Serializable create(T obj) {
        return get(s -> s.save(obj));
    }

    public static <T extends AbstractModel> void create(final Collection<T> collection) {
        process(s -> collection.forEach(s::save));
    }

    public static <T extends AbstractModel> void update(T obj) {
        process(s -> s.update(obj));
    }

    public static <T extends AbstractModel> void delete(T obj) {
        process(s -> s.delete(obj));
    }

    public static <T extends AbstractModel> T getObjById(Class<T> objClass, Integer id) {
        return get(s -> s.get(objClass, id));
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

    public static Optional<Event> getEventByTitle(String title) {
        return get(s -> s.createNativeQuery("SELECT * FROM Eventt WHERE Title = \'" + title + "\' LIMIT 1;", Event.class)
                        .uniqueResultOptional());
    }

    public static List<CalendarEvents> getEventsInTimeFrame(String start, String end) {
        return get(s -> s.createNativeQuery("SELECT * FROM CalendarEvents WHERE Date >= \'" + start +
                        "\' AND Date <= \'" + end + "\';", CalendarEvents.class).getResultList());
    }

    public static void deleteEventsInTimeFrame(Event event, String start, String end) {
        process(s -> s.createNativeQuery("DELETE FROM CalendarEvents WHERE EventId = " + event.getId() +
                        " AND Date >= \'" + start + "\' AND Date <= \'" + end + "\';").executeUpdate());
    }
}
