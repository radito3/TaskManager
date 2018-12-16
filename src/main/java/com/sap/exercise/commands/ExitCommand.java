package com.sap.exercise.commands;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.sap.exercise.Application;
import com.sap.exercise.handler.EventHandler;

public class ExitCommand implements Command {

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public void execute(EventHandler handler, String... args) {
        // maybe the exit command should not take part in the life cycle management of the parser?
        // and btw should one wait for the thread pool queue to be emptied before jvm suicide?
        try {
            Application.getParser().close();
        } catch (IOException e) {
            Logger.getLogger(ExitCommand.class).error("Error on closing input stream", e);
        }
        // and maybe here is not the right place to system.exit. Why not handle this with flow control and let the main method of the app
        // just end?
        System.exit(0);
    }
}
