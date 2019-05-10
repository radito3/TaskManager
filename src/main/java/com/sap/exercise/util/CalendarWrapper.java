package com.sap.exercise.util;

import java.util.Calendar;
import java.util.Objects;

public class CalendarWrapper {

    private final Calendar calendar;

    public CalendarWrapper(Calendar cal) {
        calendar = cal;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarWrapper that = (CalendarWrapper) o;
        return Objects.equals(calendar.get(0), that.calendar.get(0))
                && Objects.equals(calendar.get(1), that.calendar.get(1))
                && Objects.equals(calendar.get(6), that.calendar.get(6));
    }

    @Override
    public int hashCode() {
        return Objects.hash(calendar.get(0), calendar.get(1), calendar.get(6));
    }
}
