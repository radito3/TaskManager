package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.List;
import java.util.Map;

public class ReminderBuilder extends AbstractBuilder implements EventBuilder {

    private Map<String, Class<?>> fieldParams;

    ReminderBuilder() {
        event = new Event("", Event.EventType.REMINDER);
        fields = getFields(name -> name.matches("title")); //...
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
