package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.model.AbstractModel;
import com.sap.exercise.model.Event;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;
import java.util.function.Consumer;

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

    /**
     * this solution if for when there are many objects to commit in one transaction to the DB
     * however, if the amount of object to commit is above the threshold, the job should be split between worker threads
     * current threshold - 30
     **/
    public static <T extends AbstractModel> void create(T... arr) {
        process(s -> {
            for (T obj : arr) {
                s.save(obj);
            }
        });
    }
    //rest of crud operations with array argument

    private static void process(Consumer<Session> consumer) {
        try {
            DatabaseUtilFactory.getDbClient().processObject(consumer);
        } catch (HibernateException e) {
            System.setProperty("db-instance", "false");
            throw e;
        }
    }

    public static <R extends AbstractModel> R getObject(R obj) {
        try {
            return DatabaseUtilFactory.getDbClient().getObject(s -> s.get((Class<R>)obj.getClass(), obj.getId()));
        } catch (HibernateException e) {
            System.setProperty("db-instance", "false");
            throw e;
        }
    }

    public static Event getObjectFromTitle(String title) {
        return DatabaseUtilFactory.getDbClient().getObject(s ->
                s.createNativeQuery("SELECT * FROM Eventt WHERE Title = \'" + title + "\';", Event.class)
                        .uniqueResultOptional()
                        .orElseThrow(() -> new NullPointerException("Invalid event name")));
    }

    //may not be needed after additional models are implemented
    public static List<Event> getEventsInTimeFrame(String start, String end) {
        return DatabaseUtilFactory.getDbClient().getObject(s ->
                s.createNativeQuery("SELECT * FROM Eventt WHERE TimeOf >= \'" + start + "\' AND TimeOf <= \'" + end + "\';", Event.class)
                    .getResultList());
    }
}
