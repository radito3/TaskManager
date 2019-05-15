package com.sap.exercise;

import java.io.*;

public class Configuration {

    public enum NotificationType {
        POPUP, EMAIL
    }

    public static InputStream INPUT;

    static {
        try {
            INPUT = new FileInputStream(new File("src/main/resources/executeScript.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static OutputStream OUTPUT = System.out;
    public static NotificationType NOTIFICATION_TYPE = NotificationType.POPUP;
    public static String USER_EMAIL = "default-email";
}
