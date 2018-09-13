package com.sap.exercise;

import com.sap.exercise.parser.InputParser;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        InputParser.run(System.in);

//        Logger logger = Logger.getLogger(Main.class.getName());
//        logger.log(Level.FINE, "test log");
    }

}
