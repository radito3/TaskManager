package com.sap.exercise.wrapper.fields;

import com.sap.exercise.wrapper.FieldInfo;
import com.sap.exercise.wrapper.FieldValueUtils;
import com.sap.exercise.model.Event;

import java.util.Objects;

public class AllDayFieldInfo implements FieldInfo {

    private Event event;

    public AllDayFieldInfo(Event event) {
        this.event = event;
    }

    @Override
    public String getName() {
        return "allDay";
    }

    @Override
    public boolean isMandatory() {
        return true;
    }

    @Override
    public String getNameToDisplay() {
        return "All day? [Y]es [N]o";
    }

    @Override
    public void handleArg(String arg) {
        event.setAllDay(FieldValueUtils.valueOfBool(arg));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllDayFieldInfo that = (AllDayFieldInfo) o;
        return Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event);
    }
}
