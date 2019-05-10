package com.sap.exercise.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

import com.sap.exercise.Application;
import com.sap.exercise.handler.EventGetter;
import com.sap.exercise.handler.EventUpdater;
import com.sap.exercise.wrapper.EventWrapper;
import com.sap.exercise.wrapper.EventWrapperFactory;
import com.sap.exercise.model.Event;
import org.apache.commons.io.input.CloseShieldInputStream;

public class EditCommand extends AbstractCommand implements Command {

    @Override
    public int execute(String... args) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new CloseShieldInputStream(Application.Configuration.INPUT)))) {
            // Take a look at first comment inside execute method of AddCommand class
            String name = CommandUtils.buildEventName(args);
            Event event = new EventGetter().getEventByTitle(name);
            EventWrapper eventWrapper = EventWrapperFactory.getEventWrapper(event);
            InteractiveInput input = new InteractiveInput(reader, eventWrapper);

            input.parseInput();

            new EventUpdater().execute(eventWrapper.getEvent());
            printer.println("\nEvent updated");
        } catch (NullPointerException | NoSuchElementException | IllegalArgumentException | IOException e) {
            printer.println(e.getMessage());
        }
        return 0;
    }
}
