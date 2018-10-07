package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.List;

public interface EventBuilder {

    List<FieldInfo> getFields();

    EventBuilder append(String field, String val);

    EventBuilder append(InputArgs.Title title);

    EventBuilder append(InputArgs.When when);

    EventBuilder append(InputArgs.AllDay allDay);

    EventBuilder append(InputArgs.Location location);

    EventBuilder append(InputArgs.Description description);

    EventBuilder append(InputArgs.Reminder reminder);

    EventBuilder append(InputArgs.Repeat repeat);

    EventBuilder append(InputArgs.Duration duration);

    Event build();
}
