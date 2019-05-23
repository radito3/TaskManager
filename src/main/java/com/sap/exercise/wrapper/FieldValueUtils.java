package com.sap.exercise.wrapper;

import java.util.Optional;

import com.sap.exercise.model.Event;

public class FieldValueUtils {

    private FieldValueUtils() {
    }

    public static Boolean valueOfBool(String input) {
        if (input.matches("(?i)^\\s*y|yes\\s*$")) {
            return true;
        }
        if (input.matches("(?i)^\\s*n|no\\s*$")) {
            return false;
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
        return Optional.ofNullable(input)
                .filter(string -> string.matches("[-_.a-zA-Z0-9 ]*"))
                .orElseThrow(() -> new IllegalArgumentException("Illegal characters in input"));
    }
}
