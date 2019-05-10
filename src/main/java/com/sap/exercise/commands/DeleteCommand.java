package com.sap.exercise.commands;

import com.sap.exercise.handler.EventDeletor;
import com.sap.exercise.handler.EventDeletorTF;
import com.sap.exercise.handler.EventGetter;
import com.sap.exercise.util.DateArgumentEvaluator;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.NoSuchElementException;

public class DeleteCommand extends AbstractCommand implements Command {

    private Event event;
    private DateArgumentEvaluator evaluator;

    @Override
    public int execute(String... args) {
        try {
            // Take a look at first comment inside execute method of AddCommand class
            String[] vars = CommandUtils.flagHandlerForTimeFrame(
                    args,
                    cmd -> CommandUtils.buildEventName(cmd.getArgs())
            );
            String start = vars[0],
                    end = vars[1],
                    eventName = vars[2];
            event = new EventGetter().getEventByTitle(eventName);

            evaluator = new DateArgumentEvaluator(start, end);
            int result = evaluator.eval(this::deleteEvents);

            printer.println(result == 0 ? "\nEvent deleted" : "\nEvent entries deleted");
        } catch (NullPointerException | NoSuchElementException | IllegalArgumentException | ParseException e) {
            printer.println(e.getMessage());
        }
        return 0;
    }

    private int deleteEvents(String start, String end) {
        if (event.getToRepeat() == Event.RepeatableType.NONE || evaluator.numOfArgs() == 0) {
            new EventDeletor().execute(event);
            return 0;
        }
        new EventDeletorTF().execute(event, start, end);
        return 1;
    }

    static Options getOptions() {
        Option start = Option.builder("s")
                .required(false)
                .longOpt("start")
                .hasArg(true)
                .numberOfArgs(1)
                .optionalArg(false)
                .desc("Specify the start time from when to delete entries")
                .build();
        Option end = Option.builder("e")
                .required(false)
                .longOpt("end")
                .hasArg(true)
                .numberOfArgs(1)
                .optionalArg(false)
                .desc("Specify the end time to when to delete entries")
                .build();
        return CommandUtils.buildOptions(start, end);
    }
}
