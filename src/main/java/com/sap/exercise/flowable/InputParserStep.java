package com.sap.exercise.flowable;

import com.sap.exercise.Configuration;
import com.sap.exercise.printer.OutputPrinterProvider;
import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.commons.lang3.ArrayUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputParserStep implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        String[] allCommands = (String[]) delegateExecution.getVariable("allCommands");
        StepsUtil.handlePreStepError(delegateExecution);

        if (allCommands.length == 0) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new CloseShieldInputStream(Configuration.INPUT)))) {

                allCommands = reader.lines()
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toArray(String[]::new);
            } catch (IOException e) {
                OutputPrinterProvider.getPrinter().printStackTrace(e);
                delegateExecution.setVariable("error", true);
                return;
            }

            if (!ArrayUtils.contains(allCommands, "exit")) {
                allCommands = ArrayUtils.addAll(allCommands, "exit");
            }
            delegateExecution.setVariable("allCommands", allCommands);
        }
        delegateExecution.setVariable("userInput", allCommands[0].split("\\s+"));
    }
}
