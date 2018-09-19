package com.sap.exercise.parser.commands;

public class Add implements Command {

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public void execute(String... args) {
        //if AllDay is true -> Duration will be in number of days
        //if AllDay is false -> Duration is number of minutes
        printer.print("in add class");
    }
}
