package com.sap.exercise.commands.util;

import java.util.function.BiFunction;

public class OneArgEvaluator implements Evaluator {

    private String arg1, arg2;

    OneArgEvaluator(String arg) {
        String[] args = getArgs();
        if (arg.endsWith("-")) {
            arg1 = args[0];
            arg2 = arg.substring(0, arg.length() - 1);
        } else {
            arg1 = arg;
            arg2 = args[1];
        }
    }

    @Override
    public <T> T evaluate(BiFunction<String, String, T> func) {
        return func.apply(arg1, arg2);
    }
}
