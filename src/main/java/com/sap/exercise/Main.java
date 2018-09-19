package com.sap.exercise;

import com.sap.exercise.parser.InputParser;

import java.io.InputStream;
import java.io.OutputStream;
//import java.util.logging.Level;
//import java.util.logging.Logger;

public class Main {

    //system config
    public static final InputStream INPUT = System.in;
    public static final OutputStream OUTPUT = System.out;

    public static void main(String[] args) {
        InputParser.run(INPUT);

//        Logger logger = Logger.getLogger(Main.class.getName());
//        logger.log(Level.FINE, "test log");
    }

    //TODO:
    //System config may be another file or a feature that the users can modify
    //Implement Logging system
    //Implement User system - every user has their own calendar
    //Users may be able to share calendars/some parts of their calendars
    //The app may run on multiple JVMs while connected to one database

}
