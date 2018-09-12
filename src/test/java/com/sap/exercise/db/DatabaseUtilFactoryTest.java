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

}
