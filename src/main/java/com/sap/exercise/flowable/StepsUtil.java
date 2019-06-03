package com.sap.exercise.flowable;

import com.sap.exercise.printer.OutputPrinterProvider;
import org.flowable.engine.delegate.DelegateExecution;

import java.util.Arrays;

class StepsUtil {

    static void handlePreStepError(DelegateExecution delegateExecution) {
        boolean error = (boolean) delegateExecution.getVariable("error");
        if (error) {
            delegateExecution.setVariable("error", false);
        }
    }

    static void handlePostStepError(DelegateExecution delegateExecution, Exception e) {
        OutputPrinterProvider.getPrinter().printStackTrace(e);
        delegateExecution.setVariable("error", true);
        finishCommand(delegateExecution);
    }

    static void finishCommand(DelegateExecution delegateExecution) {
        String[] allCommands = (String[]) delegateExecution.getVariable("allCommands");
        if (allCommands.length > 1) {
            delegateExecution.setVariable("allCommands",
                    Arrays.copyOfRange(allCommands, 1, allCommands.length));
        }
    }
}
