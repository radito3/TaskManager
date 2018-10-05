package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class AbstractEventBuilder {

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

    //will be deleted
    protected static <T, X extends RuntimeException> T filterInput(T obj, Predicate<T> condition, Supplier<X> supplier) {
        return Optional.ofNullable(obj).filter(condition).orElseThrow(supplier);
    }

    protected FieldInfo findField(String val) {
        for (FieldInfo field : fields) {
            if (field.getName().equals(val))
                return field;
        }
        return fields.get(0);
    }

}
