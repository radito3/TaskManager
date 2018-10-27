package com.sap.exercise.filters;

import com.sap.exercise.handler.DateHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarFilter implements InputValueFilter<Calendar> {

    @Override
    public Calendar valueOf(String input) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            cal.setTime(sdf.parse(filter(input)));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date value");
        }
        return cal;
    }

    private String filter(String val) {
        if (val.matches("^\\s*(\\d{1,2})-\\1-2\\d{3}\\s*$")) { //tested and working!
            return val + " 12:00:00";
        } else if (val.matches("^\\s*[0-3]\\d-[01]\\d-2\\d{3} [0-5]\\d:[0-5]\\d:[0-5]\\d\\s*$")) {
            return val;
        } else {
            throw new IllegalArgumentException("Invalid time format");
        }
    }

    private String filter1(String val) {
        DateHandler handler = new DateHandler(val);
        if (val.matches("^\\s*" + handler.getPattern() + "\\s*$")) {
            return handler.asString();
        }
        throw new IllegalArgumentException("Invalid date value");
    }
}
