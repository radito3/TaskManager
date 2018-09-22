package com.sap.exercise.parser.commands;

import com.sap.exercise.builder.AbstractBuilder;
import com.sap.exercise.builder.EventBuilder;
import com.sap.exercise.handler.EventsHandler;
import com.sap.exercise.model.Event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

import static com.sap.exercise.Main.INPUT;

public class Edit implements Command {

    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public void execute(String... args) {
        try {
            String name = buildEventName(args);
            Event event = EventsHandler.getObjectFromTitle(name);

            edit(event);
        } catch (NullPointerException npe) {
            printer.println("Invalid event name"); //these static strings could be set methods in OutputPrinter
        } catch (IllegalArgumentException iae) {
            printer.println("Event name not specified");
        }
    }

    private String buildEventName(String[] input) {
        if (input.length == 1) throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder(input[1]);
        for (int i = 2; i < input.length; i++) sb.append(' ').append(input[i]);
        return sb.toString();
    }

    private void edit(Event event) {
        try { //this body of code could be in InputParser
            //not closing the input stream so the application can continue running
            BufferedReader reader = new BufferedReader(new InputStreamReader(INPUT));

            EventBuilder builder = AbstractBuilder.getEventBuilder(event);

            for (String field : builder.getFields()) {
                printer.print(field + ": ");
                String input = reader.readLine();

                if (input.equals("")) {
                    Object val = Event.class.getDeclaredMethod("get" + builder.getOrigFieldName(field)).invoke(event);
                    builder.append(field, String.valueOf(val));
                    continue;
                }

                builder.append(field, input);
            }

            EventsHandler.update(builder.build());
            printer.println("Event updated");
        } catch (IOException e) {
            printer.error(e.getMessage());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            printer.error("whoops");
        }
    }

}
