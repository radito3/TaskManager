package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.model.BaseEvent;
import com.sap.exercise.model.Event;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.function.Consumer;

public class EventsHandler {

    public static <T extends BaseEvent> void create(T obj) {
        process(s -> s.save(obj));
    }

    public static <T extends BaseEvent> void update(T obj) {
        process(s -> s.update(obj));
    }

    public static <T extends BaseEvent> void delete(T obj) {
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

    public static <R extends BaseEvent> R getObject(R obj) {
        try {
            return DatabaseUtilFactory.getDbClient().getObject(s -> s.get((Class<R>)obj.getClass(), obj.getId()));
        } catch (HibernateException e) {
            System.setProperty("db-instance", "false");
            throw e;
        }
    }

    public static Event getObjectFromTitle(String title) {
        //don't know if this can throw an exception
        return DatabaseUtilFactory.getDbClient().getObject(s ->
                s.createNativeQuery("SELECT * FROM Eventt WHERE Title = \'" + title + "\';", Event.class)
                        .uniqueResultOptional().orElseThrow(NullPointerException::new));
    }
}
