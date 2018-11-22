package com.sap.exercise.wrapper.fields;

import com.sap.exercise.wrapper.FieldInfo;
import com.sap.exercise.wrapper.FieldValueUtils;
import com.sap.exercise.model.Event;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class DescriptionFieldInfo implements FieldInfo {

    private Event event;

    public DescriptionFieldInfo(Event event) {
        this.event = event;
    }

    @Override
    public String getName() {
        return "description";
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
        event.setDescription(FieldValueUtils.valueOfStr(arg.trim()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DescriptionFieldInfo that = (DescriptionFieldInfo) o;
        return Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event);
    }
}
