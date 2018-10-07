package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.ArrayList;
import java.util.List;

public class ReminderBuilder extends AbstractEventBuilder implements EventBuilder {

    ReminderBuilder(Event event) {
        super(event);
        fields = new ArrayList<>(4);
        fields.add(new FieldInfo("title", true));
        fields.add(new FieldInfo("allDay", "All day? [Y]es [N]o", true));
        fields.add(new FieldInfo("when", false));
        fields.add(new FieldInfo("toRepeat", "Repeat? [N]o [D]aily [W]eekly [M]onthly [Y]early", true));
    }

    public List<FieldInfo> getFields() {
        return fields;
    }

    public EventBuilder append(String field, String input) {
        FieldInfo fInfo = findField(field);
        switch (fInfo.getName()) {
            case "title":
                return super.append(InputArgs.Title.build(input));
            case "allDay":
                return super.append(InputArgs.AllDay.build(input));
            case "when":
                return super.append(InputArgs.When.build(input));
            case "toRepeat":
                return super.append(InputArgs.Repeat.build(input));
        }
        return this;
    }

    public Event build() {
        return event;
    }
}
