package com.sap.exercise.parser.commands;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

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
                .desc("Display the whole year")
                .build();
        Options options = new Options().addOption(one).addOption(three).addOption(year);
    }
}
