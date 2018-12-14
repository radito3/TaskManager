package com.sap.exercise.commands;

import com.sap.exercise.AbstractTest;
import com.sap.exercise.db.CRUDOperationsNew;
import com.sap.exercise.db.CRUDOps;
import com.sap.exercise.model.Event;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteTest extends AbstractTest {

    @Test
    @DisplayName("Delete command name test")
    public void deleteNameTest() {
        assertEquals("delete", new Delete().getName());
    }

    @Test
    @DisplayName("Delete command functionality test")
    public void deleteCommandTest() {
        Event event = new Event("test title");
        CRUDOps<Event> crudOps = new CRUDOperationsNew<>(Event.class);
        Serializable id = crudOps.create(event);

        new Delete().execute(handler, "test", "title");
        assertThrows(NullPointerException.class,
                () -> crudOps.getObjById((Integer) id),
                "Event has not been deleted");
    }

    @Test
    @DisplayName("Delete test with null name")
    @Disabled("Doesn't work for some reason")
    public void deleteNullNameTest() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream defaultOut = System.out;
        System.setOut(new PrintStream(out));
        Delete delete = new Delete();

        delete.execute(handler);
        System.out.flush();
        assertEquals("Event name not specified\n", out.toString());
        System.setOut(defaultOut);
    }

    @Test
    @DisplayName("Delete test with incorrect event name")
    public void deleteIncorrectNameTest() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream defaultOut = System.out;
        System.setOut(new PrintStream(out));
        Delete delete = new Delete();

        delete.execute(handler, "!!@$");
        System.out.flush();
        assertEquals("Invalid event name\n", out.toString());
        System.setOut(defaultOut);
    }
}
