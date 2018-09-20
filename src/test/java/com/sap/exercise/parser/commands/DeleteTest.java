package com.sap.exercise.parser.commands;

import com.sap.exercise.AbstractTest;
import com.sap.exercise.handler.EventsHandler;
import com.sap.exercise.model.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.condition.EnabledIf;

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
        EventsHandler.create(event);

        new Delete().execute("delete", "test", "title");
        assertThrows(NullPointerException.class, () -> EventsHandler.getObject(event), "Event has not been deleted");
    }

//    @Test
//    @DisplayName("Delete test with null name")
//    @EnabledIf({"com.sap.exercise.Main.OUTPUT == System.out"}) //incorrect script
//    public void deleteNullNameTest() {
//
//    }
//
//    @Test
//    @DisplayName("Delete test with incorrect event name")
//    public void deleteIncorrectNameTest() {
//
//    }
}
