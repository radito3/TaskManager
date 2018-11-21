package com.sap.exercise.wrapper;

//Dido: nice, good use of an interface
public interface FieldInfo {

    String getName();

    boolean isMandatory();

    String getNameToDisplay();

    // Dido : name methods as verbs / actions - the behavior this object 'does'/'acts' , not 'is'
    // Rangel: Like this?
    void handleArg(String arg);

}
