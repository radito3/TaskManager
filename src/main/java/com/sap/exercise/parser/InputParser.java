package com.sap.exercise.parser;

import com.sap.exercise.commands.*;
import com.sap.exercise.printer.OutputPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static com.sap.exercise.Application.Configuration.INPUT;
import static com.sap.exercise.Application.Configuration.OUTPUT;

public class InputParser {

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(INPUT));

    private static List<Command> commands = Arrays.asList(
            new Exit(), new Add(), new Edit(), new Delete(), new Help(), new Agenda(), new Calendar());

    private static OutputPrinter printer = new OutputPrinter(OUTPUT);

    public static void run() {
        try {
            while (true) {
                String input = reader.readLine();
                if (input.matches("\\s*|\\r|\\t*|\\n")) continue;

                String[] inputArgs = input.split("\\s+");

                iterateCommands(inputArgs);
            }
        } catch (IOException e) {
            printer.println(e.getMessage());
        } finally {
            close();
        }
    }

    private static void iterateCommands(String[] arguments) {
        for (Command command : commands) {
            if (arguments[0].equals(command.getName())) {
                command.execute(
                        Arrays.stream(arguments)
                        .skip(1)
                        .toArray(String[]::new));
                return;
            }
        }
        printer.println("Invalid command");
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
