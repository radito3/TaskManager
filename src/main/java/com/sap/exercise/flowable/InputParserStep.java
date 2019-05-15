package com.sap.exercise.flowable;

import com.sap.exercise.Configuration;
import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.commons.lang3.ArrayUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class InputParserStep implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new CloseShieldInputStream(Configuration.INPUT)))) {

            String[] allCommands = (String[]) delegateExecution.getVariable("allCommands");

            if (allCommands.length == 0) {
                allCommands = reader.lines()
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toArray(String[]::new);

                if (!allCommands[allCommands.length - 1].equals("exit")) {
                    allCommands = ArrayUtils.addAll(allCommands, "exit");
                }
            }

            delegateExecution.setVariable("userInput", allCommands[0].split("\\s+"));
            if (allCommands.length > 1) {
                delegateExecution.setVariable("allCommands",
                        Arrays.copyOfRange(allCommands, 1, allCommands.length));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
