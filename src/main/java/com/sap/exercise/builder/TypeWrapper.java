package com.sap.exercise.builder;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;

import com.sap.exercise.model.Event;

public class TypeWrapper {

    private String name;

    TypeWrapper(String name) {
        this.name = name;
    }

    // Why not make such a class for each 'name' (or value type as these seem to be)?
    // From the naming alone, I can't really think what a TypeWrapper is supposed to filter and from what.
    // please extract some methods with meaningful names
    String filter(String val) {
        switch (name) {
            case "calendar":
                if (val.matches("2\\d\\d.+")) {
                    return val;
                } else if (val.matches("^\\s*[0-3]\\d-[01]\\d-2\\d{3}\\s*$")) {
                    return val + " 12:00:00";
                } else if (val.matches("^\\s*[0-3]\\d-[01]\\d-2\\d{3} [0-5]\\d:[0-5]\\d:[0-5]\\d\\s*$")) {
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
            case "integer":
                if (!val.matches("^\\s*\\d{1,3}\\s*$")) {
                    throw new IllegalArgumentException("Invalid number");
                }
                return val;
            default:
                return val;
        }
    }

    Object valueOf(String val) {
        switch (name) {
            case "calendar":
                Calendar cal = Calendar.getInstance();
                if (val.matches("2\\d\\d.+")) {
                    cal.setTime(Date.from(Instant.parse(val)));
                } else {
                    //may add more date formats in future implementation
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                    try { cal.setTime(sdf.parse(val)); } catch (ParseException e) {
                        throw new IllegalArgumentException("Invalid date value");
                    }
                }
                return cal;
            case "bool":
                return Boolean.valueOf(val);
            case "integer":
                return Integer.valueOf(val);
            case "repeat":
                return Event.RepeatableType.valueOf(val.toUpperCase());
            default:
                return AbstractBuilder.filterInput(val, string -> string.matches("[-_.a-zA-Z0-9 ]*"),
                        () -> new IllegalArgumentException("Illegal characters in input"));
        }
    }

}
