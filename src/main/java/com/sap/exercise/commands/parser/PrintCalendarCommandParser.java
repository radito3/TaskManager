package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.CommandExecutionResult;
import com.sap.exercise.commands.CommandUtils;
import com.sap.exercise.commands.PrintCalendarCommand;
import com.sap.exercise.commands.validator.CommandValidator;
import com.sap.exercise.commands.validator.PrintCalendarCommandValidator;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PrintCalendarCommandParser implements CommandParser {

    @Override
    public Command parse(String[] args) {
        CommandLine cmd = CommandParser.safeParseCmd(getOptions(), args);
        CommandValidator validator = new PrintCalendarCommandValidator(cmd);
        if (!validator.validate())
            return () -> CommandExecutionResult.ERROR;

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

    public static Options getOptions() {
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
        return CommandParserFactory.buildOptions(one, three, year, withEvents);
    }
}
