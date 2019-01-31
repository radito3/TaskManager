package com.sap.exercise.db;

public class DatabaseClientHolder {

    private static DatabaseUtil db;

    public static void createDbClient() {
        db = new DatabaseUtil();
        System.setProperty("db-instance", "true");
    }

    static DatabaseUtil getDbClient() {
        //TODO What better is the db-instance property compared to a null check?
        boolean set = System.getProperty("db-instance") != null &&
                System.getProperty("db-instance").equals("true");

        if (!set) {
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
