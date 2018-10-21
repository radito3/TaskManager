package com.sap.exercise.parser.commands;

import com.sap.exercise.parser.InputParser;

public class Exit implements Command {

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public void execute(String... args) {
        InputParser.close();
        System.exit(0);
    }
}
