package com.sap.exercise.db;

public class DatabaseClientHolder {

    private static DatabaseUtil db;

    public static void createDbClient() {
        db = new DatabaseUtil();
        System.setProperty("db-instance", "true");
    }

    static DatabaseUtil getDbClient() {
        if (System.getProperty("db-instance") == null) {
            createDbClient();
        }
        return db;
    }

    public static void close() {
        System.clearProperty("db-instance");
        db.close();
        db = null;
    }
}
