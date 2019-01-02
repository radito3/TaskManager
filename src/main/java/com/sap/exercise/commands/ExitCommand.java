package com.sap.exercise.commands;

import com.sap.exercise.Application;
import com.sap.exercise.handler.ExitHandler;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ExitCommand implements Command {

    @Override
    public int execute(String... args) {
        try {
            Application.getParser().close();
        } catch (IOException e) {
            Logger.getLogger(ExitCommand.class).error("Error on closing input stream", e);
        }
        new ExitHandler().execute();
        System.clearProperty("db-instance");
        return 1;
    }
}
