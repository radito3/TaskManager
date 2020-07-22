package com.sap.exercise.builder;

import com.sap.exercise.model.Event;
import com.sap.exercise.builder.fields.AllDayFieldInfo;
import com.sap.exercise.builder.fields.TimeOfFieldInfo;
import com.sap.exercise.builder.fields.TitleFieldInfo;
import com.sap.exercise.builder.fields.ToRepeatFieldInfo;

import java.util.Arrays;
import java.util.List;

public class ReminderEventBuilder implements EventBuilder {

    private Event event;

    ReminderEventBuilder() {
        this.event = new Event("", Event.EventType.REMINDER);
    }

    ReminderEventBuilder(Event event) {
        this.event = event;
    }

    @Override
    public List<FieldInfo> getFields() {
        return Arrays.asList(new TitleFieldInfo(event), new TimeOfFieldInfo(event), new AllDayFieldInfo(event),
                new ToRepeatFieldInfo(event, false));
    }

    @Override
    public Event build() {
        return event;
    }
}
