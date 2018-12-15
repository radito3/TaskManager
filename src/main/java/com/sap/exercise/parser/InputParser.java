package com.sap.exercise.parser;

import com.sap.exercise.Application;
import com.sap.exercise.commands.*;
import com.sap.exercise.handler.EventHandler;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class InputParser extends BufferedReader {

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

    public InputParser() {
        super(new InputStreamReader(Application.Configuration.INPUT));
    }

    public void run(EventHandler handler) {
        try {
            while (true) {
                String input = this.readLine();
                if (input.matches("\\s*|\\r|\\t*|\\n")) {
                    continue;
                }
                String[] inputArgs = input.split("\\s+");

                executeCommand(handler, inputArgs);
            }
        } catch (IOException e) {
            Logger.getLogger(InputParser.class).error("Input reading error", e);
        }
    }

    private void executeCommand(EventHandler arg, String[] userInput) {
        String command = userInput[0];
        if (!commands.containsKey(command)) {
            Command.onInvalidCommand();
            return;
        }
        commands.get(command).get()
                .execute(arg, Arrays.copyOfRange(userInput, 1, userInput.length));
    }
}
