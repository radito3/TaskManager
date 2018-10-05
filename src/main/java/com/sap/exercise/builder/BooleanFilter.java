package com.sap.exercise.builder;

public class BooleanFilter implements InputValueFilter<Boolean> {

    @Override
    public Boolean valueOf(String input) {
        return Boolean.valueOf(filter(input));
    }

    private String filter(String val) {
        if (val.matches("^\\s*[yY]|[yY]es\\s*$")) {
            return "true";
        } else if (val.matches("^\\s*[nN]|[nN]o\\s*$")) {
            return "false";
        } else {
            throw new IllegalArgumentException("Invalid input");
        }
    }
}
