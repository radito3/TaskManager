package com.sap.exercise.commands;

import com.sap.exercise.printer.OutputPrinter;
import com.sap.exercise.printer.OutputPrinterProvider;

class AbstractCommand {

    protected OutputPrinter printer = OutputPrinterProvider.getPrinter();

}
