package com.sap.exercise.filters;

public class IntegerFilter implements InputValueFilter<Integer> {

    @Override
    public Integer valueOf(String input) {
        return Integer.valueOf(filter(input));
    }

    private String filter(String val) {
        if (!val.matches("^\\s*\\d{1,3}\\s*$")) {
            throw new IllegalArgumentException("Invalid number");
        }
        return val;
    }
}
