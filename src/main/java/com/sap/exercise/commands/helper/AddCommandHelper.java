package com.sap.exercise.commands.helper;

import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.printer.PrinterColors;
import org.apache.commons.cli.Options;

public class AddCommandHelper implements CommandHelper {

    @Override
    public void printHelp(Options options) {
        OutputPrinterProvider.getPrinter()
                .printHelp("add",
                        String.format("%nCreate an event%n%n"),
                        String.format("%n" + PrinterColors.BOLD + "Note: "+ PrinterColors.RESET +
                                "Only one of the options can be present"),
                        options);
    }
}
