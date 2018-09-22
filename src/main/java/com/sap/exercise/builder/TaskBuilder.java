package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.HashMap;
import java.util.List;

public class TaskBuilder extends AbstractBuilder implements EventBuilder {

    TaskBuilder() {
        event = new Event("", Event.EventType.TASK);
        fields = getFields(name ->
                name.matches("title|location|description|reminder|when|repeat|all day"));
        fieldParams = new HashMap<>();
    }


    //if param type is boolean -> need to map input (yes -> true & no -> false)
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
