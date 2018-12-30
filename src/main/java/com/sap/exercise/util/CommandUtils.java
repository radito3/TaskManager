package com.sap.exercise.util;

import com.sap.exercise.wrapper.EventWrapper;
import com.sap.exercise.wrapper.FieldInfo;
import com.sap.exercise.commands.Command;
import com.sap.exercise.printer.OutputPrinter;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

public class CommandUtils {

    public static void interactiveInput(BufferedReader reader, EventWrapper builder) {
        try {
            for (FieldInfo field : builder.getFields()) {
                Command.printer.print(field.getNameToDisplay() + ": " + OutputPrinter.CURSOR_RIGHT);

                String input = reader.readLine();

                input = checkMandatoryField(input, reader, field);

                if (!input.isEmpty())
                    field.handleArg(input);
            }
        } catch (IOException e) {
            Logger.getLogger(CommandUtils.class).error("Input reading error", e);
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
        return Arrays.stream(input)
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
                .desc("Create a Task (default)")
                .build();
        Option reminder = Option.builder("r")
                .required(false)
                .longOpt("reminder")
                .desc("Create a Reminder")
                .build();
        Option goal = Option.builder("g")
                .required(false)
                .longOpt("goal")
                .desc("Create a Goal")
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
                .desc("Display the whole year (default argument is current year)")
                .build();
        Option withEvents = Option.builder("e")
                .required(false)
                .longOpt("events")
                .desc("Display calendar with events highlighted")
                .build();
        return new Options().addOption(one).addOption(three).addOption(year).addOption(withEvents);
    }

    public static Options timeFrameOptions(boolean isDelete) {
        Option start = Option.builder("s")
                .required(false)
                .longOpt("start")
                .hasArg(true)
                .numberOfArgs(1)
                .optionalArg(false)
                .desc("Specify the start time from when to " + (isDelete ? "delete" : "get") + " entries")
                .build();
        Option end = Option.builder("e")
                .required(false)
                .longOpt("end")
                .hasArg(true)
                .numberOfArgs(1)
                .optionalArg(false)
                .desc("Specify the end time to when to " + (isDelete ? "delete" : "get") + " entries")
                .build();
        return new Options().addOption(start).addOption(end);
    }

    public static Options helpOptions() {
        Option add = Option.builder("ad")
                .required(false)
                .longOpt("add")
                .build();
        Option edit = Option.builder("e")
                .required(false)
                .longOpt("edit")
                .build();
        Option delete = Option.builder("d")
                .required(false)
                .longOpt("delete")
                .build();
        Option agenda = Option.builder("ag")
                .required(false)
                .longOpt("agenda")
                .build();
        Option calendar = Option.builder("c")
                .required(false)
                .longOpt("calendar")
                .build();
        return new Options().addOption(add).addOption(edit).addOption(delete).addOption(agenda).addOption(calendar);
    }

    public static long optionsSizeWithoutEvents(CommandLine cmd) {
        return Arrays.stream(cmd.getOptions())
                .filter(o -> !o.equals(calendarOptions().getOption("e")))
                .count();
    }

    public static String[] flagHandlerForTimeFrame(String[] args, Function<CommandLine, String> func) throws ParseException {
        return timeFrameFlagHandler(
                getParsedCmd(timeFrameOptions(true), args),
                func);
    }

    public static String[] flagHandlerForTimeFrame(String[] args) throws ParseException {
        return timeFrameFlagHandler(
                getParsedCmd(timeFrameOptions(false), args),
                cmd -> "");
    }

    private static String[] timeFrameFlagHandler(CommandLine cmd, Function<CommandLine, String> func) {
        String startTime = "", endTime = "", eventName = func.apply(cmd);

        if (cmd.hasOption('s')) {
            startTime = cmd.getOptionValue('s');
        }
        if (cmd.hasOption('e')) {
            endTime = cmd.getOptionValue('e') + "-";
        }

        return new String[] { startTime, endTime, eventName };
    }
}
