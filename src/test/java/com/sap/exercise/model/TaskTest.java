package com.sap.exercise.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TaskTest {

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
    @DisplayName("Constructor without arguments test")
    public void creationTest() {
        assertAll(() -> {
            assertEquals(task.getTitle(), "task title");
            assertEquals(task.getBody(), "task body");
        });
    }

    @Test
    @DisplayName("Constructor with one argument test")
    public void creationWithOneArgumentTest() {
        Task task = new Task("test");
        assertAll(() -> {
            assertEquals(task.getTitle(), "test");
            assertEquals(task.getBody(), "task body");
        });
    }

    @Test
    @DisplayName("Constructor with two arguments test")
    public void creationWithTwoArgumentsTest() {
        Task task = new Task("title", "body");
        assertAll(() -> {
            assertEquals(task.getTitle(), "title");
            assertEquals(task.getBody(), "body");
        });
    }

    @Test
    @DisplayName("Database entry creation test")
    public void dbCreationTest() {
        task.create(new Task());
        assertEquals("event created\n", out.toString());
    }

    @Test
    @DisplayName("Database entry updating with valid input test")
    public void updateWithValidInputTest() {
        task.update(new Task());
        assertEquals("event updated\n", out.toString());
    }

    @Test
    @DisplayName("Database entry deletion test")
    public void deletionTest() {
        task.delete(new Task());
        assertEquals("event deleted\n", out.toString());
    }
}
