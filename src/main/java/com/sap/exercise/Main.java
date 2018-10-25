package com.sap.exercise;

import com.sap.exercise.handler.CRUDOperations;
import com.sap.exercise.model.Event;
import com.sap.exercise.parser.InputParser;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static final InputStream INPUT = System.in;
    public static final OutputStream OUTPUT = System.out;

    public static void main(String[] args) {
        Logger.getLogger("org.hibernate").setLevel(Level.OFF);
//        fillDb();
        InputParser.run();
    }

    private static void fillDb() {
        for (int i = 1; i <= 20; i++) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            String day = i < 10 ? "0" + i : "" + i;
            try { cal.setTime(sdf.parse(day + "-10-2018" +" 12:00:00")); } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date value");
            }
            CRUDOperations.create(new Event("title-" + i, Event.EventType.TASK, cal, Event.RepeatableType.NONE));
        }
    }

    //TODO move database from localhost to online service

    //Implement User system - every user has their own calendar
    //Users may be able to share calendars/some parts of their calendars
    //The app may run on multiple JVMs while connected to one database

}
