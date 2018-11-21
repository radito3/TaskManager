package com.sap.exercise.wrapper;

import com.sap.exercise.wrapper.fields.*;
import com.sap.exercise.model.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventWrapper {

    private Event event;

    public EventWrapper(Event event) {
        this.event = event;
    }

    public List<FieldInfo> getFields() {
        switch (event.getTypeOf()) {
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
        return new ArrayList<>();
    }

    public Event getEvent() {
        return event;
    }

}
