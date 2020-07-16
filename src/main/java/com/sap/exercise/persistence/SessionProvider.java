package com.sap.exercise.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.TimeZone;

public class SessionProvider {

    private final SessionFactory sessionFactory;

    SessionProvider(Configuration configuration) {
        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        sessionFactory = configuration.buildSessionFactory(registry);
    }

    public Session getSession() {
        if (sessionFactory.getCurrentSession() == null) {
            sessionFactory.withOptions()
                          .jdbcTimeZone(TimeZone.getTimeZone("UTC"))
                          .openSession();
        }
        return sessionFactory.getCurrentSession();
    }

    public void closeSessionFactory() {
        sessionFactory.close();
    }
}
