package com.sap.exercise.commands;

import com.sap.exercise.Application;
import com.sap.exercise.printer.OutputPrinter;

public interface Command {

    // Interface containing a variable. What if the implementation would not make use of this printer (ExitCommand for example does not)?
    // Perhaps introducing one more level of abstraction by adding an abstract class would make it better and easier to extend if the code requires extension in the future?
    // This way if I wanted to use notifications of some other sort rather than printing, 
    // it would never force me to touch the current code, all I'd have to do is define a new abstract class - apart from the default implementation.
    OutputPrinter printer = new OutputPrinter(Application.Configuration.OUTPUT);

    int execute(String... args);
}