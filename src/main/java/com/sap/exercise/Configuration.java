package com.sap.exercise;

import java.io.*;

public class Configuration {

    public enum NotificationType {
        POPUP, EMAIL
    }

    public static InputStream INPUT;

    static {
        try {
            INPUT = new FileInputStream("src/main/resources/executeScript.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static OutputStream OUTPUT = System.out;
    public static NotificationType NOTIFICATION_TYPE = NotificationType.POPUP;
    public static String USER_EMAIL = System.getenv("user_email");
    public static String USER_PASS = System.getenv("user_pass");
    public static String RECIPIENT_EMAIL = "default-email";
}
