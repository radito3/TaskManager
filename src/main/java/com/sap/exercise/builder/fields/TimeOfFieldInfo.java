package com.sap.exercise.builder.fields;

import com.sap.exercise.builder.FieldInfo;
import com.sap.exercise.handler.DateHandler;
import com.sap.exercise.model.Event;

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
    public void argHandler(String arg) {
        DateHandler dateHandler = new DateHandler(arg);
        event.setTimeOf(dateHandler.asCalendar());
    }
}
