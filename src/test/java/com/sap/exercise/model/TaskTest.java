package com.sap.exercise.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TaskTest {

    @Test
    public void creationTest() {
        Task task = new Task();
        assertEquals(task.getBody(), "task body");
    }

    @Test
    public void creationWithArgumentTest() {
        Task task = new Task("test");
        assertEquals(task.getBody(), "test");
    }
}
