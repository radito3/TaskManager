package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.List;

public class TaskBuilder extends AbstractBuilder implements EventBuilder {

    TaskBuilder(Event event) {
        super(event);
        fields = getFields(name ->
                name.matches("title|location|description|reminder|when|repeat.+|all day.+"));
    }


    public List<String> getFields() {
        return fields;
    }

    //if param type is boolean -> need to map input (yes -> true & no -> false)
    public TaskBuilder append(String field, String value) {
        //filter input data based on field it needs to fill

        return this;
    }

    public Event build() {
        return event;
    }
}
