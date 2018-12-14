package com.sap.exercise.commands;

import com.sap.exercise.handler.EventHandler;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExitCommandTest {

    @Rule
    public ExpectedSystemExit ee = ExpectedSystemExit.none();

    @Test
    @DisplayName("Exit command name test")
    public void exitNameTest() {
        ExitCommand exit = new ExitCommand();
        assertEquals("exit", exit.getName(), "Exit command name is incorrect");
    }

    @org.junit.Test
    public void exitTest() {
        ee.expectSystemExitWithStatus(0);
        new ExitCommand().execute(new EventHandler());
    }
}
