package com.sap.exercise.flowable;

import com.sap.exercise.Configuration;
import org.apache.commons.io.input.CloseShieldInputStream;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputParserStep implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new CloseShieldInputStream(Configuration.INPUT)))) {
            String userInput;
            do {
                userInput = reader.readLine();
            } while (userInput.trim().isEmpty());
            String[] userInputArgs = userInput.split("\\s+");
            delegateExecution.setVariable("userInput", userInputArgs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
