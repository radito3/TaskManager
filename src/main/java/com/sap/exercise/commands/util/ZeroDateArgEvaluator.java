package com.sap.exercise.commands.util;

import java.util.function.BiFunction;

public class ZeroDateArgEvaluator implements Evaluator {

    private String arg1, arg2;

    ZeroDateArgEvaluator() {
        String[] args = getWeekTimeFrame();
        arg1 = args[0];
        arg2 = args[1];
    }

    @Override
    public <T> T evaluate(BiFunction<String, String, T> func) {
        return func.apply(arg1, arg2);
    }

    @Override
    public short getNumOfArgs() {
        return 0;
    }
}
