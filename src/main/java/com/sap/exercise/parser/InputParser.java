package com.sap.exercise.parser;

import com.sap.exercise.parser.commands.*;
import com.sap.exercise.printer.OutputPrinter;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static com.sap.exercise.Main.OUTPUT;

public class InputParser {

    //Scanner variable (will remake it to Buffered reader because it is thread safe) may be a private variable
    //and the two methods will open a new reader when they are called

    private static List<Command> commands = Arrays.asList(
            new Exit(), new Add(), new Edit(), new Delete(), new Help());

    private static OutputPrinter printer = new OutputPrinter(OUTPUT);

    public static void run(InputStream in) {
        try (Scanner scanner = new Scanner(in)) {
            outside:
            while (scanner.hasNext()) {
                String input = scanner.nextLine();
                if (input.matches("\\s*|\\r|\\t|\\n")) continue;

                String[] inputArgs = input.split("\\s+");

                for (Command command : commands) {
                    if (inputArgs[0].equals(command.getName())) {
                        command.execute(Stream.of(inputArgs).skip(1).toArray(String[]::new));
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
