package com.sap.exercise.persistence;

import com.sap.exercise.services.DefaultPersistenceConfigurationProvider;
import org.hibernate.cfg.Configuration;

public class SessionProviderFactory {

    private static SessionProvider sessionProvider;

    public static SessionProvider getSessionProvider() {
        return getSessionProvider(DefaultPersistenceConfigurationProvider.getConfiguration());
    }

    public static SessionProvider getSessionProvider(Configuration configuration) {
        if (sessionProvider == null) {
            sessionProvider = new SessionProvider(configuration);
        }
        return sessionProvider;
    }

    public static void close() {
        if (sessionProvider != null) {
            sessionProvider.closeSessionFactory();
        }
    }
}
