package com.sap.exercise.parser;

import com.sap.exercise.parser.commands.Command;
import com.sap.exercise.parser.commands.Exit;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputParser {

    //TODO create input arguments parser

    private static List<Command> commands = Arrays.asList(new Exit());

    public static void run(InputStream in) {
        try (Scanner scanner = new Scanner(in)) {
            while (scanner.hasNext()) {
                String input = scanner.nextLine();

                for (Command command : commands) {
                    if (command.getName().equals(input)) {
                        command.execute();
                    }
                }

                //wrong / non-existent command handler
            }
        }
    }
}
