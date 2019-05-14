package com.sap.exercise.commands;

import com.sap.exercise.handler.EventGetter;
import com.sap.exercise.printer.OutputPrinter;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.DateArgumentEvaluator;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.io.Serializable;
import java.util.Set;

public class PrintAgendaCommand implements Command, Serializable {

    private String start, end;

    public PrintAgendaCommand(String start, String end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public int execute() {
        OutputPrinter printer = OutputPrinterProvider.getPrinter();
        try {
            DateArgumentEvaluator dateArgumentEvaluator = new DateArgumentEvaluator(start, end);
            Set<Event> events = dateArgumentEvaluator.eval(new EventGetter()::getEventsInTimeFrame);

            if (events.isEmpty()) {
                printer.println("\nNo upcoming events");
            } else {
                OutputPrinterProvider.getPrinter().printEvents(events);
            }
        } catch (IllegalArgumentException e) {
            printer.println(e.getMessage());
        }
        return 0;
    }

    public static Options getOptions() {
        Option start = Option.builder("s")
                .required(false)
                .longOpt("start")
                .hasArg(true)
                .numberOfArgs(1)
                .optionalArg(false)
                .desc("Specify the start time from when to get entries")
                .build();
        Option end = Option.builder("e")
                .required(false)
                .longOpt("end")
                .hasArg(true)
                .numberOfArgs(1)
                .optionalArg(false)
                .desc("Specify the end time to when to get entries")
                .build();
        return CommandUtils.buildOptions(start, end);
    }
}
