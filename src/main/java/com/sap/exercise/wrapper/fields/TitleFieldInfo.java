package com.sap.exercise.wrapper.fields;

import com.sap.exercise.wrapper.FieldInfo;
import com.sap.exercise.wrapper.FieldValueUtils;
import com.sap.exercise.model.Event;
import org.apache.commons.lang3.StringUtils;

public class TitleFieldInfo implements FieldInfo {

    private Event event;

    public TitleFieldInfo(Event event) {
        this.event = event;
    }

    @Override
    public String getName() {
        return "title";
    }

    @Override
    public boolean isMandatory() {
        return true;
    }

    @Override
    public String getNameToDisplay() {
        return StringUtils.capitalize(this.getName());
    }

    @Override
    public void handleArg(String arg) {
        event.setTitle(FieldValueUtils.valueOfStr(arg.trim()));
    }
}
