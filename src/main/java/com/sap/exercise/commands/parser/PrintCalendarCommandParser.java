package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.CommandUtils;
import com.sap.exercise.commands.PrintCalendarCommand;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.ArrayUtils;

public class PrintCalendarCommandParser implements CommandParser {

    @Override
    public Command parse(String[] args) {
        CommandLine cmd;
        if ((cmd = CommandParser.safeParseCmd(printCalendarOptions(), args)) == null)
            return () -> 0;
        if (cmd.getOptions().length > 2 || optionsSizeWithoutEvents(cmd) > 1) {
            throw new IllegalArgumentException("Invalid number of arguments");
        }

        boolean withEvents = cmd.hasOption('e');
        if (cmd.hasOption('3')) {
            return new PrintCalendarCommand(CommandUtils.PrintCalendarOptions.THREE, withEvents);
        }
        if (cmd.hasOption('y')) {
            CommandUtils.PrintCalendarOptions options = CommandUtils.PrintCalendarOptions.YEAR;
            if (cmd.getOptionValues('y') != null) {
                options.setArgument(cmd.getOptionValue('y'));
            }
            return new PrintCalendarCommand(options, withEvents);
        }
        return new PrintCalendarCommand(CommandUtils.PrintCalendarOptions.ONE, withEvents);
    }

    private int optionsSizeWithoutEvents(CommandLine cmd) {
        return ArrayUtils.removeElement(cmd.getOptions(), printCalendarOptions().getOption("e")).length;
    }

    public static Options printCalendarOptions() {
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
        return CommandUtils.buildOptions(one, three, year, withEvents);
    }
}
