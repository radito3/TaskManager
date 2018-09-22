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
        printer.println("in help class");
    }

    /*
    with add and edit will write time format for 'when' field

    add ->
        add [[-t|--task]|[-r|--reminder]|[-g|--goal]]
            event fields for event: input
     */
}
