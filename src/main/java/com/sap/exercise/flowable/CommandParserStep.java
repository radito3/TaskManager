package com.sap.exercise.flowable;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.parser.CommandParser;
import com.sap.exercise.commands.parser.CommandParserFactory;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.util.Arrays;

public class CommandParserStep implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        String[] userInputArgs = (String[]) delegateExecution.getVariable("userInput");

        CommandParserFactory factory = new CommandParserFactory();
        try {
            CommandParser parser = factory.getParser(userInputArgs[0]);
            Command command = parser.parse(Arrays.copyOfRange(userInputArgs, 1, userInputArgs.length));
            delegateExecution.setVariable("command", command);
        } catch (Exception e) {
            StepsUtil.handlePostStepError(delegateExecution, e);
        }
    }
}
