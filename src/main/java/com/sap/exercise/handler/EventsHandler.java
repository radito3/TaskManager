package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.model.BaseEvent;
import org.hibernate.HibernateException;

public class EventsHandler {

    public static <T extends BaseEvent> void create(T obj) {
        try {
            DatabaseUtilFactory.getDbClient().processObject(s -> s.save(obj));
        } catch (HibernateException e) {
             System.out.println("Something went wrong!"); //maybe more in-depth
             System.setProperty("db-instance", "false");
        }
    }

    public static <T extends BaseEvent> void update(T obj) {
        try {
            DatabaseUtilFactory.getDbClient().processObject(s -> s.update(obj));
        } catch (HibernateException e) { //doing this so as to
            System.out.println(e.getMessage());
            System.setProperty("db-instance", "false");
        }
    }

    public static <T extends BaseEvent> void delete(T obj) {
        try {
            DatabaseUtilFactory.getDbClient().processObject(s -> s.delete(obj));
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            System.setProperty("db-instance", "false");
        }
    }

    public static <R extends BaseEvent> R getObject(R obj) {
        try {
            return DatabaseUtilFactory.getDbClient().getObject(s -> s.get((Class<R>)obj.getClass(), obj.getId()));
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            System.setProperty("db-instance", "false");
        }
        return null;
    }
}
