package com.sap.exercise.commands;

import com.sap.exercise.Configuration;
import com.sap.exercise.handler.EventCreator;
import com.sap.exercise.printer.OutputPrinter;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.wrapper.EventWrapper;
import com.sap.exercise.wrapper.EventWrapperFactory;
import com.sap.exercise.model.Event;
import org.apache.commons.io.input.CloseShieldInputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AddEventCommand implements Command {

    private Event event;

    public AddEventCommand(Event event) {
        this.event = event;
    }

    @Override
    public int execute() {
        OutputPrinter printer = OutputPrinterProvider.getPrinter();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new CloseShieldInputStream(Configuration.INPUT)))) {
            EventWrapper eventWrapper = EventWrapperFactory.getEventWrapper(event);
            EventDataParser dataParser = new EventDataParser(reader, eventWrapper);

            dataParser.parseInput();

            new EventCreator().execute(eventWrapper.getEvent());
            printer.println("\nEvent created");
        } catch (IllegalArgumentException | IOException e) {
            printer.println(e.getMessage());
        }
        return 0;
    }
}