package com.sap.exercise.wrapper;

import com.sap.exercise.model.Event;

import java.util.List;

public interface EventWrapper {

    List<FieldInfo> getFields();

    Event getEvent();
}
