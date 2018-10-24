package com.sap.exercise.commands.util;

import java.util.function.BiFunction;

public class ZeroArgEvaluator implements Evaluator {

    private String arg1, arg2;

    ZeroArgEvaluator() {
        String[] args = getArgs();
        arg1 = args[0];
        arg2 = args[1];
    }

    @Override
    public <T> T evaluate(BiFunction<String, String, T> func) {
        return func.apply(arg1, arg2);
    }
}
