package com.sap.exercise.commands;

import com.sap.exercise.Configuration;
import com.sap.exercise.persistence.SessionProviderFactory;
import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.notifications.NotificationFactory;
import com.sap.exercise.printer.OutputPrinterProvider;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ExitCommand implements Command {

    @Override
    public CommandExecutionResult execute() {
        SharedResourcesFactory.shutdown();
        NotificationFactory.clearEventsSet();
        SessionProviderFactory.close();
        try {
            Configuration.INPUT.close();
        } catch (IOException e) {
            Logger.getLogger(ExitCommand.class).error("Input stream closing error", e);
        }
        OutputPrinterProvider.close();
        return CommandExecutionResult.EXIT;
    }
}
