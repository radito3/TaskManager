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

public class DatabaseUtil {

    private SessionFactory factory;

    DatabaseUtil() {
        Configuration configuration = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Event.class)
                .addAnnotatedClass(User.class);

        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        factory = configuration.buildSessionFactory(registry);
    }

    public void processObject(Consumer<Session> consumer) {
        Transaction transaction = null;

        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();

            consumer.accept(session);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public <T> T getObject(Function<Session, T> function) {
        Transaction transaction = null;

        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();

            T obj = function.apply(session);
            transaction.commit();
            if (obj == null) throw new NullPointerException("Object does not exist");

            return obj;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

}
