package com.sap.exercise.db;

import com.sap.exercise.AbstractTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.*;

@EnabledOnJre({JRE.JAVA_8, JRE.JAVA_9})
@EnabledOnOs({OS.LINUX, OS.WINDOWS})
public class DatabaseClientHolderTest extends AbstractTest {

    @Test
    @DisplayName("Database client creation test")
    public void createDbClientTest() {
        DatabaseClientHolder.createDbClient();
        assertAll("System property assertions",
                () -> assertNotNull(System.getProperty("db-instance"), "System property for db instances is null"),
                () -> assertEquals("true", System.getProperty("db-instance"), "System property for db instances is false")
        );
    }

    @Test
    @DisplayName("Running database client test")
    @EnabledIfSystemProperty(named = "db-instance", matches = "true|false")
    public void getDbClientTest() {
        DatabaseUtil client = DatabaseClientHolder.getDbClient();
        assertAll("DB client integrity & System property assertions",
                () -> assertNotNull(client, "Database client is null"),
                () -> assertEquals("true", System.getProperty("db-instance"), "System property for db instances is false")
        );
    }

    @Test
    @DisplayName("Db client restarting test")
    @EnabledIfSystemProperty(matches = "true|false", named = "db-instance")
    public void restartDbClientTest() {
        DatabaseUtil client1 = DatabaseClientHolder.getDbClient();
        System.setProperty("db-instance", "false");
        DatabaseUtil client2 = DatabaseClientHolder.getDbClient();
        assertAll("DB clients equality & System property assertions",
                () -> assertNotEquals(client1, client2, "New client is equal to old one"),
                () -> assertEquals("true", System.getProperty("db-instance"), "System property for db instances is false")
        );
    }

}
