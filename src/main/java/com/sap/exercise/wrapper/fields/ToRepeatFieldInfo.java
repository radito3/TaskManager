package com.sap.exercise.wrapper.fields;

import com.sap.exercise.wrapper.FieldInfo;
import com.sap.exercise.wrapper.FieldValueUtils;
import com.sap.exercise.model.Event;

import java.util.Objects;

public class ToRepeatFieldInfo implements FieldInfo {

    private Event event;
    private boolean isGoal;

    public ToRepeatFieldInfo(Event event, boolean isGoal) {
        this.event = event;
        this.isGoal = isGoal;
    }

    @Override
    public String getName() {
        return "toRepeat";
    }

    @Override
    public boolean isMandatory() {
        return true;
    }

    @Override
    public String getNameToDisplay() {
        return (isGoal ? "How often? " : "Repeat? [N]o ") + "[D]aily [W]eekly [M]onthly [Y]early";
    }

    @Override
    public void handleArg(String arg) {
        if (isGoal && arg.toLowerCase().matches("^\\s*n|none\\s*$")) {
            throw new IllegalArgumentException("Invalid input");
        }
        event.setToRepeat(FieldValueUtils.valueOfRepeatable(arg));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToRepeatFieldInfo that = (ToRepeatFieldInfo) o;
        return isGoal == that.isGoal &&
                Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, isGoal);
    }
}
