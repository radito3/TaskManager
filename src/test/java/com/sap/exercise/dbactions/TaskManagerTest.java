package com.sap.exercise.dbactions;

import com.sap.exercise.model.Task;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.jupiter.api.DisplayName;

import static org.junit.Assert.assertEquals;

public class TaskManagerTest {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    private Task task;

    @Before
    public void init() {
        task = new Task();
    }

    @Test
    @DisplayName("Database entry creation test")
    public void creationTest() {
        task.create();
        assertEquals(systemOutRule.getLog(), "event created\n");
    }

    @Test
    @DisplayName("Database entry updating with valid input test")
    public void updateWithValidInputTest() {
        task.update("test");
        assertEquals(systemOutRule.getLog(), "event updated\n");
    }

    @Test
    @DisplayName("Database entry deletion test")
    public void deletionTest() {
        task.delete();
        assertEquals(systemOutRule.getLog(), "event deleted\n");
    }
}
