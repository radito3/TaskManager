package com.sap.exercise.handler;

public class DeleteCondition extends TimeFrameCondition {

    private final boolean toExecute;

    public DeleteCondition(String startDate, String endDate, boolean toExecute) {
        super(startDate, endDate);
        this.toExecute = toExecute;
    }

    @Override
    public boolean isToBeExecuted() {
        return toExecute;
    }
}
