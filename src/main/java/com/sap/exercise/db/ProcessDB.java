package com.sap.exercise.db;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.persistence.RollbackException;
import java.util.function.Consumer;

public interface ProcessDB {

    default void process(Consumer<Session> consumer) {
        try {
            DatabaseUtilFactory.getDbClient().processObject(consumer);
        } catch (HibernateException | IllegalStateException | RollbackException e) {
            System.setProperty("db-instance", "false");
            Logger.getLogger(ProcessDB.class).error("Database processing error", e);
        }
    }
}
