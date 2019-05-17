package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.CommandUtils;
import com.sap.exercise.commands.EditEventCommand;
import com.sap.exercise.commands.ExitCommand;

import java.util.HashMap;
import java.util.Map;

public class CommandParserFactory {

    private static Map<String, CommandParser> commandsMap = new HashMap<>();

    static {
        commandsMap.put("add", new AddCommandParser());
        commandsMap.put("edit", args -> new EditEventCommand(CommandUtils.buildEventName(args)));
        commandsMap.put("delete", new DeleteCommandParser());
        commandsMap.put("cal", new PrintCalendarCommandParser());
        commandsMap.put("agenda", new PrintAgendaCommandParser());
        commandsMap.put("help", new PrintHelpCommandParser());
        commandsMap.put("exit", args -> new ExitCommand());
    }

    public static CommandParser getParser(String command) {
        if (!commandsMap.containsKey(command))
            throw new IllegalArgumentException("Invalid command");

        return commandsMap.get(command);
    }
}
