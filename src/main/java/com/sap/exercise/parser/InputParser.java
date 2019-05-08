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

//Do you think this class could be improved to comply with the single responsibility principle?
//It does several things (retrieve input -> parse input to a command -> execute the command).
//Perhaps you could implement some structure like Reader - Parser - Executor
//where the reader is responsible only for console input, the parser knows all available Command(s) and parses the input, the executor executes it.
//This would make it more readable and maintainable, it would also allow easier extension of the available functionalities.
//The current name InputParser is a bit misleading.
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
        // Think of using try-with-resources, to avoid the 'messy' finally block
        try {
            // Do you think there's a better way of writing this? Seeing such loops ( while
            // true ) do not make it easy for the reader of the code to understand what is
            // happening
            // Makes it hard to maintain the code, and the way you are escaping the loop (by
            // avoiding the conditional) is very 'goto'like having to break somewhere in the block code. Think
            // of some ways to improve this block
            while (true) {
                String userInput = reader.readLine();
                if (userInput.trim().isEmpty()) {
                    continue;
                }
                String[] userInputArgs = userInput.split("\\s+");

                if (executeCommand(userInputArgs) != 0) {
                    break;
                }
            }
        } catch (IOException e) {
            Logger.getLogger(InputParser.class).error("Input reading error", e);
        } finally {
            // use try-with-resources to avoid this try-catch inside the finally
            try {
                reader.close();
            } catch (IOException e) {
                Logger.getLogger(InputParser.class).error("Stream closing error", e);
            }
            SharedResourcesFactory.shutdown(); // This does not sound very intuitive. First read makes me think you
                                               // shutdown the factory itself. I have to go inside to understand what
                                               // it does, I'd suggest some renaming
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