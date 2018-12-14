package com.sap.exercise.parser;

import com.sap.exercise.Application;
import com.sap.exercise.commands.*;
import com.sap.exercise.handler.EventHandler;
import com.sap.exercise.printer.OutputPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class InputParser {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(Application.Configuration.INPUT));

    private Map<String, Supplier<Command>> commands = new HashMap<>(7);

    {
        commands.put("add", AddCommand::new);
        commands.put("edit", EditCommand::new);
        commands.put("exit", ExitCommand::new);
        commands.put("delete", Delete::new);
        commands.put("help", PrintHelpCommand::new);
        commands.put("agenda", PrintAgendaCommand::new);
        commands.put("cal", PrintCalendarCommand::new);
    }

    private final OutputPrinter printer = new OutputPrinter(Application.Configuration.OUTPUT);

    public InputParser() {
    }

    public void run(EventHandler handler) {
        try {
            while (true) {
                String input = reader.readLine();
                if (input.matches("\\s*|\\r|\\t*|\\n")) {
                    continue;
                }
                String[] inputArgs = input.split("\\s+");

                executeCommand(handler, inputArgs);
            }
        } catch (IOException e) {
            printer.println(e.getMessage());
        } finally {
            close();
        }
    }

    private void executeCommand(EventHandler arg, String[] userInput) {
        String command = userInput[0];
        if (!commands.containsKey(command)) {
            printer.println("Invalid command");
            return;
        }
        commands.get(command).get()
                .execute(arg, Arrays.copyOfRange(userInput, 1, userInput.length));
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
