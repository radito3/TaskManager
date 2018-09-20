package com.sap.exercise.parser.commands;

import com.sap.exercise.handler.EventsHandler;
import com.sap.exercise.model.Event;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.sap.exercise.Main.INPUT;

public class Edit implements Command {

    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public void execute(String... args) {
        try {
            String name = buildEventName(args);
            Event event = EventsHandler.getObjectFromTitle(name);

            edit(event);

            printer.println("end of execute method");

//            EventsHandler.update(event);
//            printer.println("Event updated");
        } catch (NullPointerException npe) {
            printer.println("Invalid event name"); //these static strings could be set methods in OutputPrinter
        } catch (IllegalArgumentException iae) {
            printer.println("Event name not specified");
        }
    }

    private String buildEventName(String[] input) {
        if (input.length == 1) throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder(input[1]);
        for (int i = 2; i < input.length; i++) sb.append(' ').append(input[i]);
        return sb.toString();
    }

    private void edit(Event event) {
        //need to filter them based on type of event
        Set<String> fields = getFields();

        try (Scanner scanner = new Scanner(INPUT)) {
            for (String field : fields) {
                printer.print(field + ": ");
                String input = scanner.nextLine();

                if (input.equals("")) continue;

                printer.println(input);
//                Method method = Event.class.getDeclaredMethod("set" + field, Boolean.class);
//                method.invoke(event, Boolean.valueOf(input));

                printer.println(event.toString());
            }
        }/* catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            printer.println(ex.getLocalizedMessage());
        }*/

        printer.println("end of edit method");
    }

    private Set<String> getFields() {
        return Arrays.stream(Event.class.getDeclaredFields())
                .map(Field::getName)
                .filter(name -> !name.equals("id"))
                .map(name -> {
                    char[] chars = name.toCharArray();
                    chars[0] = Character.toUpperCase(chars[0]);
                    return new String(chars);
                })
                .collect(Collectors.toSet());
    }

    private Map<String, Method> getMethods() {
        //need to make it interactive ->
        //for it to know what type of input to expect for what type of method

        return new HashMap<>();
    }

}
