package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.helper.*;

import java.util.HashMap;
import java.util.Map;

public class CommandParserFactory {

    private Map<String, CommandParser> commandsMap = new HashMap<>(7);

    public CommandParserFactory() {
        commandsMap.put("add", new AddCommandParser(AddCommandHelper::new));
        commandsMap.put("edit", new EditCommandParser(new EditCommandHelper()));
        commandsMap.put("delete", new DeleteCommandParser(DeleteCommandHelper::new));
        commandsMap.put("cal", new PrintCalendarCommandParser(PrintCalendarCommandHelper::new));
        commandsMap.put("agenda", new PrintAgendaCommandParser(PrintAgendaCommandHelper::new));
        commandsMap.put("help", new PrintHelpCommandParser());
        commandsMap.put("exit", new ExitCommandParser());
    }

    public CommandParser getParser(String command) {
        if (!commandsMap.containsKey(command)) {
            throw new IllegalArgumentException("Invalid command");
        }
        return commandsMap.get(command);
    }
}
