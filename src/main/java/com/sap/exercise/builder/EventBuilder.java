package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface EventBuilder {

    List<String> getFields();

    String getOrigFieldName(String alias);

    EventBuilder append(String field, String val) throws InvocationTargetException, IllegalAccessException;

    Event build();
}
