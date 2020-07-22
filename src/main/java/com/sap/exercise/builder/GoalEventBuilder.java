package com.sap.exercise.builder;

import com.sap.exercise.model.Event;
import com.sap.exercise.builder.fields.*;

import java.util.Arrays;
import java.util.List;

public class GoalEventBuilder implements EventBuilder {

    private Event event;

    GoalEventBuilder() {
        this.event = new Event("", Event.EventType.GOAL);
    }

    GoalEventBuilder(Event event) {
        this.event = event;
    }

    @Override
    public List<FieldInfo> getFields() {
        return Arrays.asList(new TitleFieldInfo(event), new TimeOfFieldInfo(event), new ToRepeatFieldInfo(event, true),
                new ReminderFieldInfo(event), new DurationFieldInfo(event, true));
    }

    @Override
    public Event build() {
        return event;
    }
}
