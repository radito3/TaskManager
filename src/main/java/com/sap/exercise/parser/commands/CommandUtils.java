package com.sap.exercise.parser.commands;

import com.sap.exercise.builder.EventBuilder;
import com.sap.exercise.model.Event;
import com.sap.exercise.model.Mandatory;
import com.sap.exercise.printer.OutputPrinter;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

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

                input = checkMandatoryField(input, reader, printer, field, builder);

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

    private static String checkMandatoryField(String input, BufferedReader reader, OutputPrinter printer, String field,
                                       EventBuilder builder) throws ReflectiveOperationException, IOException {
        if (Event.class.getDeclaredField(StringUtils.uncapitalize(builder.getOrigFieldName(field)))
                .isAnnotationPresent(Mandatory.class) && input.equals("")) {
            do {
                printer.println("Field is mandatory!");
                printer.print(field + ": ");
                input = reader.readLine();
            } while (input.equals(""));
            return input;
        }
        return input;
    }

    public static String buildEventName(String[] input) {
        if (input.length == 0) throw new IllegalArgumentException("Event name not specified");
        StringBuilder sb = new StringBuilder(input[0]);
        for (int i = 1; i < input.length; i++) sb.append(' ').append(input[i]);
        return sb.toString();
    }

    //will need to make this intuitive for the different commands
    //for now it only handles Add
    public static CommandLine getParsedCmd(String[] args) throws ParseException {
        Option task = Option.builder("t")
                .required(false)
                .longOpt("task")
                .desc("Specify the event created to be a Task")
                .build();
        Option reminder = Option.builder("r")
                .required(false)
                .longOpt("reminder")
                .desc("Specify the event created to be a Reminder")
                .build();
        Option goal = Option.builder("g")
                .required(false)
                .longOpt("goal")
                .desc("Specify the event created to be a Goal")
                .build();
        Options options = new Options().addOption(task).addOption(reminder).addOption(goal);
        return new DefaultParser().parse(options, args, false);
    }
}
