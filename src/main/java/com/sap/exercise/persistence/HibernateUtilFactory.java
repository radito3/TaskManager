package com.sap.exercise.persistence;

import com.sap.exercise.services.DefaultPersistenceConfigurationProvider;
import org.hibernate.cfg.Configuration;

public class HibernateUtilFactory {

    private static HibernateUtil hibernateUtil;

    public static HibernateUtil getHibernateUtil() {
        return getHibernateUtil(DefaultPersistenceConfigurationProvider.getConfiguration());
    }

    public static HibernateUtil getHibernateUtil(Configuration configuration) {
        if (hibernateUtil == null) {
            hibernateUtil = new HibernateUtil(configuration);
        }
        return hibernateUtil;
    }

    public static void close() {
        if (hibernateUtil != null) {
            hibernateUtil.closeSessionFactory();
        }
    }
}
