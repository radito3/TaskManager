package com.sap.exercise.commands.util;

import java.util.function.BiFunction;

public class TwoArgEvaluator implements Evaluator {

    private String arg1, arg2;

    TwoArgEvaluator(String arg1, String arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public <T> T evaluate(BiFunction<String, String, T> func) {
        return func.apply(arg1, arg2);
    }

    @Override
    public short getNumOfArgs() {
        return 2;
    }
}
