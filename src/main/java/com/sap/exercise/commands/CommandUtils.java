package com.sap.exercise.commands;

import com.sap.exercise.wrapper.EventWrapper;
import com.sap.exercise.wrapper.FieldInfo;
import com.sap.exercise.printer.OutputPrinter;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

class CommandUtils {

    static void interactiveInput(BufferedReader reader, EventWrapper wrapper) {
        try {
            for (FieldInfo field : wrapper.getFields()) {
                Command.printer.print(field.getNameToDisplay() + ": " + OutputPrinter.CURSOR_RIGHT);

                String input = reader.readLine();

                input = checkMandatoryField(input, reader, field);

                if (!input.isEmpty()) {
                    field.handleArg(input);
                }
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

    static String buildEventName(String[] input) {
        return Arrays.stream(input)
                .reduce((a, b) -> a.concat(" ").concat(b))
                .orElseThrow(() -> new IllegalArgumentException("Event name not specified"));
    }

    static CommandLine getParsedCmd(Options options, String[] args) throws ParseException {
        return new DefaultParser().parse(options, args, false);
    }

    static Options timeFrameOptions(boolean isDelete) {
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

    static String[] flagHandlerForTimeFrame(String[] args, Function<CommandLine, String> func) throws ParseException {
        return timeFrameFlagHandler(
                getParsedCmd(timeFrameOptions(true), args),
                func);
    }

    static String[] flagHandlerForTimeFrame(String[] args) throws ParseException {
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
