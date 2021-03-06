package com.sap.exercise.services;

import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Properties;

public class DefaultPersistenceConfigurationProvider {

    public static Configuration getConfiguration() {
        Configuration configuration = new Configuration();

        configuration.setProperties(getProperties());

        configuration.addAnnotatedClass(Event.class)
                .addAnnotatedClass(CalendarEvents.class);

        return configuration;
    }

    private static Properties getProperties() {
        Properties props = new Properties();

        props.setProperty(Environment.DRIVER, "org.postgresql.Driver");
        props.setProperty(Environment.URL, System.getenv("db_url"));
        props.setProperty(Environment.USER, System.getenv("db_username"));
        props.setProperty(Environment.PASS, System.getenv("db_password"));
        props.setProperty(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        props.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        props.setProperty(Environment.SHOW_SQL, "false");
        props.setProperty(Environment.HBM2DDL_AUTO, "update");
        props.setProperty(Environment.POOL_SIZE, "1");
        props.setProperty(Environment.USE_SQL_COMMENTS, "false");

        return props;
    }

}
