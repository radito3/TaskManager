package com.sap.exercise.flowable;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.CommandExecutionResult;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.ExceptionMessageHandler;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class ExecuteCommandStep implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Command command = (Command) delegateExecution.getVariable("command");
        CommandExecutionResult result = command.execute();
        if (result == CommandExecutionResult.ERROR) {
            OutputPrinterProvider.getPrinter()
                    .println(ExceptionMessageHandler.getMessage());
        }
        delegateExecution.setVariable("commandResult", result);
    }
}
