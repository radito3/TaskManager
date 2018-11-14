//Dido: Also builder is not a very expressive name for that whole package. As I see, the package contains domain model knowledge ( what are the fields fo the vents) as well as ui knowledge  - how that domain model is represented in text user interface. 
package com.sap.exercise.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sap.exercise.builder.fields.AllDayFieldInfo;
import com.sap.exercise.builder.fields.DescriptionFieldInfo;
import com.sap.exercise.builder.fields.DurationFieldInfo;
import com.sap.exercise.builder.fields.LocationFieldInfo;
import com.sap.exercise.builder.fields.ReminderFieldInfo;
import com.sap.exercise.builder.fields.TimeOfFieldInfo;
import com.sap.exercise.builder.fields.TitleFieldInfo;
import com.sap.exercise.builder.fields.ToRepeatFieldInfo;
import com.sap.exercise.model.Event;

//Dido: this does not read as a builder (as in the builder pattern) This looks more like a 'wrapper' (check out that pattern)
public class EventBuilder {

    private Event event;

    public EventBuilder(Event event) {
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
