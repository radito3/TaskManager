package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.ArrayList;
import java.util.List;

public class TaskBuilder extends AbstractEventBuilder implements EventBuilder {

    TaskBuilder(Event event) {
        super(event);
        fields = new ArrayList<>(8);
        //"title|location|description|reminder|when|repeat.+|all day.+|duration"
    }

    @Override
    public List<FieldInfo> getFields() {
        return fields;
    }

    @Override
    public EventBuilder append(String field, String val) {
        return null;
    }

    @Override
    public Event build() {
        return event;
    }
}
