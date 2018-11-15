package com.sap.exercise.commands;

import static com.sap.exercise.Application.Configuration.OUTPUT;

import com.sap.exercise.printer.OutputPrinter;

//Again - good use of an interface

//The commands are effectively used as singletons, regardless that some are stateful and their state changes. 
//Why not use the factory design pattern and issue a new command instance every time one is needed? 
//That way, you'd be able to also initialize the state of the stateful ones in advance :)
public interface Command {

    OutputPrinter printer = new OutputPrinter(OUTPUT);

    String getName();

    void execute(String... args);

}
