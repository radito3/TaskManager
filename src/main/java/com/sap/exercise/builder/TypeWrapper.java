package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TypeWrapper {

    private String name;

    TypeWrapper(String name) {
        this.name = name;
    }

    String filter(String val) {
        switch (name) {
            case "calendar":
                if (val.matches("^\\s*\\d{2}-\\d{2}-2\\d{3}\\s*$")) {
                    return val + " 00:00:00";
                } else if (val.matches("^\\s*\\d{2}-\\d{2}-2\\d{3} \\d{2}:\\d{2}:\\d{2}\\s*$")) {
                    return val;
                } else {
                    throw new IllegalArgumentException("Invalid time format");
                }
            case "bool":
                if (val.matches("^\\s*[yY]|[yY]es\\s*$")) {
                    return "true";
                } else if (val.matches("^\\s*[nN]|[nN]o\\s*$")) {
                    return "false";
                } else {
                    throw new IllegalArgumentException("Invalid input");
                }
            case "repeat":
                if (val.matches("^\\s*[nN]one|[dD]aily|[wW]eekly|[mM]onthly|[yY]early\\s*$")) {
                    return val;
                } else if (val.matches("^\\s*[nN]|[dD]|[wW]|[mM]|[yY]\\s*$")) {
                    switch (val.toLowerCase()) {
                        case "d":
                            return "daily";
                        case "w":
                            return "weekly";
                        case "m":
                            return "monthly";
                        case "y":
                            return "yearly";
                        default:
                            return "none";
                    }
                } else {
                    throw new IllegalArgumentException("Invalid input");
                }
            default:
                return val;
        }
    }

    Object valueOf(String val) {
        switch (name) {
            case "calendar":
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                try { cal.setTime(sdf.parse(val)); } catch (ParseException ignored) {}
                return cal;
            case "bool":
                return Boolean.valueOf(val);
            case "integer":
                return Integer.valueOf(val);
            case "repeat":
                return Event.RepeatableType.valueOf(val.toUpperCase());
            default:
                return AbstractBuilder.filterInput(val, string -> string.matches("[-_.a-zA-Z0-9 ]+"),
                        () -> new IllegalArgumentException("Illegal characters in input"));
        }
    }

}
