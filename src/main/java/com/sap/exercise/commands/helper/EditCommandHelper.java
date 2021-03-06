package com.sap.exercise.commands.helper;

import com.sap.exercise.printer.OutputPrinterProvider;
import org.apache.commons.cli.Options;

public class EditCommandHelper implements CommandHelper {

    @Override
    public void printHelp(Options options) {
        OutputPrinterProvider.getPrinter()
                .printHelp("edit <event name>",
                        String.format("%nEdit an event"), "", new Options());
    }
}
