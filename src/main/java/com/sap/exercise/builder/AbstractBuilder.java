package com.sap.exercise.builder;

import com.sap.exercise.model.Alias;
import com.sap.exercise.model.Event;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class AbstractBuilder {

    protected Event event;
    protected List<String> fields;
    protected Map<String, Class<?>> fieldParams;

    public static EventBuilder getEventBuilder(Event.EventType type) {
        if (type == Event.EventType.TASK) {
            return new TaskBuilder();
        } else if (type == Event.EventType.REMINDER) {
            return new ReminderBuilder();
        } else {
            return new GoalBuilder();
        }
    }

    protected <T, X extends RuntimeException> T filterInput(T obj, Predicate<T> condition, Supplier<X> supplier) {
        return Optional.ofNullable(obj).filter(condition).orElseThrow(supplier);
    }

    protected List<String> getFields(Predicate<String> condition) {
        Map<String, String> aliases = new HashMap<>();
        return Arrays.stream(Event.class.getDeclaredFields())
                .peek(field -> {
                    if (field.isAnnotationPresent(Alias.class)) {
                        aliases.put(field.getName(), field.getAnnotation(Alias.class).value());
                    }
                })
                .map(Field::getName)
                .filter(name -> !name.equals("id"))
                .map(name -> {
                    for (Map.Entry<String, String> entry : aliases.entrySet()) {
                        if (entry.getKey().equals(name)) {
                            return entry.getValue();
                        }
                    }
                    return name;
                })
                .filter(condition)
                .map(name -> {
                    char[] chars = name.toCharArray();
                    chars[0] = Character.toUpperCase(chars[0]);
                    return new String(chars);
                })
                .collect(Collectors.toList());
    }
}
