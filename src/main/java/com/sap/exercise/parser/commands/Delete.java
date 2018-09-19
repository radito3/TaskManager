package com.sap.exercise.parser.commands;

import com.sap.exercise.db.DatabaseUtil;
import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.model.Task;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Delete implements Command {

    //example command
    //del -t DoHW -r Work
    //this will delete the task "DoHW" and the reminder "Work"

    //"del -all" will not be a valid command

    @Override
    public String getName() {
        return "del";
    }

    @Override
    public void execute(String... args) {
        DatabaseUtil db = DatabaseUtilFactory.getDbClient();
        //verify if arguments are valid tasks/reminders/goals

        for (int i = 1; i < args.length - 1; i++) {
            final int k = i + 1;
            switch (args[i]) {
                case "-t":
                    if (args[k].startsWith("-")) throw new IllegalArgumentException("Invalid task name");

                    Task task = db.getObject(s ->
                            s.createNativeQuery("FROM Task WHERE Title = " + args[k], Task.class).getSingleResult());

                    db.processObject(s -> s.delete(task));
                    break;
                case "-r":
                    if (args[k].startsWith("-")) throw new IllegalArgumentException("Invalid reminder name");

                    throw new NotImplementedException();
//                    break;
                case "-g":
                    if (args[k].startsWith("-")) throw new IllegalArgumentException("Invalid goal name");

                    throw new NotImplementedException();
//                    break;
                default:
                    throw new IllegalArgumentException("Invalid event type argument");
            }
        }
        printer.print("Deletion successful"); //only if arguments are valid
    }
}
