package com.sap.exercise.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

import com.sap.exercise.Configuration;
import com.sap.exercise.handler.EventGetter;
import com.sap.exercise.handler.EventUpdater;
import com.sap.exercise.printer.OutputPrinter;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.CommandExecutionException;
import com.sap.exercise.wrapper.EventWrapper;
import com.sap.exercise.wrapper.EventWrapperFactory;
import com.sap.exercise.model.Event;
import org.apache.commons.io.input.CloseShieldInputStream;

public class EditEventCommand implements Command {

    private String eventName;

    public EditEventCommand(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public int execute() {
        OutputPrinter printer = OutputPrinterProvider.getPrinter();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new CloseShieldInputStream(Configuration.INPUT)))) {
            Event event = new EventGetter().getEventByTitle(eventName);
            EventWrapper eventWrapper = EventWrapperFactory.getEventWrapper(event);
            EventDataParser dataParser = new EventDataParser(reader, eventWrapper);

            dataParser.parseInput();

            new EventUpdater().execute(eventWrapper.getEvent());
            printer.println("\nEvent updated");
        } catch (NoSuchElementException | IllegalArgumentException | IOException e) {
            throw new CommandExecutionException(e);
        }
        return 0;
    }
}
