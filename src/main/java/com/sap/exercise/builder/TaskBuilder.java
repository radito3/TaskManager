package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.ArrayList;
import java.util.List;

public class TaskBuilder extends AbstractEventBuilder implements EventBuilder {

    TaskBuilder(Event event) {
        super(event);
        fields = new ArrayList<>(8);
        fields.add(new FieldInfo("title", true));
        fields.add(new FieldInfo("when", false));
        fields.add(new FieldInfo("allDay", "All day? [Y]es [N]o", true));
        fields.add(new FieldInfo("toRepeat", "Repeat? [N]o [D]aily [W]eekly [M]onthly [Y]early", true));
        fields.add(new FieldInfo("location", false));
        fields.add(new FieldInfo("description", false));
        fields.add(new FieldInfo("reminder", false));
        fields.add(new FieldInfo("duration", false));
    }

    @Override
    public List<FieldInfo> getFields() {
        return fields;
    }

    @Override
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
            case "location":
                return super.append(InputArgs.Location.build(input));
            case "description":
                return super.append(InputArgs.Description.build(input));
            case "duration":
                return super.append(InputArgs.Duration.build(input));
            case "reminder":
                return super.append(InputArgs.Reminder.build(input));
        }
        return this;
    }

    @Override
    public Event build() {
        return event;
    }
}
