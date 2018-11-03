package com.sap.exercise.commands;

import com.sap.exercise.printer.OutputPrinter;

import static com.sap.exercise.Application.Configuration.OUTPUT;

public interface Command {

    OutputPrinter printer = new OutputPrinter(OUTPUT);

    String getName();

    void execute(String... args);

}
