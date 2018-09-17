package com.sap.exercise.parser.commands;

public class Edit implements Command {
    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public void execute() {
        System.out.println("in edit class");
    }
}
