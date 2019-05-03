package com.sap.exercise.commands;

import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.function.Function;

class CommandUtils {

    private CommandUtils() {
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
        String startTime = "",
                endTime = "",
                eventName = func.apply(cmd);

        if (cmd.hasOption('s')) {
            startTime = cmd.getOptionValue('s');
        }
        if (cmd.hasOption('e')) {
            endTime = cmd.getOptionValue('e') + "-";
        }

        return new String[] { startTime, endTime, eventName };
    }
}
