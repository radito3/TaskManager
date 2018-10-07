package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.Calendar;
import java.util.List;

public abstract class AbstractEventBuilder implements EventBuilder {

    protected Event event;
    protected List<FieldInfo> fields;

    AbstractEventBuilder(Event event) {
        this.event = event;
    }

    public static EventBuilder getEventBuilder(Event event) {
        switch (event.getTypeOf()) {
            case REMINDER:
                return new ReminderBuilder(event);
            case GOAL:
                return new GoalBuilder(event);
            default:
                return new TaskBuilder(event);
        }
    }

    //have to figure out a way to not need this
    protected FieldInfo findField(String val) {
        for (FieldInfo field : fields) {
            if (field.getName().equals(val))
                return field;
        }
        return fields.get(0);
    }

    //to have this approach, I need to map the FieldInfo with the InputArg
    public EventBuilder append(InputArgs.Title title) {
        InputValueFilter<String> filter = new StringFilter();
        event.setTitle(filter.valueOf(title.getValue()));
        return this;
    }

    public EventBuilder append(InputArgs.When when) {
        InputValueFilter<Calendar> filter = new CalendarFilter();
        event.setTimeOf(filter.valueOf(when.getValue()));
        return this;
    }

    public EventBuilder append(InputArgs.AllDay allDay) {
        InputValueFilter<Boolean> filter = new BooleanFilter();
        event.setAllDay(filter.valueOf(allDay.getValue()));
        return this;
    }

    public EventBuilder append(InputArgs.Location location) {
        InputValueFilter<String> filter = new StringFilter();
        event.setLocation(filter.valueOf(location.getValue()));
        return this;
    }

    public EventBuilder append(InputArgs.Description description) {
        InputValueFilter<String> filter = new StringFilter();
        event.setDescription(filter.valueOf(description.getValue()));
        return this;
    }

    public EventBuilder append(InputArgs.Reminder reminder) {
        InputValueFilter<Integer> filter = new IntegerFilter();
        event.setReminder(filter.valueOf(reminder.getValue()));
        return this;
    }

    public EventBuilder append(InputArgs.Repeat repeat) {
        InputValueFilter<Event.RepeatableType> filter = new RepeatableFilter();
        event.setToRepeat(filter.valueOf(repeat.getValue()));
        return this;
    }

    public EventBuilder append(InputArgs.Duration duration) {
        InputValueFilter<Integer> filter = new IntegerFilter();
        event.setDuration(filter.valueOf(duration.getValue()));
        return this;
    }
}
