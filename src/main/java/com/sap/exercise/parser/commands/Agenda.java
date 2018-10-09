package com.sap.exercise.parser.commands;

public class Agenda implements Command {

    @Override
    public String getName() {
        return "agenda";
    }
    /*this will print the events for a specified time period as :
        <day>  <hour>  <event>
        ...

    (text has configurable color)
     */
    @Override
    public void execute(String... args) {
        printer.println("in agenda class");
    }
}
