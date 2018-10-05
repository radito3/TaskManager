package com.sap.exercise.builder;

public interface InputValueFilter<T> {

    T valueOf(String input);
}
