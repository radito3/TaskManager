package com.sap.exercise.builder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarFilter implements InputValueFilter<Calendar> {

    @Override
    public Calendar valueOf(String input) {
        Calendar cal = Calendar.getInstance();
        //may add more date formats in future implementation
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        try { cal.setTime(sdf.parse(filter(input))); } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date value");
        }
        return cal;
    }

    private String filter(String val) {
        if (val.matches("^\\s*[0-3]\\d-[01]\\d-2\\d{3}\\s*$")) {
            return val + " 12:00:00";
        } else if (val.matches("^\\s*[0-3]\\d-[01]\\d-2\\d{3} [0-5]\\d:[0-5]\\d:[0-5]\\d\\s*$")) {
            return val;
        } else {
            throw new IllegalArgumentException("Invalid time format");
        }
    }
}
