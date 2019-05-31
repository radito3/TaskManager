package com.sap.exercise.handler;

import com.sap.exercise.util.DateArgumentEvaluator;

public class DeleteOptions extends TimeFrameOptions {

    private final DateArgumentEvaluator evaluator;

    public DeleteOptions(DateArgumentEvaluator evaluator, String startDate, String endDate) {
        super(startDate, endDate);
        this.evaluator = evaluator;
    }

    @Override
    public boolean getCondition() {
        return evaluator.numOfArgs() == 0;
    }
}
