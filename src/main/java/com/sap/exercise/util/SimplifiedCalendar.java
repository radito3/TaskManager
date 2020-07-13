package com.sap.exercise.util;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Objects;

public class SimplifiedCalendar {

    private final Calendar delegate;

    public SimplifiedCalendar(Calendar cal) {
        delegate = cal;
    }

    public Calendar getDelegate() {
        return delegate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplifiedCalendar that = (SimplifiedCalendar) o;
        return DateUtils.isSameDay(this.delegate, that.delegate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(delegate.get(0), delegate.get(1), delegate.get(6));
    }

    @Override
    public String toString() {
        return String.format("%1$tY-%1$tm-%1$td", delegate);
    }
}
