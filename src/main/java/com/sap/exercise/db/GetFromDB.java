package com.sap.exercise.db;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.persistence.RollbackException;
import java.util.function.Function;

public interface GetFromDB {

    default <T> T get(Function<Session, T> function) {
        try {
            return DatabaseClientHolder.getDbClient().getObject(function);
        } catch (HibernateException | IllegalStateException | RollbackException e) {
            Logger.getLogger(GetFromDB.class).error("Getting object from database error", e);
            DatabaseClientHolder.close();
        }
        throw new UnsupportedOperationException();
    }
}
