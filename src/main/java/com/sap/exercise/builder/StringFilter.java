package com.sap.exercise.builder;

import java.util.Optional;

public class StringFilter implements InputValueFilter<String> {

    @Override
    public String valueOf(String input) {
        return Optional.ofNullable(input)
                .filter(string -> string.matches("[-_.a-zA-Z0-9 ]*"))
                .orElseThrow(() -> new IllegalArgumentException("Illegal characters in input"));
    }
}
