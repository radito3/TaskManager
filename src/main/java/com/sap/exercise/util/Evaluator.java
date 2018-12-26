package com.sap.exercise.util;

import java.util.function.BiFunction;

public interface Evaluator {

    <T> T evaluate(BiFunction<String, String, T> func);

    short getNumOfArgs();

}
