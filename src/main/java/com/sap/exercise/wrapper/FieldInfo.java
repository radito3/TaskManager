package com.sap.exercise.wrapper;

//Dido: nice, good use of an interface
public interface FieldInfo {

    String getName();

    boolean isMandatory();

    String getNameToDisplay();

    void handleArg(String arg);

}
