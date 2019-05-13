package com.sap.exercise.commands;

import com.sap.exercise.handler.EventDeletor;
import com.sap.exercise.handler.EventDeletorTF;
import com.sap.exercise.handler.EventGetter;
import com.sap.exercise.printer.OutputPrinter;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.DateArgumentEvaluator;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.NoSuchElementException;

public class DeleteCommand implements Command {

    private Event event;
    private DateArgumentEvaluator evaluator;
    private String start, end, eventName;

    public DeleteCommand(String start, String end, String eventName) {
        this.start = start;
        this.end = end;
        this.eventName = eventName;
    }

    @Override
    public int execute() {
        OutputPrinter printer = OutputPrinterProvider.getPrinter();
        try {
            event = new EventGetter().getEventByTitle(eventName);
            evaluator = new DateArgumentEvaluator(start, end);
            int result = evaluator.eval(this::deleteEvents);

            printer.println(result == 0 ? "\nEvent deleted" : "\nEvent entries deleted");
        } catch (NullPointerException | NoSuchElementException | IllegalArgumentException e) {
            printer.println(e.getMessage());
        }
        return 0;
    }

    private int deleteEvents(String start, String end) {
        if (event.getToRepeat() == Event.RepeatableType.NONE || evaluator.numOfArgs() == 0) {
            new EventDeletor().execute(event);
            return 0;
        }
        new EventDeletorTF().execute(event, start, end);
        return 1;
    }

    public static Options getOptions() {
        Option start = Option.builder("s")
                .required(false)
                .longOpt("start")
                .hasArg(true)
                .numberOfArgs(1)
                .optionalArg(false)
                .desc("Specify the start time from when to delete entries")
                .build();
        Option end = Option.builder("e")
                .required(false)
                .longOpt("end")
                .hasArg(true)
                .numberOfArgs(1)
                .optionalArg(false)
                .desc("Specify the end time to when to delete entries")
                .build();
        return CommandUtils.buildOptions(start, end);
    }
}
