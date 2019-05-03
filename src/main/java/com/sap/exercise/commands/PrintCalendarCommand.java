package com.sap.exercise.commands;

import com.sap.exercise.handler.EventGetter;
import com.sap.exercise.handler.EventsGetterHandler;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Calendar;

public class PrintCalendarCommand implements Command, CommandOptions {

    @Override
    public int execute(String... args) {
        try {
            Calendar cal = Calendar.getInstance();
            CommandLine cmd = CommandUtils.getParsedCmd(getOptions(), args);
            EventsGetterHandler handler = new EventGetter();

            if (cmd.getOptions().length > 2 || optionsSizeWithoutEvents(cmd) > 1) {
                throw new IllegalArgumentException("Invalid number of arguments");
            }

            if (cmd.hasOption('3')) {
                for (int i = -1; i < 2; i++) {
                    printer.monthCalendar(handler, cal.get(Calendar.MONTH) + i, cmd.hasOption('e'));
                    printer.println();
                }
            } else if (cmd.hasOption('y')) {
                int year = cmd.getOptionValues('y') == null ? cal.get(Calendar.YEAR) : Integer.valueOf(cmd.getOptionValue('y'));
                printer.yearCalendar(handler, year, cmd.hasOption('e'));
            } else {
                printer.monthCalendar(handler, cal.get(Calendar.MONTH), cmd.hasOption('e'));
            }
        } catch (ParseException | IllegalArgumentException e) {
            printer.println(e.getMessage());
        }
        return 0;
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

    private int optionsSizeWithoutEvents(CommandLine cmd) {
        return ArrayUtils.removeElement(cmd.getOptions(), getOptions().getOption("e")).length;
    }
}
