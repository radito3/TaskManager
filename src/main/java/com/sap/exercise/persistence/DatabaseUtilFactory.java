package com.sap.exercise.persistence;

import com.sap.exercise.services.DefaultPersistenceConfigurationProvider;
import org.hibernate.cfg.Configuration;

public class DatabaseUtilFactory {

    private static DatabaseUtil databaseUtil;

    public static DatabaseUtil getDatabaseUtil() {
        return getDatabaseUtil(new DefaultPersistenceConfigurationProvider().getConfiguration());
    }

    public static DatabaseUtil getDatabaseUtil(Configuration configuration) {
        if (databaseUtil == null) {
            databaseUtil = new DatabaseUtil(configuration);
        }
        return databaseUtil;
    }

    public static void close() {
        if (databaseUtil != null)
            databaseUtil.close();
    }
}
