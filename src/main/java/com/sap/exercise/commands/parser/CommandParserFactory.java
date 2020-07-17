package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.ExitCommand;
import com.sap.exercise.commands.PrintHelpCommand;

public class CommandParserFactory {

    private CommandParserFactory() {
    }

    public static CommandParser newCommandParser(String command) {
        switch (command) {
            case "add":
                return new AddCommandParser();
            case "edit":
                return new EditCommandParser();
            case "delete":
                return new DeleteCommandParser();
            case "cal":
                return new PrintCalendarCommandParser();
            case "agenda":
                return new PrintAgendaCommandParser();
            case "help" :
                return args -> new PrintHelpCommand();
            case "exit":
                return args -> new ExitCommand();
        }
        throw new IllegalArgumentException("Invalid command");
    }
}
