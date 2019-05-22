package com.sap.exercise.commands.helper;

import com.sap.exercise.commands.parser.PrintCalendarCommandParser;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.printer.PrinterColors;

public class PrintCalendarCommandHelper {

    public PrintCalendarCommandHelper() {
        OutputPrinterProvider.getPrinter()
                .printHelp("cal",
                        String.format("%nDisplay a calendar%n%n"),
                        String.format("%n" + PrinterColors.BOLD + "Note: "+ PrinterColors.RESET +
                                "Only one of the options can be present"),
                        PrintCalendarCommandParser.getOptions());
    }
}
