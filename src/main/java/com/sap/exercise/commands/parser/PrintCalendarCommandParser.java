package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.PrintCalendarCommand;
import com.sap.exercise.commands.helper.PrintCalendarCommandHelper;
import com.sap.exercise.commands.validator.PrintCalendarCommandValidator;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PrintCalendarCommandParser extends AbstractCommandParser {

    PrintCalendarCommandParser() {
        super(new PrintCalendarCommandHelper(), new PrintCalendarCommandValidator());
    }

    @Override
    Command parseInternal(CommandLine cmd) {
        boolean withEvents = cmd.hasOption('e');
        if (cmd.hasOption('3')) {
            return new PrintCalendarCommand(PrintCalendarCommand.PrintCalendarOptions.THREE, withEvents);
        }

        if (cmd.hasOption('y')) {
            PrintCalendarCommand.PrintCalendarOptions options = PrintCalendarCommand.PrintCalendarOptions.YEAR;
            if (cmd.getOptionValues('y') != null) {
                options.setArgument(cmd.getOptionValue('y'));
            }
            return new PrintCalendarCommand(options, withEvents);
        }

        return new PrintCalendarCommand(PrintCalendarCommand.PrintCalendarOptions.ONE, withEvents);
    }

    @Override
    public Options getOptions() {
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
                .argName("year")
                .optionalArg(true)
                .desc("Display the whole year (default argument is current year)")
                .build();
        Option withEvents = Option.builder("e")
                .required(false)
                .longOpt("events")
                .desc("Display calendar with events highlighted")
                .build();
        Option help = Option.builder()
                .longOpt("help")
                .required(false)
                .desc("Print command help")
                .build();
        return buildOptions(one, three, year, withEvents, help);
    }
}
