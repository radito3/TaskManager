package com.sap.exercise.builder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.sap.exercise.model.Alias;
import com.sap.exercise.model.Event;

//this name is a bit generic
public abstract class AbstractBuilder {

    protected Event event;
    protected List<String> fields;
    protected Map<String, String> aliases = new HashMap<>();

    protected Map<String, Map<Method, TypeWrapper>> methods = new HashMap<>();

    AbstractBuilder(Event event) {
        this.event = event;
        try {// something tells me that there is some duplication in the following lines.
             // Btw- why do you have annotations + reflection to persist UI info in the entities, when you have for what they seem to be
             // hard-coded ui strings here?
            methods.put("Title", Collections.singletonMap(Event.class.getDeclaredMethod("setTitle", String.class), new TypeWrapper("string")));
            methods.put("Location", Collections.singletonMap(Event.class.getDeclaredMethod("setLocation", String.class), new TypeWrapper("string")));
            methods.put("TimeOf", Collections.singletonMap(Event.class.getDeclaredMethod("setTimeOf", Calendar.class), new TypeWrapper("calendar")));
            methods.put("Description", Collections.singletonMap(Event.class.getDeclaredMethod("setDescription", String.class), new TypeWrapper("string")));
            methods.put("AllDay", Collections.singletonMap(Event.class.getDeclaredMethod("setAllDay", Boolean.class), new TypeWrapper("bool")));
            methods.put("Duration", Collections.singletonMap(Event.class.getDeclaredMethod("setDuration", Integer.class), new TypeWrapper("integer")));
            methods.put("Reminder", Collections.singletonMap(Event.class.getDeclaredMethod("setReminder", Integer.class), new TypeWrapper("integer")));
            methods.put("ToRepeat", Collections.singletonMap(Event.class.getDeclaredMethod("setToRepeat", Event.RepeatableType.class), new TypeWrapper("repeat")));
        } catch (NoSuchMethodException ignored) {}
    }

    public static EventBuilder getEventBuilder(Event event) {
        // It does not make much sense for a parent class to have notion of it's inheritor (and maybe neglect the others extending it)
        return new EventBuilderImpl(event);
    }

    protected static <T, X extends RuntimeException> T filterInput(T obj, Predicate<T> condition, Supplier<X> supplier) {
        return Optional.ofNullable(obj).filter(condition).orElseThrow(supplier);
    }
    // Streams are cool, but do replace the lambdas with method references where possible. This is pretty hard to read & understand
    // and even if refactored, 3 filters, a peek and 2 mappings are no easy stream to follow without distracting my self of kittens
    // btw - be careful when you modify a shared non-thread safe collection from within processing a stream
    protected List<String> getFields(Predicate<String> condition) {
        return Arrays.stream(Event.class.getDeclaredFields())
                .peek(field -> {
                    if (field.isAnnotationPresent(Alias.class)) {
                        aliases.put(field.getName(), field.getAnnotation(Alias.class).value());
                    }
                })
                .map(Field::getName)
            .filter(name -> !name.equals("id")) // this seems a bit hard coded.. why not go hard with annotations ;)
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
