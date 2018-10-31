package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.Optional;

public class FieldValueUtils {

    public static Boolean valueOfBool(String input) {      
        if (input.toLowerCase().matches("^\\s*y|yes\\s*$")) {
            return true;
        } else if (input.toLowerCase().matches("^\\s*n|no\\s*$")) {
            return false;
        } else {
            throw new IllegalArgumentException("Invalid input");
        }
    }

    public static Integer valueOfInt(String input) {
        if (!input.matches("^\\s*\\d{1,3}\\s*$")) {
            throw new IllegalArgumentException("Invalid number");
        }
        return Integer.valueOf(input);
    }

    public static Event.RepeatableType valueOfRepeatable(String input) {
        String arg = "";
        if (input.toLowerCase().matches("^\\s*none|daily|weekly|monthly|yearly\\s*$")) {
            arg = input;
        } else if (input.toLowerCase().matches("^\\s*n|d|w|m|y\\s*$")) {
            switch (input.toLowerCase()) {
                case "d":
                    arg = "daily";
                    break;
                case "w":
                    arg = "weekly";
                    break;
                case "m":
                    arg = "monthly";
                    break;
                case "y":
                    arg = "yearly";
                    break;
                case "n":
                    arg = "none";
            }
        } else {
            throw new IllegalArgumentException("Invalid input");
        }
        return Event.RepeatableType.valueOf(arg.toUpperCase());
    }

    public static String valueOfStr(String input) {
        return Optional.ofNullable(input)
                .filter(string -> string.matches("[-_.a-zA-Z0-9 ]*"))
                .orElseThrow(() -> new IllegalArgumentException("Illegal characters in input"));
    }
}
