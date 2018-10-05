package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

public class RepeatableFilter implements InputValueFilter<Event.RepeatableType> {

    @Override
    public Event.RepeatableType valueOf(String input) {
        return Event.RepeatableType.valueOf(filter(input).toUpperCase());
    }

    private String filter(String val) {
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
    }
}
