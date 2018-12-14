package com.sap.exercise;

import com.sap.exercise.handler.EventHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.util.logging.Level;
import java.util.logging.Logger;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractTest {

    protected EventHandler handler = new EventHandler();

    @BeforeAll
    protected void disableLogging() {
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
    }
}
