package com.sap.exercise.parser.commands;

import com.sap.exercise.printer.OutputPrinter;

import static com.sap.exercise.Main.OUTPUT;

public interface Command {

    OutputPrinter printer = new OutputPrinter(OUTPUT);

    String getName();

    void execute(String... args);
}
