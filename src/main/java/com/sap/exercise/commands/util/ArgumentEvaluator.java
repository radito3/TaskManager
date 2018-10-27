package com.sap.exercise.commands.util;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ArgumentEvaluator {

    private Evaluator evaluator;

    public ArgumentEvaluator(String... args) {
        Supplier<Stream<String>> argStream = () -> Stream.of(args)
                .filter(s -> !s.isEmpty());

        if (argStream.get().count() != argStream.get()
                .filter(s -> s.matches("2\\d{3}-[01]\\d-[0-3]\\d-?")).count()) {
            throw new IllegalArgumentException("Invalid date format");
        }

        switch (Math.toIntExact(argStream.get().count())) {
            case 0:
                evaluator = new ZeroArgEvaluator();
                break;
            case 1:
                evaluator = new OneArgEvaluator(argStream.get().findFirst().orElse(""));
                break;
            case 2:
                evaluator = new TwoArgEvaluator(args[0], args[1]);
                break;
        }
    }

    public <T> T eval(BiFunction<String, String, T> func) {
        return evaluator.evaluate(func);
    }
}
