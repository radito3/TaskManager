package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.HashMap;
import java.util.List;

public class GoalBuilder extends AbstractBuilder implements EventBuilder {

    GoalBuilder() {
        event = new Event("", Event.EventType.GOAL);
        fields = getFields(name -> name.matches("title")); //...
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
