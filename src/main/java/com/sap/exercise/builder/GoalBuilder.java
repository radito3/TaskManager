package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.List;

public class GoalBuilder extends AbstractBuilder implements EventBuilder {

    GoalBuilder(Event event) {
        super(event);
        fields = getFields(name -> name.matches("title")); //...
    }


    public List<String> getFields() {
        return fields;
    }

    public EventBuilder append(String field, String val) {
        //filter input
        return this;
    }

    public Event build() {
        return event;
    }
}
