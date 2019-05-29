package com.sap.exercise.handler;

import com.sap.exercise.util.DateArgumentEvaluator;

import java.util.HashMap;
import java.util.Map;

public class DeleteOptions extends GetInTimeFrameOptions {

    private final DateArgumentEvaluator evaluator;

    public DeleteOptions(DateArgumentEvaluator evaluator, String startDate, String endDate) {
        super(startDate, endDate);
        this.evaluator = evaluator;
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> result = new HashMap<>();
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        return result;
    }

    @Override
    public boolean getCondition() {
        return evaluator.numOfArgs() == 0;
    }
}
