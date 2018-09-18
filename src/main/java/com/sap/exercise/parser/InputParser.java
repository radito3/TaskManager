package com.sap.exercise.parser;

import com.sap.exercise.parser.commands.*;
import com.sap.exercise.printer.OutputPrinter;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputParser {

    //TODO create input arguments parser

    private static List<Command> commands = Arrays.asList(
            new Exit(), new Add(), new Edit(), new Delete());

    private static OutputPrinter printer = new OutputPrinter().configure(System.out);

    public static void run(InputStream in) {
        try (Scanner scanner = new Scanner(in)) {
            outside:
            while (scanner.hasNext()) {
                String input = scanner.nextLine();
                List<String> args = inputArgs(input);

                for (Command command : commands) {
                    if (args.get(0).equals(command.getName())) {
                        command.execute();
                        continue outside;
                    }
                }

                printer.print("Invalid command");
            }
        } catch (Exception e) { //will specify the exception later
            printer.print(e.getMessage());
            run(in); //not the best solution
        }
    }

    private static List<String> inputArgs(String input) {
        return Arrays.asList(input.split("\\s+")); //doesn't work when a command is written after a series of \s characters (works with \r|\n)
    }
}
