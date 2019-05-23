package com.sap.exercise.persistence;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class TransactionBuilder {

    private SessionFactory sessionFactory;
    private Session currentSession;
    private List<Consumer<Session>> operations;
    private Set<Object> results;

    TransactionBuilder(Configuration configuration) {
        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        sessionFactory = configuration.buildSessionFactory(registry);
        beginTransaction();
    }

    private synchronized void beginTransaction() {
        currentSession = sessionFactory.getCurrentSession();
        currentSession.beginTransaction();
        currentSession.getTransaction().setTimeout(10);
        operations = Collections.synchronizedList(new LinkedList<>());
    }

    public synchronized TransactionBuilder addOperation(Consumer<Session> consumer) {
        operations.add(consumer);
        return this;
    }

    public synchronized TransactionBuilder addOperationWithResult(Function<Session, ?> function) {
        if (results == null)
            results = new LinkedHashSet<>();
        operations.add(session -> results.add(function.apply(session)));
        return this;
    }

    public synchronized Set<Object> commit() {
        try {
            for (Consumer<Session> operation : operations) {
                operation.accept(currentSession);
            }
            currentSession.getTransaction().commit();
        } catch (HibernateException e) {
            if (results != null)
                results.clear();

            if (currentSession.getTransaction().getStatus().canRollback())
                currentSession.getTransaction().rollback();

            Logger.getLogger(TransactionBuilder.class).error("Transaction build error", e);
        }
        sessionFactory.close();
        return results;
    }
}
