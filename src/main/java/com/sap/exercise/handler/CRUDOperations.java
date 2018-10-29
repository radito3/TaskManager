package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.model.AbstractModel;
import com.sap.exercise.model.Event;
import org.apache.commons.lang3.NotImplementedException;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class CRUDOperations {

    public static <T extends AbstractModel> void create(T obj) {
        process(s -> s.save(obj));
    }

    public static <T extends AbstractModel> void update(T obj) {
        process(s -> s.update(obj));
    }

    public static <T extends AbstractModel> void delete(T obj) {
        process(s -> s.delete(obj));
    }

    @SafeVarargs
    public static <T extends AbstractModel> void create(T... arr) {
        process(s -> {
            for (T obj : arr) {
                s.save(obj);
            }
        });
    }

    private static void process(Consumer<Session> consumer) {
        try {
            DatabaseUtilFactory.getDbClient().processObject(consumer);
        } catch (HibernateException e) {
            System.setProperty("db-instance", "false");
            throw e;
        }
    }

    public static <R extends AbstractModel> R getObject(R obj) {
        return get(s -> s.get((Class<R>)obj.getClass(), obj.getId()));
    }

    @SafeVarargs
    public static <R extends AbstractModel> List<R> getObjects(R... arr) {
        return get(s -> {
            List<R> result = new ArrayList<>();
            for (R o : arr) {
                Optional<R> opt = s.byId((Class<R>)o.getClass()).loadOptional(o.getId());
                opt.ifPresent(result::add);
            }
            return result;
        });
    }

    private static <T> T get(Function<Session, T> function) {
        try {
            return DatabaseUtilFactory.getDbClient().getObject(function);
        } catch (HibernateException e) {
            System.setProperty("db-instance", "false");
            throw e;
        }
    }

    public static Event getObjectFromTitle(String title) {
        return DatabaseUtilFactory.getDbClient().getObject(s ->
                s.createNativeQuery("SELECT * FROM Eventt WHERE Title = \'" + title + "\' LIMIT 1;", Event.class)
                        .uniqueResultOptional()
                        .orElseThrow(() -> new NullPointerException("Invalid event name")));
    }

    /*development stage methods*/

    //may not be needed after additional models are implemented
    public static List<Event> getEventsInTimeFrame(String start, String end) {
        return DatabaseUtilFactory.getDbClient().getObject(s ->
                s.createNativeQuery("SELECT * FROM Eventt WHERE TimeOf >= \'" + start + "\' AND TimeOf <= \'" + end + "\';", Event.class)
                    .getResultList());
    }

    public static Event getEventAt(String time) {
        //TODO implement
        throw new NotImplementedException("Functionality not implemented");
    }

    public static void deleteEventsInTimeFrame(Event event, String start, String end) {
        //TODO implement
        throw new NotImplementedException("Deleting repeatable events not implemented");
    }
}
