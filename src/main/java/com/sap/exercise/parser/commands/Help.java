package com.sap.exercise.parser.commands;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import javax.swing.*;

import java.io.PrintWriter;

import static com.sap.exercise.Main.OUTPUT;

public class Help implements Command {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute(String... args) {
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Sample message", "Sample title",
                JOptionPane.PLAIN_MESSAGE);

        /*
        if no arguments are present -> display pseudo man page for application
        if an argument is present -> display helper for that argument
         */
        printer.println("in help class");

        //with add and edit will write time format for 'when' field  <dd-mm-yyyy hh:mm:ss>
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(new PrintWriter(OUTPUT), HelpFormatter.DEFAULT_WIDTH, "test", "header", new Options(), HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD, "footer", true);
    }

}
