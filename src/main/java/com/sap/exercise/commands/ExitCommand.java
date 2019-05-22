package com.sap.exercise.commands;

import com.sap.exercise.Configuration;
import com.sap.exercise.persistence.DatabaseUtilFactory;
import com.sap.exercise.handler.SharedResourcesFactory;
import com.sap.exercise.notifications.NotificationFactory;
import com.sap.exercise.printer.OutputPrinterProvider;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ExitCommand implements Command {

    @Override
    public CommandExecutionResult execute() {
        SharedResourcesFactory.close();
        DatabaseUtilFactory.close();
        OutputPrinterProvider.close();
        NotificationFactory.clearEventsSet();
        try {
            Configuration.INPUT.close();
        } catch (IOException e) {
            Logger.getLogger(ExitCommand.class).error("Streams closing error", e);
        }
        return CommandExecutionResult.EXIT;
    }
}
