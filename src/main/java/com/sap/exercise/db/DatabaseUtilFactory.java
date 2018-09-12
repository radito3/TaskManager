package com.sap.exercise.db;

public class DatabaseUtilFactory {
    //temporary solution
    private static DatabaseUtil db;

    public static void createDbClient() {
        db = new DatabaseUtil();
        System.setProperty("db-instance", "true");
    }

    public static DatabaseUtil getDbClient() {
        boolean set = !(System.getProperty("db-instance") == null) && System.getProperty("db-instance").equals("true");

        if (set) {
            return db;
        } else {
            createDbClient();
            return db;
        }
    }
}