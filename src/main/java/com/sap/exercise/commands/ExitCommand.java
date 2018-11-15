package com.sap.exercise.commands;

import com.sap.exercise.parser.InputParser;

public class ExitCommand implements Command {

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
