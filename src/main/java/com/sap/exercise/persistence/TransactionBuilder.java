package com.sap.exercise.persistence;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class TransactionBuilder {

    private Session currentSession;
    private List<Consumer<Session>> operations;

    TransactionBuilder(Session session) {
        currentSession = session;
        beginTransaction();
    }

    private synchronized void beginTransaction() {
        currentSession.beginTransaction();
        currentSession.getTransaction().setTimeout(10);
        operations = Collections.synchronizedList(new LinkedList<>());
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
