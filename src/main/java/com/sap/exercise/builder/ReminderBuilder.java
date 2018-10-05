package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.sap.exercise.builder.InputValueTypes.*;

public class ReminderBuilder extends AbstractEventBuilder implements EventBuilder {

    ReminderBuilder(Event event) {
        super(event);
        fields = new ArrayList<>(4);
        fields.add(new FieldInfo("title", true, STRING));
        fields.add(new FieldInfo("allDay", "All day? [Y]es [N]o", true, BOOL));
        fields.add(new FieldInfo("when", false, CALENDAR));
        fields.add(new FieldInfo("toRepeat", "Repeat? [N]o [D]aily [W]eekly [M]onthly [Y]early", true, REPEAT));
    }

    public List<FieldInfo> getFields() {
        return fields;
    }

    public ReminderBuilder append(String field, String input) {
        FieldInfo fInfo = findField(field);
        InputValueFilter filter = InputFilterFactory.getInputFilter(fInfo.getValueType());
        switch (fInfo.getName()) {
            case "title":
                event.setTitle((String) filter.valueOf(input));
                break;
            case "allDay":
                event.setAllDay((Boolean) filter.valueOf(input));
                break;
            case "when":
                event.setTimeOf((Calendar) filter.valueOf(input));
                break;
            case "toRepeat":
                event.setToRepeat((Event.RepeatableType) filter.valueOf(input));
                break;
        }
        return this;
    }

    public Event build() {
        return event;
    }
}
