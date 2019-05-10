package com.sap.exercise.db;

public class DatabaseUtilFactory {

    private static DatabaseUtil db;

    public static DatabaseUtil getDb() {
        if (db == null) {
            db = new DatabaseUtil();
        }
        return db;
    }

    public static void close() {
        if (db != null)
            db.close();
    }
}
