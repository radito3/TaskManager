package com.sap.exercise;

import com.sap.exercise.parser.InputParser;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.OutputStream;

public class Application {

    public static void main(String[] args) {
        Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        Logger.getLogger("javax.mail").setLevel(Level.WARN);
        InputParser.run();
    }

    public static class Configuration {

        public enum NotificationType {
            POPUP, EMAIL
        }

        public static InputStream INPUT = System.in;
        public static OutputStream OUTPUT = System.out;
        public static NotificationType NOTIFICATION_TYPE = NotificationType.POPUP;
        public static String USER_EMAIL = "default-email";
    }
}
