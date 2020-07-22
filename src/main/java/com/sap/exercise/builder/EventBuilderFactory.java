package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

public class EventBuilderFactory {

    public static EventBuilder newEventBuilder(Event.EventType eventType) {
        switch (eventType) {
            case TASK:
                return new TaskEventBuilder();
            case GOAL:
                return new GoalEventBuilder();
            case REMINDER:
                return new ReminderEventBuilder();
        }
        throw new UnsupportedOperationException("Invalid event type");
    }

    public static EventBuilder newEventBuilder(Event event) {
        switch (event.getTypeOf()) {
            case TASK:
                return new TaskEventBuilder(event);
            case GOAL:
                return new GoalEventBuilder(event);
            case REMINDER:
                return new ReminderEventBuilder(event);
        }
        throw new UnsupportedOperationException("Invalid event type");
    }
}
