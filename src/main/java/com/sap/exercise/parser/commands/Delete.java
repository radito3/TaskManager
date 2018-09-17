package com.sap.exercise.parser.commands;

public class Delete implements Command {
    @Override
    public String getName() {
        return "del";
    }

    @Override
    public void execute() {
        System.out.println("in delete class");
    }
}
