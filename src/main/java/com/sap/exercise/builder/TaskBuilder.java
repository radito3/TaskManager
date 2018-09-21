package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.List;

public class TaskBuilder extends AbstractBuilder implements EventBuilder {

    private Event event;
    private Class<?>[] params;
    private List<String> fields;

    public TaskBuilder() {
        event = new Event("", Event.EventType.TASK);
        fields = getFields(name -> name.matches("title|location|description")); //...
    }



    public List<String> getFields() {
        return fields;
    }

    public Event build() {
        return event;
    }
}
