package com.sap.exercise.commands.util;

import com.sap.exercise.handler.DateHandler;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ArgumentEvaluator {

    private Evaluator evaluator;

    public ArgumentEvaluator(String... args) {
        Supplier<Stream<String>> argStream = () -> Stream.of(args).filter(s -> !s.isEmpty());

        switch (Math.toIntExact(argStream.get().count())) {
            case 0:
                evaluator = new ZeroArgEvaluator();
                break;
            case 1:
                String arg = argStream.get().findFirst().orElse("");
                boolean end = arg.endsWith("-");
                DateHandler handler = new DateHandler(arg);
                evaluator = new OneArgEvaluator(handler.asString() + (end ? "-" : ""));
                break;
            case 2:
                DateHandler handler1 = new DateHandler(args[0]);
                DateHandler handler2 = new DateHandler(args[1]);
                evaluator = new TwoArgEvaluator(handler1.asString(), handler2.asString());
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
