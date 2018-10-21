package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.model.AbstractModel;
import com.sap.exercise.model.Event;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.function.Consumer;

public class EventsHandler {

    public static <T extends AbstractModel> void create(T obj) {
        process(s -> s.save(obj));
    }

    public static <T extends AbstractModel> void update(T obj) {
        process(s -> s.update(obj));
    }

    public static <T extends AbstractModel> void delete(T obj) {
        process(s -> s.delete(obj));
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
}
