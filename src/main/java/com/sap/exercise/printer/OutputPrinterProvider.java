package com.sap.exercise.printer;

import com.sap.exercise.Configuration;

public class OutputPrinterProvider {

    private static OutputPrinter printer;

    public static OutputPrinter getPrinter() {
        if (printer == null)
            printer = new OutputPrinter(Configuration.OUTPUT);
        return printer;
    }

    public static void close() {
        if (printer != null)
            printer.close();
    }
}
