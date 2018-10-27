package com.sap.exercise.filters;

public class BooleanFilter implements InputValueFilter<Boolean> {

    @Override
    public Boolean valueOf(String input) {
        return Boolean.valueOf(filter(input));
    }

    private String filter(String val) {
        if (val.toLowerCase().matches("^\\s*y|yes\\s*$")) {
            return "true";
        } else if (val.toLowerCase().matches("^\\s*n|no\\s*$")) {
            return "false";
        } else {
            throw new IllegalArgumentException("Invalid input");
        }
    }
}
