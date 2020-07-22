package com.sap.exercise.builder;

//Dido: nice, good use of an interface
public interface FieldInfo {

    String getName();

    boolean isMandatory();

    String getNameToDisplay();

    void parseArgument(String arg);

}
