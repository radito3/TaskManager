package com.sap.exercise.commands.helper;

import com.sap.exercise.commands.parser.PrintAgendaCommandParser;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.DateHandler;

public class PrintAgendaCommandHelper {

    public PrintAgendaCommandHelper() {
        OutputPrinterProvider.getPrinter()
                .printHelp("agenda",
                        String.format("%nDisplay a weekly agenda (if not given time arguments)%n%n"),
                        String.format("%nThe time options accept time formats as follows:%n" +
                                String.join(String.format("%n"), DateHandler.DATE_FORMATS)),
                        PrintAgendaCommandParser.getOptions());
    }
}
