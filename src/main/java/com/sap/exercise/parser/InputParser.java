package com.sap.exercise.parser;

import com.sap.exercise.parser.commands.*;
import com.sap.exercise.printer.OutputPrinter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {

    //TODO create input arguments parser

    private static List<Command> commands = Arrays.asList(
            new Exit(), new Add(), new Edit(), new Delete());

    //there needs to be a OutputPrinter class variable

    public static void run(InputStream in) {
        try (Scanner scanner = new Scanner(in)) {
            outside:
            while (scanner.hasNext()) {
                String input = scanner.nextLine();
                List<String> args = inputArgs(input);

                for (Command command : commands) {
                    if (args.get(0).equals(command.getName())) { //may be .startsWith() but prefer not
                        command.execute();
                        continue outside;
                    }
                }

                new OutputPrinter().configure(System.out).print("Invalid command");
            }
        }
    }

    private static List<String> inputArgs(String input) {
        Pattern pattern = Pattern.compile("^(\\w+)\\s*$"); //doesn't work
        Matcher matcher = pattern.matcher(input);
        List<String> args = new ArrayList<>();
        if (matcher.matches()) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                args.add(matcher.group(i));
            }
        }
        args.forEach(System.out::println);
        return args;
    }
}
