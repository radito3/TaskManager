package com.sap.exercise.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.sap.exercise.Application;
import com.sap.exercise.commands.AddCommand;
import com.sap.exercise.commands.PrintAgendaCommand;
import com.sap.exercise.commands.PrintCalendarCommand;
import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.Delete;
import com.sap.exercise.commands.EditCommand;
import com.sap.exercise.commands.ExitCommand;
import com.sap.exercise.commands.PrintHelpCommand;
import com.sap.exercise.printer.OutputPrinter;

public class InputParser {


    private static BufferedReader reader = new BufferedReader(new InputStreamReader(Application.Configuration.INPUT));

    // Dido: using a map is just a bit more efficient, and makes the code a bit cleaner (see below)
    private static Map<String, Command> commands;
    static {
        commands = new HashMap<>();
        registerCommand(new ExitCommand());
        registerCommand(new AddCommand());
        registerCommand(new EditCommand());
        registerCommand(new Delete());
        registerCommand(new PrintHelpCommand());
        registerCommand(new PrintAgendaCommand());
        registerCommand(new PrintCalendarCommand());
    }

    private static void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    private static OutputPrinter printer = new OutputPrinter(Application.Configuration.OUTPUT);

    public static void run() {
        try {
            while (true) {
                String input = reader.readLine();
                if (input.matches("\\s*|\\r|\\t*|\\n")) {
                    continue;
                }
                String[] inputArgs = input.split("\\s+");

                executeCommand(inputArgs);
            }
        } catch (IOException e) {
            printer.println(e.getMessage());
        } finally {
            close();
        }
    }

    private static void executeCommand(String[] userInput) {
        String command = userInput[0];
        if (!commands.containsKey(command)) {
            printer.println("Invalid command");
            return;
        }
        String[] arguments = Arrays.stream(userInput)
            .skip(1)
            .toArray(String[]::new);
        commands.get(command).execute(arguments);
    }

    public static BufferedReader getReader() {
        return reader;
    }

    public static void close() {
        try {
            reader.close();
        } catch (IOException e) {
            printer.println(e.getMessage());
        }
    }
}
