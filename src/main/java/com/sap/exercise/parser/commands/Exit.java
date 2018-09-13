package com.sap.exercise.parser.commands;

public class Exit implements Command {

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public void execute() {
        System.exit(0);
    }
}
