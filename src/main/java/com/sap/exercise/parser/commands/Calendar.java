package com.sap.exercise.parser.commands;

import org.apache.commons.cli.*;

public class Calendar implements Command {

    @Override
    public String getName() {
        return "cal";
    }

    @Override
    public void execute(String... args) {
        printer.println("in calendar class");
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
        }

    }
}
