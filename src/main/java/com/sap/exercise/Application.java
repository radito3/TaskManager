package com.sap.exercise;

import com.sap.exercise.parser.InputParser;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.OutputStream;

public class Application {

    //TODO db package needs refactoring
    //TODO NEED MORE ISOLATED MODULES - NEED TO DECOUPLE CURRENT ONES

    public static void main(String[] args) {
        Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        Logger.getLogger("javax.mail").setLevel(Level.WARN);
        new InputParser().run();
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
