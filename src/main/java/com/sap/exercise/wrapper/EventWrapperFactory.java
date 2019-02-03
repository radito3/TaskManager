package com.sap.exercise.wrapper;

import com.sap.exercise.model.Event;

public class EventWrapperFactory {

    public static EventWrapper getEventWrapper(Event event) {
        switch (event.getTypeOf()) {
            case TASK:
                return new TaskEventWrapper(event);
            case GOAL:
                return new GoalEventWrapper(event);
            case REMINDER:
                return new ReminderEventWrapper(event);
        }
        throw new UnsupportedOperationException("Invalid event type");
    }
}
