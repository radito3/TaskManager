package com.sap.exercise;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.util.logging.Level;
import java.util.logging.Logger;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractTest {

    @BeforeAll
    protected void disableLogging() {
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
    }
}
