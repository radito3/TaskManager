package com.sap.exercise.parser.commands;

public class Add implements Command {

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public void execute(String... args) {

        printer.print("in add class");
    }
}
