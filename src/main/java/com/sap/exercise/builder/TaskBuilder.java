package com.sap.exercise.builder;

import com.sap.exercise.model.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class TaskBuilder extends AbstractBuilder implements EventBuilder {

    TaskBuilder(Event event) {
        super(event);
        fields = getFields(name ->
                name.matches("title|location|description|reminder|when|repeat.+|all day.+"));
    }


    public List<String> getFields() {
        return fields;
    }

    public TaskBuilder append(String field, String value) throws InvocationTargetException, IllegalAccessException {
        Map<Method, TypeWrapper> method = methods.get(getOrigFieldName(field));
        Method m = method.keySet().iterator().next(); //should get the only method in the singleton map
        TypeWrapper wrapper = method.get(m);
        m.invoke(event, wrapper.valueOf(wrapper.filter(value)));
        return this;
    }

    public Event build() {
        return event;
    }
}
