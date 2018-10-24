package com.sap.exercise.commands.util;

import com.sap.exercise.builder.EventBuilder;
import com.sap.exercise.builder.FieldInfo;
import com.sap.exercise.commands.Command;
import com.sap.exercise.printer.OutputPrinter;
import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class CommandUtils {

    public static void interactiveInput(BufferedReader reader, EventBuilder builder) {
        try {
            for (FieldInfo field : builder.getFields()) {
                Command.printer.print(field.getNameToDisplay() + ": " + OutputPrinter.CURSOR_RIGHT);

                String input = reader.readLine();

                input = checkMandatoryField(input, reader, field);

                if (!input.isEmpty())
                    builder.append(field.getName(), input);
            }
        } catch (IOException e) {
            Command.printer.error("Error: " + e.getMessage());
        }
    }

    private static String checkMandatoryField(String input, BufferedReader reader, FieldInfo fInfo) throws IOException {
        if (fInfo.isMandatory() && input.isEmpty()) {
            do {
                Command.printer.println("Field is mandatory!");
                Command.printer.print(fInfo.getNameToDisplay() + ": " + OutputPrinter.CURSOR_RIGHT);

                input = reader.readLine();
            } while (input.isEmpty());
        }
        return input;
    }

    public static String buildEventName(String[] input) {
        return Stream.of(input)
                .reduce((a, b) -> a.concat(" ").concat(b))
                .orElseThrow(() -> new IllegalArgumentException("Event name not specified"));
    }

    public static CommandLine getParsedCmd(Options options, String[] args) throws ParseException {
        return new DefaultParser().parse(options, args, false);
    }

    public static Options addOptions() {
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
        return new Options().addOption(task).addOption(reminder).addOption(goal);
    }

    public static Options calendarOptions() {
        Option one = Option.builder("1")
                .required(false)
                .longOpt("one")
                .desc("Display one month")
                .build();
        Option three = Option.builder("3")
                .required(false)
                .longOpt("three")
                .desc("Display three months")
                .build();
        Option year = Option.builder("y")
                .required(false)
                .longOpt("year")
                .hasArg()
                .optionalArg(true)
                .desc("Display the whole year")
                .build();
        Option withEvents = Option.builder("e")
                .required(false)
                .longOpt("events")
                .desc("Display calendar with events highlighted")
                .build();
        return new Options().addOption(one).addOption(three).addOption(year).addOption(withEvents);
    }

    public static Options timeFrameOptions() {
        Option start = Option.builder("s")
                .required(false)
                .longOpt("start")
                .hasArg(true)
                .numberOfArgs(1)
                .optionalArg(false)
                .desc("Specify the start time from when to get/delete entries")
                .build();
        Option end = Option.builder("e")
                .required(false)
                .longOpt("end")
                .hasArg(true)
                .numberOfArgs(1)
                .optionalArg(false)
                .desc("Specify the end time to when to get/delete entries")
                .build();
        return new Options().addOption(start).addOption(end);
    }

    public static long optionsSizeWithoutEvents(CommandLine cmd) {
        return Arrays.stream(cmd.getOptions()).filter(o -> !o.equals(calendarOptions().getOption("e"))).count();
    }
}
