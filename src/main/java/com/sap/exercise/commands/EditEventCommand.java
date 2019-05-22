package com.sap.exercise.commands;

import java.util.NoSuchElementException;

import com.sap.exercise.handler.EventGetter;
import com.sap.exercise.handler.EventUpdater;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.ExceptionMessageHandler;
import com.sap.exercise.wrapper.EventWrapper;
import com.sap.exercise.wrapper.EventWrapperFactory;
import com.sap.exercise.model.Event;

public class EditEventCommand implements Command {

    private String eventName;

    public EditEventCommand(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public CommandExecutionResult execute() {
        try  {
            Event event = new EventGetter().getEventByTitle(eventName);
            EventWrapper eventWrapper = EventWrapperFactory.getEventWrapper(event);
            EventDataParser dataParser = new EventDataParser(eventWrapper);

            dataParser.parseInput();

            new EventUpdater().execute(eventWrapper.getEvent());
            OutputPrinterProvider.getPrinter().println("\nEvent updated");
        } catch (NoSuchElementException | IllegalArgumentException e) {
            ExceptionMessageHandler.setMessage(e.getMessage());
            return CommandExecutionResult.ERROR;
        }
        return CommandExecutionResult.SUCCESSFUL;
    }
}
