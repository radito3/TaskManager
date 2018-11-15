package com.sap.exercise.builder.fields;

import com.sap.exercise.builder.FieldInfo;
import com.sap.exercise.builder.FieldValueUtils;
import com.sap.exercise.model.Event;
import org.apache.commons.lang3.StringUtils;

public class ReminderFieldInfo implements FieldInfo {

    private Event event;

    public ReminderFieldInfo(Event event) {
        this.event = event;
    }

    @Override
    public String getName() {
        return "reminder";
    }

    @Override
    public boolean isMandatory() {
        return false;
    }

    @Override
    public String getNameToDisplay() {
        return StringUtils.capitalize(this.getName());
    }

    @Override
    public void handleArg(String arg) {
        event.setReminder(FieldValueUtils.valueOfInt(arg));
    }
}
