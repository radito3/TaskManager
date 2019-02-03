package com.sap.exercise.wrapper;

import com.sap.exercise.model.Event;
import com.sap.exercise.wrapper.fields.AllDayFieldInfo;
import com.sap.exercise.wrapper.fields.TimeOfFieldInfo;
import com.sap.exercise.wrapper.fields.TitleFieldInfo;
import com.sap.exercise.wrapper.fields.ToRepeatFieldInfo;

import java.util.Arrays;
import java.util.List;

public class ReminderEventWrapper implements EventWrapper {

    private Event event;

    ReminderEventWrapper(Event event) {
        this.event = event;
    }

    @Override
    public List<FieldInfo> getFields() {
        return Arrays.asList(new TitleFieldInfo(event), new TimeOfFieldInfo(event), new AllDayFieldInfo(event),
                new ToRepeatFieldInfo(event, false));
    }

    @Override
    public Event getEvent() {
        return event;
    }
}
