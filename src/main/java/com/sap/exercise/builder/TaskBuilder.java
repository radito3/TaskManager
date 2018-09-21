package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.List;
import java.util.Map;

public class TaskBuilder extends AbstractBuilder implements EventBuilder {

    private Event event;
    private Map<String, Class<?>> fieldParams;
    private List<String> fields;

    public TaskBuilder() {
        event = new Event("", Event.EventType.TASK);
        fields = getFields(name -> name.matches("title|location|description|reminder")); //...
    }


    public TaskBuilder append(String value) {
        //filter input data based on field it needs to fill
        return this;
    }

    public List<String> getFields() {
        return fields;
    }

    public Event build() {
        return event;
    }
}
