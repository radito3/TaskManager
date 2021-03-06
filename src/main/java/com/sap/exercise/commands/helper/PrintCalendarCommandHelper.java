package com.sap.exercise.commands.helper;

import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.printer.PrinterColors;
import org.apache.commons.cli.Options;

public class PrintCalendarCommandHelper implements CommandHelper {

    @Override
    public void printHelp(Options options) {
        OutputPrinterProvider.getPrinter()
                .printHelp("cal",
                        String.format("%nDisplay a calendar%n%n"),
                        String.format("%n" + PrinterColors.BOLD + "Note: "+ PrinterColors.RESET +
                                "Only one of the options can be present"),
                        options);
    }
}
