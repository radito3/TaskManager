package com.sap.exercise.filters;

import com.sap.exercise.model.Event;

public class RepeatableFilter implements InputValueFilter<Event.RepeatableType> {

    @Override
    public Event.RepeatableType valueOf(String input) {
        return Event.RepeatableType.valueOf(filter(input).toUpperCase());
    }

    private String filter(String val) {
        if (val.toLowerCase().matches("^\\s*none|daily|weekly|monthly|yearly\\s*$")) {
            return val;
        } else if (val.toLowerCase().matches("^\\s*n|d|w|m|y\\s*$")) {
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
