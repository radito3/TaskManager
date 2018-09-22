package com.sap.exercise.builder;

import com.sap.exercise.model.Event;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public interface EventBuilder {

    List<String> getFields();

    default String getOrigFieldName(String alias) {
        for (Map.Entry<String, String> entry : AbstractBuilder.aliases.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(alias)) {
                return StringUtils.capitalize(entry.getKey());
            }
        }
        return StringUtils.capitalize(alias);
    }

    EventBuilder append(String field, String val) throws InvocationTargetException, IllegalAccessException;

    Event build();
}
