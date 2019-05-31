package com.sap.exercise.commands;

import com.sap.exercise.handler.Dao;
import com.sap.exercise.handler.DeleteOptions;
import com.sap.exercise.handler.EventDao;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.DateArgumentEvaluator;
import com.sap.exercise.model.Event;
import com.sap.exercise.util.ExceptionMessageHandler;

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
        try {
            event = handler.get(eventName)
                    .orElseThrow(() -> new NoSuchElementException("Invalid event name"));
            evaluator = new DateArgumentEvaluator(start, end);
            evaluator.eval(this::deleteEvents);

            OutputPrinterProvider.getPrinter().printf("%nEvent entries deleted");
        } catch (NoSuchElementException | IllegalArgumentException e) {
            ExceptionMessageHandler.setMessage(e.getMessage());
            return CommandExecutionResult.ERROR;
        }
        return CommandExecutionResult.SUCCESSFUL;
    }

    private void deleteEvents(String start, String end) {
        handler.delete(event, new DeleteOptions(evaluator, start, end));
    }
}
