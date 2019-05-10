package com.sap.exercise.parser;

import com.sap.exercise.Application;
import com.sap.exercise.commands.CommandExecutor;
import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputParser {

    public static void run() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new CloseShieldInputStream(Application.Configuration.INPUT)))) {
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
                CommandExecutor executor = new CommandExecutor(userInputArgs);

                if (executor.executeCommand() != 0) {
                    return;
                }
            }
        } catch (IOException e) {
            Logger.getLogger(InputParser.class).error("Input reading error", e);
        }
    }
}
