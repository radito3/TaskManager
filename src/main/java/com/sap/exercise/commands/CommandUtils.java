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

    static Options buildOptions(Option... options) {
        Options result = new Options();
        for (Option opt : options) {
            result.addOption(opt);
        }
        return result;
    }

    static CommandLine getParsedCmd(Options options, String[] args) throws ParseException {
        return new DefaultParser().parse(options, args, false);
    }

    static String[] flagHandlerForTimeFrame(String[] args, Function<CommandLine, String> func) throws ParseException {
        return timeFrameFlagHandler(
                getParsedCmd(DeleteCommand.getOptions(), args),
                func);
    }

    static String[] flagHandlerForTimeFrame(String[] args) throws ParseException {
        return timeFrameFlagHandler(
                getParsedCmd(PrintAgendaCommand.getOptions(), args),
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
