package com.sap.exercise.handler;

import com.sap.exercise.util.DateArgumentEvaluator;

public class DeleteCondition extends TimeFrameCondition {

    private final DateArgumentEvaluator evaluator;

    public DeleteCondition(DateArgumentEvaluator evaluator, String startDate, String endDate) {
        super(startDate, endDate);
        this.evaluator = evaluator;
    }

    @Override
    public boolean isToBeExecuted() {
        return evaluator.numOfArgs() == 0;
    }
}
