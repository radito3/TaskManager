package com.sap.exercise.commands;

import com.sap.exercise.handler.Dao;
import com.sap.exercise.handler.DeleteCondition;
import com.sap.exercise.handler.EventDao;
import com.sap.exercise.persistence.Property;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.DateArgumentEvaluator;
import com.sap.exercise.model.Event;

import java.util.NoSuchElementException;

public class DeleteEventCommand implements Command {

    private Event event;
    private DateArgumentEvaluator evaluator;
    private String start, end, eventName;
    private final Dao<Event> handler = new EventDao();

    public DeleteEventCommand(String start, String end, String eventName) {
        this.start = start;
        this.end = end;
        this.eventName = eventName;
    }

    @Override
    public CommandExecutionResult execute() {
        event = handler.get(new Property<>("title", eventName))
                       .orElseThrow(() -> new NoSuchElementException("Invalid event name"));
        evaluator = new DateArgumentEvaluator(start, end);
        evaluator.eval(this::deleteEvents);

        OutputPrinterProvider.getPrinter().printf("%nEvent entries deleted");
        return CommandExecutionResult.SUCCESSFUL;
    }

    private void deleteEvents(String start, String end) {
        handler.delete(event, new DeleteCondition(evaluator, start, end));
    }
}
