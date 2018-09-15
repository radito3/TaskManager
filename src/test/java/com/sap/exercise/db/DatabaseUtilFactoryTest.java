package com.sap.exercise.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseUtilFactoryTest {

    @Test
    @DisplayName("Database client creation test")
    public void createDbClientTest() {
        DatabaseUtilFactory.createDbClient();
        assertAll("System property assertions", () -> {
            assertNotNull(System.getProperty("db-instance"), "System property for db instances is null");
            assertEquals("true", System.getProperty("db-instance"), "System property for db instances is false");
        });
    }

    @Test
    @DisplayName("Running database client test")
    public void getDbClientTest() {
        DatabaseUtil client = DatabaseUtilFactory.getDbClient();
        assertAll(() -> {
            assertNotNull(client, "Database client is null");
            assertNotNull(System.getProperty("db-instance"));
            assertEquals("true", System.getProperty("db-instance"));
        });
    }

    @Test
    @DisplayName("Db client restarting test")
    public void restartDbClientTest() {
        if (System.getProperty("db-instance") == null) {
            fail("System property for db instances is null");
        }
        DatabaseUtil client1 = DatabaseUtilFactory.getDbClient();
        System.setProperty("db-instance", "false");
        DatabaseUtil client2 = DatabaseUtilFactory.getDbClient();
        assertAll(() -> {
            assertNotEquals(client1, client2);
            assertNotNull(System.getProperty("db-instance"));
            assertEquals("true", System.getProperty("db-instance"));
        });
    }

}
