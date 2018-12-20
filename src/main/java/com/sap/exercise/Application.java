package com.sap.exercise;

import com.sap.exercise.handler.EventHandler;
import com.sap.exercise.parser.InputParser;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.OutputStream;

public class Application {

    private static InputParser parser;

    public static void main(String[] args) {
        Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        Logger.getLogger("javax.mail").setLevel(Level.WARN);
        EventHandler handler = new EventHandler();
        parser = new InputParser();
        parser.run(handler);
        System.exit(0);
    }

    public static InputParser getParser() {
        return parser;
    }

    public static class Configuration {
        public enum NotificationType {
            POPUP, EMAIL
        }

        public static final InputStream INPUT = System.in;
        public static final OutputStream OUTPUT = System.out;
        public static NotificationType NOTIFICATION_TYPE = NotificationType.POPUP;
        public static String USER_EMAIL = "default-email";

    }

}
