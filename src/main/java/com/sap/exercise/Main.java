package com.sap.exercise;

import com.sap.exercise.handler.CRUDOperations;
import com.sap.exercise.handler.DateHandler;
import com.sap.exercise.handler.EventHandler;
import com.sap.exercise.model.Event;
import com.sap.exercise.parser.InputParser;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static final InputStream INPUT = System.in;
    public static final OutputStream OUTPUT = System.out;

    public static void main(String[] args) {
        Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        EventHandler.onStartup();
//        fillDb();
        InputParser.run();
    }

    private static void fillDb() {
        for (int i = 0; i <= 31; i++) {
            CRUDOperations.create(new Event("title-" + i, Event.EventType.TASK,
                    new DateHandler(i + "-10-2018 12:00").asCalendar(), Event.RepeatableType.NONE));
        }
    }

    //TODO move database from localhost to online service

    //Implement User system - every user has their own calendar
    //Users may be able to share calendars/some parts of their calendars

}
