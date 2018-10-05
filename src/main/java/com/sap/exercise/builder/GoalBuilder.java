package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.ArrayList;
import java.util.List;

public class GoalBuilder extends AbstractEventBuilder implements EventBuilder {

    GoalBuilder(Event event) {
        super(event);
        fields = new ArrayList<>(5);
        //"title|when|duration|reminder|repeat.+"
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