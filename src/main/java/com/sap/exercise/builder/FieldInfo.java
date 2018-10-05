package com.sap.exercise.builder;

import org.apache.commons.lang3.StringUtils;

public class FieldInfo {

    private String name;
    private String alias;
    private boolean mandatory;
    private InputValueTypes valueType;

    FieldInfo(String name, boolean mandatory, InputValueTypes valueType) {
        this(name, "", mandatory, valueType);
    }

    FieldInfo(String name, String alias, boolean mandatory, InputValueTypes valueType) {
        this.name = name;
        this.alias = alias;
        this.mandatory = mandatory;
        this.valueType = valueType;
    }

    public String getName() {
        return name;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public InputValueTypes getValueType() {
        return valueType;
    }

    public String getNameToDisplay() {
        if (alias.isEmpty())
            return StringUtils.capitalize(name);
        else
            return alias;
    }
}
