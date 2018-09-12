package com.sap.exercise.db;

import com.sap.exercise.model.BaseEvent;
import com.sap.exercise.model.Task;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.function.Consumer;
import java.util.function.Function;

public class DatabaseUtil {

    private SessionFactory factory;

    public DatabaseUtil() {
        Configuration configuration = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Task.class);

        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        factory = configuration.buildSessionFactory(registry);
    }

    public void processObject(Consumer<Session> consumer) {
        processObject(consumer, null);
    }

    public <T extends BaseEvent> T getObject(Function<Session, T> function) {
        return processObject(null, function);
    }

    public void closeConnection() {
        factory.close();
    }

    private <T> T processObject(Consumer<Session> consumer, Function<Session, T> function) {
        Transaction transaction = null;

        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();

            if (function != null) {
                T obj = function.apply(session);
                transaction.commit();
                return obj;
            }
            consumer.accept(session);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Something went wrong", e); //maybe something more descriptive
        }

        return null;
    }
}
