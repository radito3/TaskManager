package com.sap.exercise.builder;

import org.apache.commons.lang3.StringUtils;

public class FieldInfo {

    private String name;
    private String alias;
    private boolean mandatory;

    FieldInfo(String name, boolean mandatory) {
        this(name, "", mandatory);
    }

    FieldInfo(String name, String alias, boolean mandatory) {
        this.name = name;
        this.alias = alias;
        this.mandatory = mandatory;
    }

    public String getName() {
        return name;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public String getNameToDisplay() {
        if (alias.isEmpty())
            return StringUtils.capitalize(name);
        else
            return alias;
    }

}
