package com.sap.exercise;

import com.sap.exercise.parser.InputParser;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    //system config
    public static final InputStream INPUT = System.in;
    public static final OutputStream OUTPUT = System.out;

    public static void main(String[] args) {
        InputParser.run(INPUT);

//        Logger logger = Logger.getLogger(Main.class.getName());
//        logger.log(Level.FINE, "test log");
    }

}
