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
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class DatabaseUtil implements Closeable {

    private SessionFactory factory;
    private Session currentSession;

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

    public synchronized void beginTransaction() {
        currentSession = factory.getCurrentSession();
        currentSession.beginTransaction();
        currentSession.getTransaction().setTimeout(15);
    }

    public synchronized void addOperation(Consumer<Session> consumer) {
        if (currentSession.getTransaction() == null) {
            throw new IllegalStateException("Transaction not started");
        }
        try {
            consumer.accept(currentSession);
        } catch (HibernateException e) {
            currentSession.getTransaction().rollback();
            Logger.getLogger(getClass()).error("Object operations error", e);
        }
    }

    public synchronized <T> T getObjectWithRetry(Function<Session, T> function, int times) {
        if (currentSession.getTransaction() == null) {
            throw new IllegalStateException("Transaction not started");
        }
        T obj = null;
        boolean failed = false;
        try {
            obj = function.apply(currentSession);
        } catch (HibernateException e) {
            if (times <= 0) {
                currentSession.getTransaction().rollback();
                Logger.getLogger(getClass()).error("Object retrieval error", e);
                failed = true;
            } else {
                return getObjectWithRetry(function, --times);
            }
        }
        if (!failed) {
            commitTransaction();
            return Objects.requireNonNull(obj, "Object does not exist");
        }
        throw new NullPointerException("Object could not be retrieved");
    }

    public synchronized void commitTransaction() {
        if (currentSession.getTransaction() == null) {
            throw new IllegalStateException("Transaction not started");
        }
        try {
            currentSession.getTransaction().commit();
        } catch (HibernateException e) {
            Logger.getLogger(getClass()).error("Transaction commit error", e);
        }
    }

    @Override
    public void close() {
        factory.close();
    }
}
