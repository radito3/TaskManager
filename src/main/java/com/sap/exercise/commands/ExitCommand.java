package com.sap.exercise.commands;

import com.sap.exercise.Configuration;
import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.handler.SharedResourcesFactory;
import com.sap.exercise.notifications.NotificationFactory;
import com.sap.exercise.printer.OutputPrinterProvider;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;

public class ExitCommand implements Command, Serializable {

    @Override
    public int execute() {
        SharedResourcesFactory.shutdown();
        DatabaseUtilFactory.close();
        OutputPrinterProvider.close();
        NotificationFactory.clearEventsSet();
        try {
            Configuration.INPUT.close();
        } catch (IOException e) {
            Logger.getLogger(getClass()).error("Streams closing error", e);
        }
        return 1;
    }
}
