package com.sap.exercise.db;

import com.sap.exercise.model.*;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.Closeable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class DatabaseUtil implements Closeable {

    private SessionFactory factory;

    DatabaseUtil() {
        Configuration configuration = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Event.class)
                .addAnnotatedClass(CalendarEvents.class)
                .addAnnotatedClass(User.class);

        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        factory = configuration.buildSessionFactory(registry);
    }

    public synchronized TransactionBuilder beginTransaction() {
        return new TransactionBuilder(factory.getCurrentSession());
    }

    public static class TransactionBuilder {

        private Session currentSession;
        private Set<Object> results;

        private TransactionBuilder(Session session) {
            currentSession = session;
            currentSession.beginTransaction();
            currentSession.getTransaction().setTimeout(15);
        }

        public synchronized TransactionBuilder addOperation(Consumer<Session> consumer) {
            try {
                consumer.accept(currentSession);
            } catch (HibernateException e) {
                currentSession.getTransaction().rollback();
                if (results != null)
                    results.clear();
                Logger.getLogger(getClass()).error("Object operations error", e);
            }
            return this;
        }

        public synchronized TransactionBuilder addOperationWithResult(Function<Session, ?> function) {
            if (results == null)
                results = new LinkedHashSet<>();
            try {
                results.add(function.apply(currentSession));
            } catch (HibernateException e) {
                currentSession.getTransaction().rollback();
                results.clear();
                Logger.getLogger(getClass()).error("Object retrieval error", e);
            }
            return this;
        }

        public Set<Object> commit() {
            try {
                currentSession.getTransaction().commit();
            } catch (HibernateException e) {
                if (results != null)
                    results.clear();
                Logger.getLogger(getClass()).error("Transaction commit error", e);
            }
            return results;
        }
    }

    @Override
    public void close() {
        factory.close();
    }
}
