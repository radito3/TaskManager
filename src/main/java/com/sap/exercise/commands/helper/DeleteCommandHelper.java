package com.sap.exercise.commands.helper;

import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.DateHandler;
import org.apache.commons.cli.Options;

public class DeleteCommandHelper implements CommandHelper {

    private Options options;

    public DeleteCommandHelper(Options options) {
        this.options = options;
    }

    @Override
    public void printHelp() {
        OutputPrinterProvider.getPrinter()
                .printHelp("delete <event name>",
                        String.format("%nDelete an event%n%n"),
                        String.format("%nThe time options accept time formats as follows:%n" +
                                String.join(String.format("%n"), DateHandler.DATE_FORMATS)),
                        options);
    }
}
