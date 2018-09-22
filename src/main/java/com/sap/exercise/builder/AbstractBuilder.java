package com.sap.exercise.builder;

import com.sap.exercise.model.Alias;
import com.sap.exercise.model.Event;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class AbstractBuilder {

    protected Event event;
    protected List<String> fields;
    static Map<String, String> aliases = new HashMap<>();

    protected Map<String, Map<Method, Class<?>>> methods = new HashMap<>();

    AbstractBuilder(Event event) {
        this.event = event;
        try {
            methods.put("setTitle", Collections.singletonMap(Event.class.getDeclaredMethod("setTitle", String.class), String.class));

        } catch (NoSuchMethodException ignored) {}
    }

    public static EventBuilder getEventBuilder(Event event) {
        if (event.getTypeOf() == Event.EventType.TASK) {
            return new TaskBuilder(event);
        } else if (event.getTypeOf() == Event.EventType.REMINDER) {
            return new ReminderBuilder(event);
        } else {
            return new GoalBuilder(event);
        }
    }

    protected <T, X extends RuntimeException> T filterInput(T obj, Predicate<T> condition, Supplier<X> supplier) {
        return Optional.ofNullable(obj).filter(condition).orElseThrow(supplier);
    }

    protected List<String> getFields(Predicate<String> condition) {
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
                .map(StringUtils::capitalize)
                .collect(Collectors.toList());
    }
}
