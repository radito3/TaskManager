package com.sap.exercise.builder;

import com.sap.exercise.builder.fields.*;
import com.sap.exercise.model.Event;

import java.util.Arrays;
import java.util.List;

public class EventBuilder {

    private Event event;

    public EventBuilder(Event event) {
        this.event = event;
    }

    public List<FieldInfo> getFields() {
        switch (event.getTypeOf())  {
            case TASK:
                return Arrays.asList(new TitleFieldInfo(event), new TimeOfFieldInfo(event), new AllDayFieldInfo(event),
                        new LocationFieldInfo(event), new DescriptionFieldInfo(event), new ToRepeatFieldInfo(event, false),
                        new ReminderFieldInfo(event), new DurationFieldInfo(event, false));
            case GOAL:
                return Arrays.asList(new TitleFieldInfo(event), new TimeOfFieldInfo(event), new ToRepeatFieldInfo(event, true),
                        new ReminderFieldInfo(event), new DurationFieldInfo(event, true));
            case REMINDER:
                return Arrays.asList(new TitleFieldInfo(event), new TimeOfFieldInfo(event), new AllDayFieldInfo(event),
                        new ToRepeatFieldInfo(event, false));
        }
        return null;
    }

    public Event getEvent() {
        return event;
    }

}
