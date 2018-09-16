package com.sap.exercise.handler;

import com.sap.exercise.model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CRUD operations test class")
public class EventsHandlerTest {

    @Test
    @DisplayName("Creation and reading test")
    public void createAndReadTest() {
        Task task = new Task();
        EventsHandler.create(task);

        assertAll("Object integrity assertions",
                () -> assertNotNull(EventsHandler.getObject(task), "Object retrieved from db is null"),
                () -> assertEquals(task, EventsHandler.getObject(task), "Object retrieved from db doesn't match")
        );
    }

    @Test
    @DisplayName("Updating test")
    public void updateTest()  {
        Task task = new Task();
        EventsHandler.create(task);

        task.setTitle("new title");
        task.setBody("new body");
        EventsHandler.update(task);

        assertAll("Object integrity assertions",
                () -> assertNotNull(EventsHandler.getObject(task), "Object retrieved from db is null"),
                () -> assertEquals(task, EventsHandler.getObject(task), "Object retrieved from db doesn't match")
        );
    }

    @Test
    @DisplayName("Deletion test")
    public void deleteTest() {
        Task task = new Task();
        EventsHandler.create(task);

        EventsHandler.delete(task);
        assertThrows(NullPointerException.class,
                () -> EventsHandler.getObject(task),
                "Returning null from db doesn't throw exception");
    }

}
