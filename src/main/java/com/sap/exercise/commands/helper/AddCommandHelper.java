package com.sap.exercise.commands.helper;

import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.printer.PrinterColors;
import org.apache.commons.cli.Options;

public class AddCommandHelper implements CommandHelper {

    private Options options;

    public AddCommandHelper(Options options) {
        this.options = options;
    }

    @Override
    public void printHelp() {
        OutputPrinterProvider.getPrinter()
                .printHelp("add",
                        String.format("%nCreate an event%n%n"),
                        String.format("%n" + PrinterColors.BOLD + "Note: "+ PrinterColors.RESET +
                                "Only one of the options can be present"),
                        options);
    }
}
