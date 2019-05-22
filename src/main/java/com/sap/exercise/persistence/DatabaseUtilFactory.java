package com.sap.exercise.persistence;

public class DatabaseUtilFactory {

    private static DatabaseUtil databaseUtil;

    public static DatabaseUtil getDatabaseUtil() {
        if (databaseUtil == null) {
            databaseUtil = new DatabaseUtil();
        }
        return databaseUtil;
    }

    public static void close() {
        if (databaseUtil != null)
            databaseUtil.close();
    }
}
