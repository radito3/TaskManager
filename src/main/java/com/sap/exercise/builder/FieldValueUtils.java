package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

public class FieldValueUtils {

    private FieldValueUtils() {
    }

    public static Boolean valueOfBool(String input) {
        if (input.matches("(?i)^\\s*y|yes\\s*$")) {
            return Boolean.TRUE;
        }
        if (input.matches("(?i)^\\s*n|no\\s*$")) {
            return Boolean.FALSE;
        }
        throw new IllegalArgumentException("Invalid input");
    }

    public static Integer valueOfInt(String input) {
        if (!input.matches("^\\s*\\d{1,3}\\s*$")) {
            throw new IllegalArgumentException("Invalid number");
        }
        return Integer.valueOf(input.trim());
    }

    public static Event.RepeatableType valueOfRepeatable(String input) {
        if (!input.matches("(?i)^\\s*" + Event.RepeatableType.getRegex() + "\\s*$")) {
            throw new IllegalArgumentException("Invalid input");
        }
        return Event.RepeatableType.getRepeatable(input.trim());
    }

    public static String valueOfStr(String input) {
        if (input == null || input.matches("[^-_.a-zA-Z0-9 ]+")) {
            throw new IllegalArgumentException("Illegal characters in input");
        }
        return input;
    }
}
