package com.sap.exercise.builder;

import com.sap.exercise.model.Event;
import com.sap.exercise.builder.fields.*;

import java.util.Arrays;
import java.util.List;

public class TaskEventBuilder implements EventBuilder {

    private Event event;

    TaskEventBuilder() {
        this.event = new Event("", Event.EventType.TASK);
    }

    TaskEventBuilder(Event event) {
        this.event = event;
    }

    @Override
    public List<FieldInfo> getFields() {
        return Arrays.asList(new TitleFieldInfo(event), new TimeOfFieldInfo(event), new AllDayFieldInfo(event),
                new LocationFieldInfo(event), new DescriptionFieldInfo(event), new ToRepeatFieldInfo(event, false),
                new ReminderFieldInfo(event), new DurationFieldInfo(event, false));
    }

    @Override
    public Event build() {
        return event;
    }
}
