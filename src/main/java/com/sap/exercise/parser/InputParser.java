package com.sap.exercise.parser;

import com.sap.exercise.Application;
import com.sap.exercise.commands.*;
import com.sap.exercise.handler.SharedResourcesFactory;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class InputParser {

    private Map<String, Supplier<Command>> commands = new HashMap<>(7);
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(Application.Configuration.INPUT));

    public InputParser() {
        commands.put("add", () -> new AddCommand(reader));
        commands.put("edit", () -> new EditCommand(reader));
        commands.put("exit", ExitCommand::new);
        commands.put("delete", Delete::new);
        commands.put("help", PrintHelpCommand::new);
        commands.put("agenda", PrintAgendaCommand::new);
        commands.put("cal", PrintCalendarCommand::new);
    }

    public void run() {
        try {
            while (true) {
                String input = reader.readLine();
                if (input.trim().isEmpty()) {
                    continue;
                }
                String[] inputArgs = input.split("\\s+");

                if (executeCommand(inputArgs) != 0) {
                    break;
                }
            }
        } catch (IOException e) {
            Logger.getLogger(InputParser.class).error("Input reading error", e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                Logger.getLogger(InputParser.class).error("Stream closing error", e);
            }
            SharedResourcesFactory.shutdown();
        }
    }

    private int executeCommand(String[] userInput) {
        String command = userInput[0];
        if (!commands.containsKey(command)) {
            Command.printer.println("Invalid command");
            return 0;
        }
        return commands.get(command).get()
                .execute(Arrays.copyOfRange(userInput, 1, userInput.length));
    }
}
