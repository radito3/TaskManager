package com.sap.exercise.commands;

import com.sap.exercise.Application;
import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.handler.SharedResourcesFactory;
import com.sap.exercise.printer.OutputPrinterProvider;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ExitCommand implements Command {

    @Override
    public int execute() {
        SharedResourcesFactory.shutdown();
        DatabaseUtilFactory.close();
        OutputPrinterProvider.close();
        try {
            Application.Configuration.INPUT.close();
        } catch (IOException e) {
            Logger.getLogger(getClass()).error("Streams closing error", e);
        }
        return 1;
    }
}
