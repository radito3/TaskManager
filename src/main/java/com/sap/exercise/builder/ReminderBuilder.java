package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.HashMap;
import java.util.List;

public class ReminderBuilder extends AbstractBuilder implements EventBuilder {

    ReminderBuilder() {
        event = new Event("", Event.EventType.REMINDER);
        fields = getFields(name -> name.matches("title|all day.+|when|repeat.+"));
        fieldParams = new HashMap<>();
    }

    public List<String> getFields() {
        return fields;
    }

    public EventBuilder append(String val) {
        //filter input
        return this;
    }

    public Event build() {
        return event;
    }
}
