package com.sap.exercise.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.function.Consumer;
import java.util.function.Function;

public class DatabaseUtil implements Connector {

    private SessionFactory factory;

    //need to apply a design pattern
    public DatabaseUtil() {
        factory = Connector.connect();
    }

    @Override
    public void processObject(Consumer<Session> consumer) {
        processObject(consumer, null);
    }

    @Override
    public <T> T getObject(Function<Session, T> function) {
        return processObject(null, function);
    }

    private <T> T processObject(Consumer<Session> consumer, Function<Session, T> function) {
        Transaction transaction = null;

        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();

            if (function != null) {
                return function.apply(session);
            }
            consumer.accept(session);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return null;
    }
}
