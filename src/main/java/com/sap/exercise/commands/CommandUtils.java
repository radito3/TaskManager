package com.sap.exercise.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Optional;

public class CommandUtils {

    private CommandUtils() {
    }

    public static String buildEventName(String[] input) {
        return Optional.ofNullable(input)
                .flatMap(arr -> Optional.of(String.join(" ", arr)))
                .orElseThrow(() -> new IllegalArgumentException("Event name not specified"));
    }

    static Options buildOptions(Option... options) {
        Options result = new Options();
        for (Option opt : options) {
            result.addOption(opt);
        }
        return result;
    }

    public static CommandLine getParsedCmd(Options options, String[] args) throws ParseException {
        return new DefaultParser().parse(options, args, false);
    }
}
