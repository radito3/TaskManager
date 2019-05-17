package com.sap.exercise.commands;

import com.sap.exercise.handler.EventDeletor;
import com.sap.exercise.handler.EventGetter;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.CommandExecutionException;
import com.sap.exercise.util.DateArgumentEvaluator;
import com.sap.exercise.model.Event;

import java.util.NoSuchElementException;

public class DeleteEventCommand implements Command {

    private Event event;
    private DateArgumentEvaluator evaluator;
    private String start, end, eventName;

    public DeleteEventCommand(String start, String end, String eventName) {
        this.start = start;
        this.end = end;
        this.eventName = eventName;
    }

    @Override
    public int execute() {
        try {
            event = new EventGetter().getEventByTitle(eventName);
            evaluator = new DateArgumentEvaluator(start, end);
            evaluator.eval(this::deleteEvents);

            OutputPrinterProvider.getPrinter().println("\nEvent entries deleted");
        } catch (NoSuchElementException | IllegalArgumentException e) {
            throw new CommandExecutionException(e);
        }
        return 0;
    }

    private void deleteEvents(String start, String end) {
        boolean condition = event.getToRepeat() != Event.RepeatableType.NONE || evaluator.numOfArgs() != 0;
        new EventDeletor(condition, start, end).execute(event);
    }
}
