package com.sap.exercise.builder.fields;

import com.sap.exercise.builder.FieldInfo;
import com.sap.exercise.builder.FieldValueUtils;
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
    public void argHandler(String arg) {
        event.setTitle(FieldValueUtils.valueOfStr(arg));
    }
}
