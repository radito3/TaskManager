package com.sap.exercise.commands.helper;

import com.sap.exercise.commands.parser.DeleteCommandParser;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.DateHandler;

public class DeleteCommandHelper {

    public DeleteCommandHelper() {
        OutputPrinterProvider.getPrinter()
                .printHelp("delete <event name>",
                        String.format("%nDelete an event%n%n"),
                        String.format("%nThe time options accept time formats as follows:%n" +
                                String.join(String.format("%n"), DateHandler.DATE_FORMATS)),
                        DeleteCommandParser.getOptions());
    }
}
