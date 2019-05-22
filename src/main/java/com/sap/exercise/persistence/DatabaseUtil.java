package com.sap.exercise.persistence;

import com.sap.exercise.model.*;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.io.Closeable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class DatabaseUtil implements Closeable {

    private SessionFactory sessionFactory;

    DatabaseUtil() {
        sessionFactory = createSessionFactory();
    }

    private SessionFactory createSessionFactory() {
        Configuration configuration = new Configuration();

        setProperties(configuration);

        configuration.addAnnotatedClass(Event.class)
                .addAnnotatedClass(CalendarEvents.class);

        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        return configuration.buildSessionFactory(registry);
    }

    private void setProperties(Configuration config) {
        Properties props = new Properties();

        props.setProperty(Environment.DRIVER, "org.postgresql.Driver");
        props.setProperty(Environment.URL, System.getenv("read_url"));
        props.setProperty(Environment.USER, System.getenv("username"));
        props.setProperty(Environment.PASS, System.getenv("password"));
        props.setProperty(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        props.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        props.setProperty(Environment.SHOW_SQL, "false");
        props.setProperty(Environment.HBM2DDL_AUTO, "update");
        props.setProperty(Environment.POOL_SIZE, "1");
        props.setProperty(Environment.USE_SQL_COMMENTS, "false");

        config.setProperties(props);
    }

    public synchronized TransactionBuilder createTransactionBuilder() {
        return new TransactionBuilder(sessionFactory.getCurrentSession());
    }

    public static class TransactionBuilder {

        private Session currentSession;
        private List<Consumer<Session>> operations = Collections.synchronizedList(new LinkedList<>());
        private Set<Object> results;

        private TransactionBuilder(Session session) {
            currentSession = session;
            currentSession.beginTransaction();
            currentSession.getTransaction().setTimeout(15);
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

        public synchronized Set<Object> build() {
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

                Logger.getLogger(getClass()).error("Transaction build error", e);
            }
            return results;
        }
    }

    @Override
    public void close() {
        sessionFactory.close();
    }
}
