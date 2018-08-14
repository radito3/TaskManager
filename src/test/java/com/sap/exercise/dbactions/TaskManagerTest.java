package com.sap.exercise.dbactions;

import com.sap.exercise.model.Task;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import static org.junit.Assert.assertEquals;

public class TaskManagerTest {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void creationTest() {
        Task task = new Task();
        task.create();
        assertEquals(systemOutRule.getLog(), "event created\n");
    }

    @Test
    public void updateWithValidInputTest() {
        Task task = new Task();
        task.update("test");
        assertEquals(systemOutRule.getLog(), "event updated\n");
    }
}
