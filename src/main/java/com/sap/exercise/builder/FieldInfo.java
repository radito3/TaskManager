package com.sap.exercise.builder;

public interface FieldInfo {

    String getName();

    boolean isMandatory();

    String getNameToDisplay();

    void argHandler(String arg);

}
