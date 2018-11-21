package com.sap.exercise.wrapper.fields;

import com.sap.exercise.wrapper.FieldInfo;
import com.sap.exercise.wrapper.FieldValueUtils;
import com.sap.exercise.model.Event;
import org.apache.commons.lang3.StringUtils;

public class LocationFieldInfo implements FieldInfo {

    private Event event;

    public LocationFieldInfo(Event event) {
        this.event = event;
    }

    @Override
    public String getName() {
        return "location";
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
        event.setLocation(FieldValueUtils.valueOfStr(arg.trim()));
    }
}
