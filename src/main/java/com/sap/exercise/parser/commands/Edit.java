package com.sap.exercise.parser.commands;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Edit implements Command {

    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public void execute(String... args) {

        printer.print("in edit class");
    }

    private <T, X extends RuntimeException> T filter(T obj, Predicate<T> condition, Supplier<X> supplier) {
        return Optional.of(obj).filter(condition).orElseThrow(supplier);
    }
}
