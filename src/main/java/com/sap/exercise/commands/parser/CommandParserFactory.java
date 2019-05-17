package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.CommandUtils;
import com.sap.exercise.commands.EditEventCommand;
import com.sap.exercise.commands.ExitCommand;

public class CommandParserFactory {

    public static CommandParser getParser(String command) {
        switch (command) {
            case "add":
                return new AddCommandParser();
            case "edit":
                return args -> new EditEventCommand(CommandUtils.buildEventName(args));
            case "delete":
                return new DeleteCommandParser();
            case "cal":
                return new PrintCalendarCommandParser();
            case "agenda":
                return new PrintAgendaCommandParser();
            case "help":
                return new PrintHelpCommandParser();
            case "exit":
                return args -> new ExitCommand();
            default:
                throw new IllegalArgumentException("Invalid command");
        }
    }
}
