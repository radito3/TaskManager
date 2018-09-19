package com.sap.exercise.parser.commands;

public class Add implements Command {

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public void execute(String... args) {
        //if AllDay is true -> Duration will be in number of days
        //if AllDay is false -> Duration is number of minutes
        printer.print("in add class");
    }

    /*
    when starting this command the optional flags will be:
        -t[--task] for a task;
        -r[--reminder] for a reminder;
        -g[--goal] for a goal.
    default event created is task

    after typing the command the respective fields of the event created will appear and input for them will prompt user

    method:
        <event_type> event = new Object();
        list<event_field> fields = event.getFields();

        try (Scanner s = new Scanner(inputStream)) {
            for (field f : fields) {
                printer.print(f.name() + ": ");
                String input = s.nextLine().filter(<criteria>);
                event.setField(input);
            }
        }

        <db_client> db = dbFactory.getDbClient();
        db.processObject(s -> s.save(event));
     */

    private String flagHandler(String input) {
        return input.startsWith("--") ? input.replace(input.charAt(1), input.charAt(2)).substring(0, 2) : input;
    }
}
