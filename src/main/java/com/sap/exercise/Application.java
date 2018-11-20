package com.sap.exercise;

import com.sap.exercise.parser.InputParser;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {

    public static void main(String[] args) {
        Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        Logger.getLogger("javax.mail").setLevel(Level.FINE);
        InputParser.run();
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
