package com.sap.exercise.dbactions;

import com.sap.exercise.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
//import org.junit.platform.runner.JUnitPlatform;
//import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@RunWith(JUnitPlatform.class)
public class TaskManagerTest {

    private Task task;
    private static ByteArrayOutputStream out;

    @BeforeAll
    public static void setup() {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @AfterAll
    public static void onEnd() {
        System.setOut(System.out);
    }

    @BeforeEach
    public void init() {
        task = new Task();
    }

    @AfterEach
    public void after() {
        out.reset();
    }

    @Test
    @DisplayName("Database entry creation test")
    public void creationTest() {
        task.create();
        assertEquals("event created\n", out.toString());
    }

    @Test
    @DisplayName("Database entry updating with valid input test")
    public void updateWithValidInputTest() {
        task.update("test");
        assertEquals("event updated\n", out.toString());
    }

    @Test
    @DisplayName("Database entry deletion test")
    public void deletionTest() {
        task.delete();
        assertEquals("event deleted\n", out.toString());
    }
}
