package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.List;

public abstract class AbstractEventBuilder {

    protected Event event;
    protected List<FieldInfo> fields;

    AbstractEventBuilder(Event event) {
        this.event = event;
    }

    public static EventBuilder getEventBuilder(Event event) {
        switch (event.getTypeOf()) {
            case REMINDER:
                return new ReminderBuilder(event);
            case GOAL:
                return new GoalBuilder(event);
            default:
                return new TaskBuilder(event);
        }
    }

    protected FieldInfo findField(String val) {
        for (FieldInfo field : fields) {
            if (field.getName().equals(val))
                return field;
        }
        return fields.get(0);
    }

}
