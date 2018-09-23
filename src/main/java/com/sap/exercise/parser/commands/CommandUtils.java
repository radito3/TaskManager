package com.sap.exercise.parser.commands;

import com.sap.exercise.builder.EventBuilder;
import com.sap.exercise.model.Event;
import com.sap.exercise.printer.OutputPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Calendar;

public class CommandUtils {

    public static void interactiveInput(BufferedReader reader, OutputPrinter printer,
                                        EventBuilder builder, Event event) {
        try {
            for (String field : builder.getFields()) {
                printer.print(field + ": ");
                String input = reader.readLine();

                if (input.equals("")) {
                    Object val = Event.class.getDeclaredMethod("get" +
                            builder.getOrigFieldName(field)).invoke(event);
                    if (val instanceof Calendar) {
                        builder.append(field, ((Calendar) val).toInstant().toString());
                    } else {
                        builder.append(field, String.valueOf(val));
                    }
                    continue;
                }

                builder.append(field, input);
            }
        } catch (ReflectiveOperationException | IOException e) {
            printer.error("Error: " + e.getMessage());
        }
    }

    public static String buildEventName(String[] input) {
        if (input.length == 1) throw new IllegalArgumentException("Event name not specified");
        StringBuilder sb = new StringBuilder(input[1]);
        for (int i = 2; i < input.length; i++) sb.append(' ').append(input[i]);
        return sb.toString();
    }
}
