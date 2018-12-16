package com.sap.exercise.wrapper;

import java.util.Optional;

import com.sap.exercise.model.Event;

public class FieldValueUtils {

    public static Boolean valueOfBool(String input) {
        if (input.toLowerCase().matches("^\\s*y|yes\\s*$")) {
            return true;
        }
        if (input.toLowerCase().matches("^\\s*n|no\\s*$")) {
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

    // Dido:knowledge about the actual 'repeatable' values is in multiple places - in the RepeatableType enum and in the following method
    // Rangel: Has this fixed the issue?
    // Dido: not really - modifying the enum would still require adaptations in those regexes. Why not move the entire method boyd to the
    // enunm and not just delegate to it?
    public static Event.RepeatableType valueOfRepeatable(String input) {
        if (input.toLowerCase().matches("^\\s*none|daily|weekly|monthly|yearly\\s*$")) {
            return Event.RepeatableType.valueOf(input.trim().toUpperCase());
        }
        if (input.toLowerCase().matches("^\\s*n|d|w|m|y\\s*$")) {
            return Event.RepeatableType.getRepeatableFromAlias(input.trim().toLowerCase());
        }
        throw new IllegalArgumentException("Invalid input");
    }

    public static String valueOfStr(String input) {
        return Optional.ofNullable(input)
                .filter(string -> string.matches("[-_.a-zA-Z0-9 ]*"))
                .orElseThrow(() -> new IllegalArgumentException("Illegal characters in input"));
    }
}
