package com.sap.exercise.commands.helper;

import com.sap.exercise.commands.parser.AddCommandParser;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.printer.PrinterColors;

public class AddCommandHelper {

    public AddCommandHelper() {
        OutputPrinterProvider.getPrinter()
                .printHelp("add",
                        String.format("%nCreate an event%n%n"),
                        String.format("%n" + PrinterColors.BOLD + "Note: "+ PrinterColors.RESET +
                                "Only one of the options can be present"),
                        AddCommandParser.getOptions());
    }
}
