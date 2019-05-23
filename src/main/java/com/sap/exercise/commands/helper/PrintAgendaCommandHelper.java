package com.sap.exercise.commands.helper;

import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.DateHandler;
import org.apache.commons.cli.Options;

public class PrintAgendaCommandHelper implements CommandHelper {

    private Options options;

    public PrintAgendaCommandHelper(Options options) {
        this.options = options;
    }

    @Override
    public void printHelp() {
        OutputPrinterProvider.getPrinter()
                .printHelp("agenda",
                        String.format("%nDisplay a weekly agenda (if not given time arguments)%n%n"),
                        String.format("%nThe time options accept time formats as follows:%n" +
                                String.join(String.format("%n"), DateHandler.DATE_FORMATS)),
                        options);
    }
}
