package com.sap.exercise.parser.commands;

public class Help implements Command {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute(String... args) {
        /*
        if no arguments are present -> display pseudo man page for application
        if an argument is present -> display helper for that argument
         */
        printer.print("in help class");
    }

    /*
    add ->
        add [[-t|--task]|[-r|--reminder]|[-g|--goal]]
            event fields for event: input
     */
}
