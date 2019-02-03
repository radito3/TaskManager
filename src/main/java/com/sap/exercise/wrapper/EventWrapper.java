package com.sap.exercise.wrapper;
//TODO - package name does not communicate nothing about it's responsibilities 
import com.sap.exercise.wrapper.fields.*;
import com.sap.exercise.model.Event;

import java.util.Arrays;
import java.util.List;

//If you want to utilize polynmorphysm, you should have 3 inheritors to that class, each returning the fields speciffic for it's event type. And one other e.g. factory class creating the proper inheritor object for the proper event given. 
//Otherwise this class is just as experessive as a static implementation. 
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
        throw new UnsupportedOperationException("Invalid event type");
    }

    public Event getEvent() {
        return event;
    }

}
