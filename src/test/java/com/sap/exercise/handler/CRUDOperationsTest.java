package com.sap.exercise.handler;

import com.sap.exercise.AbstractTest;
import com.sap.exercise.model.Event;
import com.sap.exercise.model.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CRUD operations test class")
public class CRUDOperationsTest extends AbstractTest {

    @Test
    @DisplayName("Creation and reading test")
    public void createAndReadTest() {
        User user = new User();
        CRUDOperations.create(user);

        assertAll("Object integrity assertions",
                () -> assertNotNull(CRUDOperations.getObject(user), "Object retrieved from db is null"),
                () -> assertEquals(user, CRUDOperations.getObject(user), "Object retrieved from db doesn't match")
        );
    }

    @Test
    @DisplayName("Updating test")
    public void updateTest()  {
        User user = new User();
        CRUDOperations.create(user);

        user.setName("new name");
        CRUDOperations.update(user);

        assertAll("Object integrity assertions",
                () -> assertNotNull(CRUDOperations.getObject(user), "Object retrieved from db is null"),
                () -> assertEquals(user, CRUDOperations.getObject(user), "Object retrieved from db doesn't match")
        );
    }

    @Test
    @DisplayName("Deletion test")
    public void deleteTest() {
        User user = new User();
        CRUDOperations.create(user);

        CRUDOperations.delete(user);
        assertThrows(NullPointerException.class,
                () -> CRUDOperations.getObject(user),
                "Returning null from db doesn't throw exception");
    }

    @Test
    @DisplayName("Getting object identified by title test")
    @Disabled("Event model 'equals' method doesn't work")
    public void getObjectByTitleTest() {
        Event event = new Event("test");
        CRUDOperations.create(event);

        Event event1 = CRUDOperations.getObjectFromTitle("test");
        assertNotNull(event1);
        assertEquals(event, event1);
    }

    @Test
    @DisplayName("Invalid name for title test")
    public void invalidNameTest() {
        assertThrows(NullPointerException.class,
                () -> CRUDOperations.getObjectFromTitle(""),
                "Incorrect or no exception thrown with invalid title");
    }

}
