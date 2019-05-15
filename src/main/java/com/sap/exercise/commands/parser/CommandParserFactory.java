package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.CommandUtils;
import com.sap.exercise.commands.EditCommand;
import com.sap.exercise.commands.ExitCommand;
import com.sap.exercise.printer.OutputPrinterProvider;

import java.io.Serializable;

public class CommandParserFactory {

    public static CommandParser getParser(String command) {
        switch (command) {
            case "add":
                return new AddCommandParser();
            case "edit":
                return args -> new EditCommand(CommandUtils.buildEventName(args));
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
            case "":
                return args -> (Serializable & Command) () -> 0;
            default:
                OutputPrinterProvider.getPrinter().println("Invalid command");
                return args -> (Serializable & Command) () -> 0;
        }
    }
}
