package com.sap.exercise.commands;

import com.sap.exercise.Application;
import com.sap.exercise.printer.OutputPrinter;

public interface Command {

    OutputPrinter printer = new OutputPrinter(Application.Configuration.OUTPUT);

    int execute(String... args);

    //TODO I see why you want the commands package to handle such relevant cases, but the only caller of that method is the Input Parser, and the input parser already has the responsibility of printing generic error messages to the console, related to parsing.
    //Maybe the Input Parser belongs to the same package?
    static void onInvalidCommand() {
        printer.println("Invalid command");
    }
}