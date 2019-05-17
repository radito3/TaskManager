package com.sap.exercise.flowable;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.parser.CommandParser;
import com.sap.exercise.commands.parser.CommandParserFactory;
import com.sap.exercise.printer.OutputPrinterProvider;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.util.Arrays;

public class CommandParserStep implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        String[] userInputArgs = (String[]) delegateExecution.getVariable("userInput");
        Command command;
        try {
            CommandParser parser = CommandParserFactory.getParser(userInputArgs[0]);
            command = parser.parse(Arrays.copyOfRange(userInputArgs, 1, userInputArgs.length));
        } catch (IllegalArgumentException e) {
            command = () -> {
                OutputPrinterProvider.getPrinter().println(e.getMessage());
                return 0;
            };
        }
        delegateExecution.setVariable("command", command);
    }
}
