package com.sap.exercise.persistence;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

public class TransactionBuilder {

    private Session currentSession;
    private Set<Consumer<Session>> operations;

    private TransactionBuilder(Session session) {
        currentSession = session;
        beginTransaction();
    }

    public static TransactionBuilder newInstance() {
        return new TransactionBuilder(SessionProviderFactory.getSessionProvider().getSession());
    }

    public static TransactionBuilder newInstance(Configuration config) {
        return new TransactionBuilder(SessionProviderFactory.getSessionProvider(config).getSession());
    }

    private synchronized void beginTransaction() {
        currentSession.beginTransaction();
        currentSession.getTransaction().setTimeout(10);
        operations = Collections.synchronizedSet(new LinkedHashSet<>());
    }

    public synchronized TransactionBuilder addOperation(Consumer<Session> consumer) {
        operations.add(consumer);
        return this;
    }

    public synchronized void commit() {
        try {
            for (Consumer<Session> operation : operations) {
                operation.accept(currentSession);
            }
            currentSession.getTransaction().commit();
        } catch (HibernateException e) {
            if (currentSession.getTransaction().getStatus().canRollback())
                currentSession.getTransaction().rollback();

            Logger.getLogger(TransactionBuilder.class).error("Transaction build error", e);
        }
    }
}
