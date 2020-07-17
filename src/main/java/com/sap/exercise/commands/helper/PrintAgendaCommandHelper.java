package com.sap.exercise.commands.helper;

import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.DateParser;
import org.apache.commons.cli.Options;

public class PrintAgendaCommandHelper implements CommandHelper {

    @Override
    public void printHelp(Options options) {
        OutputPrinterProvider.getPrinter()
                .printHelp("agenda",
                        String.format("%nDisplay a weekly agenda (if not given time arguments)%n%n"),
                        String.format("%nThe time options accept time formats as follows:%n" +
                                String.join(String.format("%n"), DateParser.DATE_FORMATS)),
                        options);
    }
}
