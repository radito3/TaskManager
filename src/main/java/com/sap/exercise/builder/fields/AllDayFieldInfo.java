package com.sap.exercise.builder.fields;

import com.sap.exercise.builder.FieldInfo;
import com.sap.exercise.builder.FieldValueUtils;
import com.sap.exercise.model.Event;

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
}
