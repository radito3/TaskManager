package com.sap.exercise.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    @DisplayName("Constructor without arguments test")
    public void creationTest() {
        Task task = new Task();
        assertAll("Class parameters assertions", () -> {
            assertEquals(task.getTitle(), "task title");
            assertEquals(task.getBody(), "task body");
            assertEquals(task.getAllDay(), false);
            assertEquals(task.getDuration(), Time.valueOf(LocalTime.MIN));
        });
    }

    @Test
    @DisplayName("Constructor with one argument test")
    public void creationWithOneArgumentTest() {
        Task task = new Task("test");
        assertAll(() -> {
            assertEquals(task.getTitle(), "test");
            assertEquals(task.getBody(), "task body");
            assertEquals(task.getAllDay(), false);
            assertEquals(task.getDuration(), Time.valueOf(LocalTime.MIN));
        });
    }

    @Test
    @DisplayName("Constructor with two arguments test")
    public void creationWithTwoArgumentsTest() {
        Task task = new Task("title", "body");
        assertAll(() -> {
            assertEquals(task.getTitle(), "title");
            assertEquals(task.getBody(), "body");
            assertEquals(task.getAllDay(), false);
            assertEquals(task.getDuration(), Time.valueOf(LocalTime.MIN));
        });
    }

    @Test
    @DisplayName("Constructor with four arguments test")
    public void creationWithFourArgumentsTest() {
        Task task = new Task("title", "body", true, Time.valueOf(LocalTime.NOON));
        assertAll(() -> {
            assertEquals(task.getTitle(), "title");
            assertEquals(task.getBody(), "body");
            assertEquals(task.getAllDay(), true);
            assertEquals(task.getDuration(), Time.valueOf(LocalTime.NOON));
        });
    }
    /*
    @Test
    @DisplayName("Title setting with correct argument test")
    public void setTitleTest() {
        Task task = new Task();
        task.setTitle(".test-1");
        assertEquals(task.getTitle(), ".test-1");
    }

    @Test
    @DisplayName("Title setting with incorrect argument test")
    public void setIncTitleTest() {
        Task task = new Task();
        assertThrows(IllegalArgumentException.class, () -> task.setTitle("$%@!&*()"));
    }

    @Test
    @DisplayName("Body setting with correct argument test")
    public void setBodyTest() {

    }

    @Test
    @DisplayName("Body setting with incorrect argument test")
    public void setIncBodyTest() {

    }

    @Test
    @DisplayName("Duration setting with correct argument test")
    public void setDurationTest() {

    }

    @Test
    @DisplayName("Duration setting with incorrect argument test")
    public void setIncDurationTest() {

    }
    */
}
