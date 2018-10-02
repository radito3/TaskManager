package com.sap.exercise.builder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.sap.exercise.model.Event;

public class EventBuilderImpl extends AbstractBuilder implements EventBuilder {

    EventBuilderImpl(Event event) {
        super(event);
        if (event.getTypeOf() == Event.EventType.TASK) {
            fields = getFields(name -> name.matches("title|location|description|reminder|when|repeat.+|all day.+|duration"));
        } else if (event.getTypeOf() == Event.EventType.REMINDER) {
            fields = getFields(name -> name.matches("title|all day.+|when|repeat.+"));
        } else {
            fields = getFields(name -> name.matches("title|when|duration|reminder|repeat.+"));
        }
    }

    public List<String> getFields() {
        return fields;
    }

    public String getOrigFieldName(String alias) {
        for (Map.Entry<String, String> entry : aliases.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(alias)) {
                return StringUtils.capitalize(entry.getKey());
            }
        }
        return StringUtils.capitalize(alias);
    }

    // As generic as this code seems to be - the idea of navigating to setter methods based on field names, I have strong doubts of it's
    // resilience to regressions when the event entity evolves
    // But if you solve this without reflection, the java compiler and IDE might just help you avoid a NoSuchMethodException.
    // And overall - it may not be a good idea to share field names in the ui - this is a separate kind of metadata, which deserves a decent
    // depiction in your code
    public EventBuilder append(String field, String value) throws InvocationTargetException, IllegalAccessException {
        Map<Method, TypeWrapper> method = methods.get(getOrigFieldName(field));
        Method m = method.keySet().iterator().next();
        TypeWrapper wrapper = method.get(m);
        m.invoke(event, wrapper.valueOf(wrapper.filter(value)));
        return this;
    }

    public Event build() {
        return event;
    }
}
