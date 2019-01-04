package com.sap.exercise.parser;

import com.sap.exercise.Application;
import com.sap.exercise.commands.*;
import com.sap.exercise.handler.EventsMapHandler;
import com.sap.exercise.handler.ThreadPoolHandler;
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
    private ThreadPoolHandler thPool = new ThreadPoolHandler();
    private EventsMapHandler mapHandler = new EventsMapHandler();

    {
        commands.put("add", () -> new AddCommand(this, thPool, mapHandler));
        commands.put("edit", () -> new EditCommand(this, thPool, mapHandler));
        commands.put("exit", ExitCommand::new);
        commands.put("delete", () -> new Delete(thPool, mapHandler));
        commands.put("help", PrintHelpCommand::new);
        commands.put("agenda", () -> new PrintAgendaCommand(thPool, mapHandler));
        commands.put("cal", () -> new PrintCalendarCommand(thPool, mapHandler));
    }

    public InputParser() {
        super(new InputStreamReader(Application.Configuration.INPUT));
    }

    public void run() {
        try {
            while (true) {
                String input = this.readLine();
                if (input.matches("\\s*|\\r|\\t*|\\n")) {
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
                close();
            } catch (IOException e) {
                Logger.getLogger(InputParser.class).error("Stream closing error", e);
            }
            thPool.close();
            mapHandler.close();
        }
    }

    private int executeCommand(String[] userInput) {
        String command = userInput[0];
        if (!commands.containsKey(command)) {
            Command.onInvalidCommand();
            return 0;
        }
        return commands.get(command).get()
                .execute(Arrays.copyOfRange(userInput, 1, userInput.length));
    }
}
