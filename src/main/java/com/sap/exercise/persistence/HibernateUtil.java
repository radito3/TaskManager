package com.sap.exercise.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

    private final SessionFactory sessionFactory;

    HibernateUtil(Configuration configuration) {
        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        sessionFactory = configuration.buildSessionFactory(registry);
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void closeSessionFactory() {
        sessionFactory.close();
    }
}
