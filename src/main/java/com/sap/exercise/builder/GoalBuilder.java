package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.sap.exercise.builder.InputValueTypes.*;

public class GoalBuilder extends AbstractEventBuilder implements EventBuilder {

    GoalBuilder(Event event) {
        super(event);
        fields = new ArrayList<>(5);
        fields.add(new FieldInfo("title", true, STRING));
        fields.add(new FieldInfo("when", false, CALENDAR));
        fields.add(new FieldInfo("toRepeat", "Repeat? [N]o [D]aily [W]eekly [M]onthly [Y]early", true, REPEAT));
        fields.add(new FieldInfo("reminder", false, INTEGER));
        fields.add(new FieldInfo("duration", false, INTEGER));
    }

    @Override
    public List<FieldInfo> getFields() {
        return fields;
    }

    @Override
    public EventBuilder append(String field, String input) {
        FieldInfo fInfo = findField(field);
        InputValueFilter filter = InputFilterFactory.getInputFilter(fInfo.getValueType());
        switch (fInfo.getName()) {
            case "title":
                event.setTitle((String) filter.valueOf(input));
                break;
            case "when":
                event.setTimeOf((Calendar) filter.valueOf(input));
                break;
            case "toRepeat":
                event.setToRepeat((Event.RepeatableType) filter.valueOf(input));
                break;
            case "duration":
                event.setDuration((Integer) filter.valueOf(input));
                break;
            case "reminder":
                event.setReminder((Integer) filter.valueOf(input));
                break;
        }
        return this;
    }

    @Override
    public Event build() {
        return event;
    }
}