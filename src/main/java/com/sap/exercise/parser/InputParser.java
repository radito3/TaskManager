package com.sap.exercise.parser;

import com.sap.exercise.parser.commands.*;
import com.sap.exercise.printer.OutputPrinter;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static com.sap.exercise.Main.OUTPUT;

public class InputParser {

    private static List<Command> commands = Arrays.asList(
            new Exit(), new Add(), new Edit(), new Delete(), new Help());

    private static OutputPrinter printer = new OutputPrinter(OUTPUT);

    public static void run(InputStream in) {
        try (Scanner scanner = new Scanner(in)) {
            outside:
            while (scanner.hasNext()) {
                //doesn't work when a command is written after a series of \s characters (works with \r|\n)
                String[] inputArgs = scanner.nextLine().split("\\s+");

                for (Command command : commands) {
                    if (inputArgs[0].equals(command.getName())) {
                        command.execute(inputArgs);
                        continue outside;
                    }
                }

                printer.println("Invalid command");
            }
        } catch (Exception e) {
            //this should not be reached
            printer.error("FATAL: " + e.getMessage());
        }
    }

}
