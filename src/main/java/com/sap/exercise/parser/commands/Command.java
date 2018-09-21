package com.sap.exercise.parser.commands;

import com.sap.exercise.printer.OutputPrinter;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.sap.exercise.Main.OUTPUT;

public interface Command {

    OutputPrinter printer = new OutputPrinter(OUTPUT);

    String getName();

    void execute(String... args);

    //this will be moved to AbstractBuilder (need to move JUnit tests as well)
    static <T, X extends RuntimeException> T filterInput(T obj, Predicate<T> condition, Supplier<X> supplier) {
        return Optional.ofNullable(obj).filter(condition).orElseThrow(supplier);
    }
}
