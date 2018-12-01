package com.sap.exercise.handler;

import com.sap.exercise.AbstractTest;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.model.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CRUD operations test class")
public class CRUDOperationsTest extends AbstractTest {

    @Test
    @DisplayName("Creation and reading test")
    public void createAndReadTest() {
        User user = new User();
        Serializable id = CRUDOperations.create(user);

        assertAll("Object integrity assertions",
                () -> assertNotNull(CRUDOperations.getObjById(User.class, (Integer) id), "Object retrieved from db is null"),
                () -> assertEquals(user, CRUDOperations.getObjById(User.class, (Integer) id), "Object retrieved from db doesn't match")
        );
        CRUDOperations.delete(user);
    }

    @Test
    @DisplayName("Creation of no objects test")
    public void createNoObjTest() {
        assertDoesNotThrow(() -> CRUDOperations.create(new ArrayList<>()));
    }

    @Test
    @DisplayName("Updating test")
    public void updateTest()  {
        User user = new User();
        Serializable id = CRUDOperations.create(user);

        user.setUserName("new name");
        CRUDOperations.update(user);

        assertAll("Object integrity assertions",
                () -> assertNotNull(CRUDOperations.getObjById(User.class, (Integer) id), "Object retrieved from db is null"),
                () -> assertEquals(user, CRUDOperations.getObjById(User.class, (Integer) id), "Object retrieved from db doesn't match")
        );
        CRUDOperations.delete(user);
    }

    @Test
    @DisplayName("Deletion test")
    public void deleteTest() {
        User user = new User();
        Serializable id = CRUDOperations.create(user);

        CRUDOperations.delete(user);
        assertThrows(NullPointerException.class,
                () -> CRUDOperations.getObjById(User.class, (Integer) id),
                "Returning null from db doesn't throw exception");
    }

    @Test
    @DisplayName("Getting object identified by title test")
    public void getObjectByTitleTest() {
        Event event = new Event("test");
        CRUDOperations.create(event);

        Event event1 = CRUDOperations.getEventByTitle("test").get();
        assertNotNull(event1);
        assertEquals(event, event1);
        CRUDOperations.delete(event);
    }

    @Test
    @DisplayName("Invalid name for title test")
    public void invalidNameTest() {
        assertFalse(CRUDOperations.getEventByTitle("").isPresent(),
                "An event is returned with invalid title");
    }

    @Test
    @DisplayName("Getting object identified by id test")
    public void getObjectByIdTest() {
        User user = new User();
        Serializable id = CRUDOperations.create(user);
        user.setId((Integer) id);

        User test = CRUDOperations.getObjById(User.class, (Integer) id);
        assertEquals(user, test, "Object retrieved by Id doesn't match");
        CRUDOperations.delete(user);
    }

    @Test
    @DisplayName("Trying to get object with invalid id test")
    public void invalidIdTest() {
        assertThrows(NullPointerException.class,
                () -> CRUDOperations.getObjById(User.class, -1),
                "Getting object with invalid id doesn't throw error");
    }

    @Test
    @DisplayName("Getting events in time frame test")
    public void getEventsInTimeFrameTest() {
        Event event = new Event("for_testing");
        event.setToRepeat(Event.RepeatableType.DAILY);
        event.setTimeOf(new GregorianCalendar(2010, 0, 1));
        EventHandler.getInstance().create(event);

        List<CalendarEvents> events = CRUDOperations.getEventsInTimeFrame("2010-01-01", "2010-01-30");
        assertEquals(30, events.size(), "Events list doesn't contain all event repetitions");
        EventHandler.getInstance().delete(event);
    }

    @Test
    @DisplayName("Getting events in invalid time frame test")
    public void invalidTimeFrameGetTest() {
        assertEquals(0, CRUDOperations.getEventsInTimeFrame("2018-01-01", "2017-01-01").size(),
                "Invalid time frame returns non-empty list");
    }

    @Test
    @DisplayName("Deleting events in time frame test test")
    @Disabled("Not working yet")
    public void deleteEventsInTimeFrameTest() {
        Event event = new Event("for_testing");
        event.setToRepeat(Event.RepeatableType.DAILY);
        event.setTimeOf(new GregorianCalendar(2010, 0, 1));

        Serializable id = CRUDOperations.create(event);
        CRUDOperations.delete(event);
        EventHandler.getInstance().create(event);
        event.setId((Integer) id + 1);

        CRUDOperations.deleteEventsInTimeFrame(event, "2010-01-15", "2010-01-30");

        List<CalendarEvents> events = CRUDOperations.getEventsInTimeFrame("2010-01-01", "2010-01-30");
        assertEquals(15, events.size(), "Events list doesn't contain correct amount of event repetitions");
        EventHandler.getInstance().delete(event);
    }

    @Test
    @DisplayName("Deleting events in invalid time frame test")
    public void invalidTimeFrameDeleteTest() {
        //implement
    }
}
