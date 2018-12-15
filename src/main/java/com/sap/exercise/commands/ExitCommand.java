package com.sap.exercise.commands;

import com.sap.exercise.Application;
import com.sap.exercise.handler.EventHandler;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ExitCommand implements Command {

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public void execute(EventHandler handler, String... args) {
        try {
            Application.getParser().close();
        } catch (IOException e) {
            Logger.getLogger(ExitCommand.class).error("Error on closing input stream", e);
        }
        System.exit(0);
    }
}
