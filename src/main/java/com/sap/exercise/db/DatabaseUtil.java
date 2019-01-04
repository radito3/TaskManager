package com.sap.exercise.db;

import com.sap.exercise.model.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.function.Consumer;
import java.util.function.Function;

class DatabaseUtil {

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

    synchronized void processObject(Consumer<Session> consumer) {
        process(consumer, null);
    }

    synchronized <T> T getObject(Function<Session, T> function) {
        return process(null, function);
    }

    private <T> T process(Consumer<Session> consumer, Function<Session, T> function) {
        Transaction transaction = null;
        T value = null;
        Runnable onError = () -> {};

        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();

            if (function != null) {
                value = function.apply(session);
                if (value == null) {
                    onError = () -> { throw new NullPointerException("Object does not exist"); };
                }
            } else {
                consumer.accept(session);
            }

            transaction.commit();

        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            onError.run();
        }

        return value;
    }

    synchronized void close() {
        factory.close();
    }
}
