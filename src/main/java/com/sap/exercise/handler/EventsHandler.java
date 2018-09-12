package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseUtilFactory;
import org.hibernate.HibernateException;

public class EventsHandler {

    public static <T> void create(T obj) {
        try {
            DatabaseUtilFactory.getDbClient().processObject(s -> s.save(obj));
        } catch (HibernateException e) {
             System.out.println("Something went wrong!");
             System.setProperty("db-instance", "false");
        }
    }
}
