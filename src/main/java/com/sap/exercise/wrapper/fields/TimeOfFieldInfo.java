package com.sap.exercise.wrapper.fields;

import com.sap.exercise.wrapper.FieldInfo;
import com.sap.exercise.handler.DateHandler;
import com.sap.exercise.model.Event;

import java.util.Objects;

public class TimeOfFieldInfo implements FieldInfo {

    private Event event;

    public TimeOfFieldInfo(Event event) {
        this.event = event;
    }

    @Override
    public String getName() {
        return "timeOf";
    }

    @Override
    public boolean isMandatory() {
        return false;
    }

    @Override
    public String getNameToDisplay() {
        return "When";
    }

    @Override
    public void handleArg(String arg) {
        String hours = arg.contains(":") ? "" : " 12:00";
        DateHandler dateHandler = new DateHandler(arg.trim() + hours);
        event.setTimeOf(dateHandler.asCalendar());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeOfFieldInfo that = (TimeOfFieldInfo) o;
        return Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event);
    }
}
