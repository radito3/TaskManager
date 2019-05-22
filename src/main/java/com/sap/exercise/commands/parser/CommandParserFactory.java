package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.CommandExecutionResult;
import com.sap.exercise.commands.EditEventCommand;
import com.sap.exercise.commands.ExitCommand;
import com.sap.exercise.commands.PrintHelpCommand;
import com.sap.exercise.util.ExceptionMessageHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandParserFactory {

    private static Map<String, CommandParser> commandsMap = new HashMap<>();

    static {
        commandsMap.put("add", new AddCommandParser());
        commandsMap.put("edit", args -> new EditEventCommand(buildEventName(args)));
        commandsMap.put("delete", new DeleteCommandParser(CommandParserFactory::buildEventName));
        commandsMap.put("cal", new PrintCalendarCommandParser());
        commandsMap.put("agenda", new PrintAgendaCommandParser());
        commandsMap.put("help", args -> new PrintHelpCommand());
        commandsMap.put("exit", args -> new ExitCommand());
    }

    public static CommandParser getParser(String command) {
        if (!commandsMap.containsKey(command)) {
            ExceptionMessageHandler.setMessage("Invalid command");
            return args -> () -> CommandExecutionResult.ERROR;
        }

        return commandsMap.get(command);
    }

    private static String buildEventName(String[] input) {
        return Optional.ofNullable(input)
                .flatMap(arr -> Optional.of(String.join(" ", arr)))
                .orElseThrow(() -> new IllegalArgumentException("Event name not specified"));
    }
}
