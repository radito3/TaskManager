package com.sap.exercise.wrapper;

import com.sap.exercise.model.Event;
import com.sap.exercise.wrapper.fields.*;

import java.util.Arrays;
import java.util.List;

public class GoalEventWrapper implements EventWrapper {

    private Event event;

    GoalEventWrapper(Event event) {
        this.event = event;
    }

    @Override
    public List<FieldInfo> getFields() {
        return Arrays.asList(new TitleFieldInfo(event), new TimeOfFieldInfo(event), new ToRepeatFieldInfo(event, true),
                new ReminderFieldInfo(event), new DurationFieldInfo(event, true));
    }

    @Override
    public Event getEvent() {
        return event;
    }
}
