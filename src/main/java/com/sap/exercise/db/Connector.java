package com.sap.exercise.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class Connector {

    public static void test() {
        // taken straight from tutorialspoint
        // not tested
        Connection c;
        Statement stmt;
        try {
            Class.forName("org.postgresql.Driver");
            Properties p = new Properties();
            p.put("user", "postgres");
            p.put("password", "postgres");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/postgres", p);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE COMPANY " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " AGE            INT     NOT NULL, " +
                    " ADDRESS        CHAR(50), " +
                    " SALARY         REAL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }
}
