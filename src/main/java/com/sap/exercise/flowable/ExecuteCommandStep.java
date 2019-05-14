package com.sap.exercise.flowable;

import com.sap.exercise.commands.Command;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class ExecuteCommandStep implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Command command = (Command) delegateExecution.getVariable("command");
        int result = command.execute();
        delegateExecution.setVariable("commandResult", result);
    }
}
