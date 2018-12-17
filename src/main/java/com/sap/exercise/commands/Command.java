package com.sap.exercise.commands;

import com.sap.exercise.Application;
import com.sap.exercise.handler.EventHandler;
import com.sap.exercise.printer.OutputPrinter;

public interface Command {

    OutputPrinter printer = new OutputPrinter(Application.Configuration.OUTPUT);

    String getName();

    int execute(EventHandler handler, String... args);

    static void onInvalidCommand() {
        printer.println("Invalid command");
    }
}
