package com.sap.exercise.db;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.persistence.RollbackException;
import java.util.function.Consumer;

public interface ProcessDB {

    default void process(Consumer<Session> consumer) {
        try {
            DatabaseClientHolder.getDbClient().processObject(consumer);
        } catch (HibernateException | IllegalStateException | RollbackException e) {
            Logger.getLogger(ProcessDB.class).error("Database processing error", e);
            DatabaseClientHolder.close();
            //TODO - the error is not communicated to the calling method, which expects for the work to be done and changes applied. It will continue and act as it is and cause errors
        }
    }
}
