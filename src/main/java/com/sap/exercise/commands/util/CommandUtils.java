package com.sap.exercise.commands.util;

import com.sap.exercise.builder.EventBuilder;
import com.sap.exercise.builder.FieldInfo;
import com.sap.exercise.commands.Command;
import com.sap.exercise.printer.OutputPrinter;
import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.function.Function;

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
        return Arrays.stream(cmd.getOptions())
                .filter(o -> !o.equals(calendarOptions().getOption("e")))
                .count();
    }

    public static String[] flagHandlerForTimeFrame(String[] args, Function<CommandLine, String> func) throws ParseException {
        CommandLine cmd = getParsedCmd(timeFrameOptions(), args);

        String startTime = "", endTime = "", eventName = func.apply(cmd);
        if (cmd.hasOption('s')) {
            startTime = cmd.getOptionValue('s');
        }
        if (cmd.hasOption('e')) {
            endTime = cmd.getOptionValue('e') + "-";
        }

        return new String[] { startTime, endTime, eventName };
    }

    public static String[] flagHandlerForTimeFrame(String[] args) throws ParseException {
        return flagHandlerForTimeFrame(args, cmd -> "");
    }

    public static int[] getToday() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR),
                month = cal.get(Calendar.MONTH) + 1,
                day = cal.get(Calendar.DAY_OF_MONTH);
        return new int[] { day, month, year };
    }

    public static int[] getTime(int day, int month, int year) {
        if (month > 12) {
            return getTime(day, 1, year + 1);
        }
        if (day > getMonthDays()[month]) {
            return getTime(day - getMonthDays()[month], month + 1, year);
        }
        return new int[] { day, month, year };
    }

    public static int[] getMonthDays() {
        return new int[] { 0, 31, isLeapYear() ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    }

    private static boolean isLeapYear(/*int year*/) {
        Calendar cal = Calendar.getInstance();
        /*cal.set(Calendar.YEAR, year);  --- for argument function */
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }
}
