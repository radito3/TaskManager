package com.sap.exercise.filters;

import com.sap.exercise.handler.DateHandler;

import java.util.Calendar;

public class CalendarFilter implements InputValueFilter<Calendar> {

    @Override
    public Calendar valueOf(String input) {
        DateHandler dateHandler = new DateHandler(input);
        return dateHandler.asCalendar();
    }

}
