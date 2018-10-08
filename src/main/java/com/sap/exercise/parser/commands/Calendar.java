package com.sap.exercise.parser.commands;

import org.apache.commons.cli.*;

import java.util.Arrays;

public class Calendar implements Command {

    private java.util.Calendar cal = java.util.Calendar.getInstance();

    @Override
    public String getName() {
        return "cal";
    }

    @Override
    public void execute(String... args) {
        try {
            flagHandler(args);
        } catch (Exception e) {
            printer.println(e.getMessage());
        }
    }

    private void flagHandler(String[] args) {
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
        Options options = new Options().addOption(one).addOption(three).addOption(year).addOption(withEvents);
        CommandLine cmd;
        try {
            cmd = new DefaultParser().parse(options, args, false);
        } catch (ParseException e) {
            printer.error(e.getMessage());
            return;
        }

        if (cmd.hasOption('e')) {
            printer.error("Not implemented");
        }

        if (cmd.getOptions().length > 2 || optionsSizeWithoutEvents(cmd, withEvents) > 1) {
            throw new IllegalArgumentException("Invalid number of arguments");
        }

        if (cmd.hasOption('3')) {
            for (int i = -1; i < 2; i++) {
                printer.monthCalendar(cal.get(java.util.Calendar.MONTH) + i);
                printer.println();
            }
        } else if (cmd.hasOption('y')) {
            printer.yearCalendar(cmd.getOptionValues('y') == null ? cal.get(java.util.Calendar.YEAR) : Integer.valueOf(cmd.getOptionValue('y')));
        } else {
            printer.monthCalendar(cal.get(java.util.Calendar.MONTH));
        }
    }

    private long optionsSizeWithoutEvents(CommandLine cmd, Option withEvents) {
        return Arrays.stream(cmd.getOptions()).filter(o -> !o.equals(withEvents)).count();
    }
}
