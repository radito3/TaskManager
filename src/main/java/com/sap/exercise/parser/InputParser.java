package com.sap.exercise.parser;

import java.io.InputStream;
import java.util.Scanner;

public class InputParser {

    //TODO create input arguments parser

    private static final String EXIT = "exit"; //these command constants could be an enum

    public static void run(InputStream in) {
        try (Scanner scanner = new Scanner(in)) {
            while (scanner.hasNext()) {
                String input = scanner.nextLine();


                if (input.equals(EXIT)) {
                    System.exit(0);
                }
            }
        }
    }
}
