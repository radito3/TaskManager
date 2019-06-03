package com.sap.exercise.flowable;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.CommandExecutionResult;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class ExecuteCommandStep implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Command command = (Command) delegateExecution.getVariable("command");
        try {
            CommandExecutionResult result = command.execute();
            delegateExecution.setVariable("commandResult", result);
            StepsUtil.finishCommand(delegateExecution);
        } catch (Exception e) {
            StepsUtil.handlePostStepError(delegateExecution, e);
        }
    }
}
