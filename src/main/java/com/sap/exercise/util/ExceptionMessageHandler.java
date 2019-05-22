package com.sap.exercise.util;

import java.util.Optional;

public class ExceptionMessageHandler {

    private static String message;

    public static void setMessage(String message1) {
        message = message1;
    }

    public static String getMessage() {
        return Optional.ofNullable(message).orElse("");
    }
}
