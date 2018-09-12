package com.sap.exercise.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    @DisplayName("Constructor without arguments test")
    public void creationTest() {
        Task task = new Task();
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

    //TODO add tests for remaining class parameters

}
