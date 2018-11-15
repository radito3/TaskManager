package com.sap.exercise.builder.fields;

import com.sap.exercise.builder.FieldInfo;
import com.sap.exercise.builder.FieldValueUtils;
import com.sap.exercise.model.Event;
import org.apache.commons.lang3.StringUtils;

public class DurationFieldInfo implements FieldInfo {

    private Event event;
    private boolean isGoal;

    public DurationFieldInfo(Event event, boolean isGoal) {
        this.event = event;
        this.isGoal = isGoal;
    }

    @Override
    public String getName() {
        return "duration";
    }

    @Override
    public boolean isMandatory() {
        return false;
    }

    @Override
    public String getNameToDisplay() {
        return StringUtils.capitalize(this.getName()) + (isGoal ? " per day" : "");
    }

    @Override
    public void handleArg(String arg) {
        event.setDuration(FieldValueUtils.valueOfInt(arg));
    }
}
