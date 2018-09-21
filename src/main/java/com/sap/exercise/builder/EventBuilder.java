package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.List;

public interface EventBuilder {

    List<String> getFields();

    EventBuilder append(String val);

    Event build();
}
