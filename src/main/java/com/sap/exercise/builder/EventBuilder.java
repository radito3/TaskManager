package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.Calendar;
import java.util.List;

public interface EventBuilder {

    List<FieldInfo> getFields();

    EventBuilder append(String field, String val);

    EventBuilder append(Calendar calendar);

    Event build();
}
