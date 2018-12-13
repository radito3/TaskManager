package com.sap.exercise.parser;

import com.sap.exercise.Application;
import com.sap.exercise.commands.*;
import com.sap.exercise.printer.OutputPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InputParser {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(Application.Configuration.INPUT));

    private final Map<String, Command> commands = Stream.of(
            new ExitCommand(), new AddCommand(), new EditCommand(), new Delete(), new PrintHelpCommand(),
            new PrintAgendaCommand(), new PrintCalendarCommand())
            .collect(Collectors.toMap(Command::getName, Function.identity()));

    private final OutputPrinter printer = new OutputPrinter(Application.Configuration.OUTPUT);

    public InputParser() {
    }

    public void run() {
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

    private void executeCommand(String[] userInput) {
        String command = userInput[0];
        if (!commands.containsKey(command)) {
            printer.println("Invalid command");
            return;
        }
        commands.get(command).execute(Arrays.copyOfRange(userInput, 1, userInput.length));
    }

    public BufferedReader getReader() {
        return reader;
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            printer.println(e.getMessage());
        }
    }
}
