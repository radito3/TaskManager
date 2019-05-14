package com.sap.exercise.parser;

import com.sap.exercise.Application;
import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.parser.CommandParser;
import com.sap.exercise.commands.parser.CommandParserFactory;
import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class InputParser {

    public static void run() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new CloseShieldInputStream(Application.Configuration.INPUT)))) {
            Command command = () -> 0;
            do {
                String userInput = reader.readLine();
                if (userInput.trim().isEmpty()) {
                    continue;
                }
                String[] userInputArgs = userInput.split("\\s+");
                CommandParser parser = CommandParserFactory.getParser(userInputArgs[0]);
                command = parser.parse(Arrays.copyOfRange(userInputArgs, 1, userInputArgs.length));

            } while (command.execute() == 0);
        } catch (IOException e) {
            Logger.getLogger(InputParser.class).error("Input reading error", e);
        }
    }
}
