package com.sap.exercise.parser.commands;

public class Add implements Command {
    @Override
    public String getName() {
        return "add";
    }

    @Override
    public void execute() {
        System.out.println("in add class");
    }
}
