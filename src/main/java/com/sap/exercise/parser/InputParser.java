package com.sap.exercise.parser;

import static com.sap.exercise.Main.OUTPUT;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import com.sap.exercise.parser.commands.Add;
import com.sap.exercise.parser.commands.Command;
import com.sap.exercise.parser.commands.Delete;
import com.sap.exercise.parser.commands.Edit;
import com.sap.exercise.parser.commands.Exit;
import com.sap.exercise.parser.commands.Help;
import com.sap.exercise.printer.OutputPrinter;

public class InputParser {

    //Scanner variable (will remake it to Buffered reader because it is thread safe) may be a private variable
    //and the two methods will open a new reader when they are called

    private static List<Command> commands = Arrays.asList(
            new Exit(), new Add(), new Edit(), new Delete(), new Help());

    private static OutputPrinter printer = new OutputPrinter(OUTPUT);

    public static void run(InputStream in) {
        try (Scanner scanner = new Scanner(in)) {
            outside: // Try not to use such labels, refactor & redesign to avoid this. See why there is no 'goto' operator
            while (scanner.hasNext()) {
                String input = scanner.nextLine();
                if (input.matches("\\s*|\\r|\\t|\\n")) continue;

                String[] inputArgs = input.split("\\s+");
                // just lost my attention after this level of indentation. Please extract some methods with meaningful names if not classes
                for (Command command : commands) {
                    if (inputArgs[0].equals(command.getName())) { // if (!<condition>){continue;} would reduce one level of indentation
                        command.execute(Stream.of(inputArgs)
                            .skip(1)
                            .toArray(String[]::new)); // event this is a bit too many instructions for a single line.
                        continue outside;
                    }
                }

                printer.println("Invalid command"); // The parser is doing printing? Interesting... Separation of concerns?
            }
        } catch (Exception e) {
            //this should not be reached
            printer.error("FATAL: " + e.getMessage());
            // If this should never happen, why not log, rethrow a runtime exception, fail miserably and hope to catch it in development -
            // before delivery?
        }
    }

}
