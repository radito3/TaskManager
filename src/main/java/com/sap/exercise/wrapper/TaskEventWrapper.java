package com.sap.exercise.wrapper;

import com.sap.exercise.model.Event;
import com.sap.exercise.wrapper.fields.*;

import java.util.Arrays;
import java.util.List;

public class TaskEventWrapper implements EventWrapper {

    private Event event;

    TaskEventWrapper(Event event) {
        this.event = event;
    }

    @Override
    public List<FieldInfo> getFields() {
        return Arrays.asList(new TitleFieldInfo(event), new TimeOfFieldInfo(event), new AllDayFieldInfo(event),
                new LocationFieldInfo(event), new DescriptionFieldInfo(event), new ToRepeatFieldInfo(event, false),
                new ReminderFieldInfo(event), new DurationFieldInfo(event, false));
    }

    @Override
    public Event getEvent() {
        return event;
    }
}
