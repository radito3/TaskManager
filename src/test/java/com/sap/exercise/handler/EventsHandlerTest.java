package com.sap.exercise.handler;

import com.sap.exercise.model.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CRUD operations test class")
public class EventsHandlerTest {

    @Test
    @DisplayName("Creation and reading test")
    public void createAndReadTest() {
        Event event = new Event();
        EventsHandler.create(event);

        assertAll("Object integrity assertions",
                () -> assertNotNull(EventsHandler.getObject(event), "Object retrieved from db is null"),
                () -> assertEquals(event, EventsHandler.getObject(event), "Object retrieved from db doesn't match")
        );
    }

    @Test
    @DisplayName("Updating test")
    public void updateTest()  {
        Event event = new Event();
        EventsHandler.create(event);

        event.setTitle("new title");
        EventsHandler.update(event);

        assertAll("Object integrity assertions",
                () -> assertNotNull(EventsHandler.getObject(event), "Object retrieved from db is null"),
                () -> assertEquals(event, EventsHandler.getObject(event), "Object retrieved from db doesn't match")
        );
    }

    @Test
    @DisplayName("Deletion test")
    public void deleteTest() {
        Event event = new Event();
        EventsHandler.create(event);

        EventsHandler.delete(event);
        assertThrows(NullPointerException.class,
                () -> EventsHandler.getObject(event),
                "Returning null from db doesn't throw exception");
    }

}
