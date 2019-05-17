package com.sap.exercise.flowable;

import com.sap.exercise.commands.Command;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.CommandExecutionException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class ExecuteCommandStep implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Command command = (Command) delegateExecution.getVariable("command");
        int result = 0;
        try {
            result = command.execute();
        } catch (CommandExecutionException e) {
            OutputPrinterProvider.getPrinter().printStackTrace(e);
        }
        delegateExecution.setVariable("commandResult", result);
    }
}
