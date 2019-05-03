package com.sap.exercise.commands;

import com.sap.exercise.Application;
import com.sap.exercise.printer.OutputPrinter;

public interface Command {

    OutputPrinter printer = new OutputPrinter(Application.Configuration.OUTPUT);

    int execute(String... args);
}