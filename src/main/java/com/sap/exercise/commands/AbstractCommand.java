package com.sap.exercise.commands;

import com.sap.exercise.Application;
import com.sap.exercise.printer.OutputPrinter;

class AbstractCommand {

    protected OutputPrinter printer = new OutputPrinter(Application.Configuration.OUTPUT);

}
