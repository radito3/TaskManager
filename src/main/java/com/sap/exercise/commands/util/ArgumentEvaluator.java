package com.sap.exercise.commands.util;

import java.util.function.BiFunction;
import java.util.stream.Stream;

public class ArgumentEvaluator {

    private Evaluator evaluator;

    public ArgumentEvaluator(String... args) {
        switch (Stream.of(args)
                .filter(s -> !s.isEmpty())
                .filter(s -> s.matches("2\\d{3}-[01]\\d-[0-3]\\d"))
                .toArray().length) {
            case 0:
                evaluator = new ZeroArgEvaluator();
            case 1:
                evaluator = new OneArgEvaluator(args[0]);
            case 2:
                evaluator = new TwoArgEvaluator(args[0], args[1]);
        }
    }

    public <T> T eval(BiFunction<String, String, T> func) {
        return evaluator.evaluate(func);
    }
}
