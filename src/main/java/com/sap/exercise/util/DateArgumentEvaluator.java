package com.sap.exercise.util;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DateArgumentEvaluator {

    private Evaluator evaluator;

    public DateArgumentEvaluator(String... args) {
        Predicate<String> condition = String::isEmpty;
        Supplier<Stream<String>> argStream = () -> Stream.of(args).filter(condition.negate());

        switch (Math.toIntExact(argStream.get().count())) {
            case 0:
                evaluator = new ZeroDateArgEvaluator();
                break;
            case 1:
                String arg = argStream.get().findFirst().orElse("");
                boolean end = arg.endsWith("-");
                DateHandler handler = new DateHandler(arg);
                evaluator = new OneDateArgEvaluator(handler.asString() + (end ? "-" : ""));
                break;
            case 2:
                DateHandler handler1 = new DateHandler(args[0]);
                DateHandler handler2 = new DateHandler(args[1]);
                evaluator = new TwoDateArgEvaluator(handler1.asString(), handler2.asString());
                break;
        }
    }

    public <T> T eval(BiFunction<String, String, T> func) {
        return evaluator.evaluate(func);
    }

    public short numOfArgs() {
        return evaluator.getNumOfArgs();
    }
}
