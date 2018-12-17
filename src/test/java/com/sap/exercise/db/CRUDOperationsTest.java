package com.sap.exercise.db;

import com.sap.exercise.AbstractTest;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.model.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("CRUD Operations tests")
public class CRUDOperationsTest extends AbstractTest {

    @Test
    @DisplayName("Creation and reading test")
    public void createAndReadTest() {
        User user = new User();
        CRUDOps<User> crudOps = new CRUDOperations<>(User.class);
        Serializable id = crudOps.create(user);

        assertAll("Object integrity assertions",
                () -> assertNotNull(crudOps.getObjById((Integer) id), "Object retrieved from db is null"),
                () -> assertEquals(user, crudOps.getObjById((Integer) id), "Object retrieved from db doesn't match")
        );
        crudOps.delete(user);
    }

    @Test
    @DisplayName("Creation of no objects test")
    public void createNoObjTest() {
        assertDoesNotThrow(() -> new CRUDOperations<>(User.class).create(new ArrayList<>()));
    }

    @Test
    @DisplayName("Updating test")
    public void updateTest()  {
        User user = new User();
        CRUDOps<User> crudOps = new CRUDOperations<>(User.class);
        Serializable id = crudOps.create(user);

        user.setUserName("new name");
        crudOps.update(user);

        assertAll("Object integrity assertions",
                () -> assertNotNull(crudOps.getObjById((Integer) id), "Object retrieved from db is null"),
                () -> assertEquals(user, crudOps.getObjById((Integer) id), "Object retrieved from db doesn't match")
        );
        crudOps.delete(user);
    }

    @Test
    @DisplayName("Deletion test")
    public void deleteTest() {
        User user = new User();
        CRUDOps<User> crudOps = new CRUDOperations<>(User.class);
        Serializable id = crudOps.create(user);

        crudOps.delete(user);
        assertThrows(NullPointerException.class,
                () -> crudOps.getObjById((Integer) id),
                "Returning null from db doesn't throw exception");
    }

    @Test
    @DisplayName("Getting object identified by title test")
    public void getObjectByTitleTest() {
        Event event = new Event("test");
        CRUDOps<Event> crudOps = new CRUDOperations<>(Event.class);
        crudOps.create(event);

        Event event1 = crudOps.getObjByProperty("Title", "test").get();
        assertNotNull(event1);
        assertEquals(event, event1);
        crudOps.delete(event);
    }

    @Test
    @DisplayName("Invalid name for title test")
    public void invalidNameTest() {
        assertFalse(new CRUDOperations<>(Event.class).getObjByProperty("Title", "").isPresent(),
                "An event is returned with invalid title");
    }

    @Test
    @DisplayName("Getting object identified by id test")
    public void getObjectByIdTest() {
        User user = new User();
        CRUDOps<User> crudOps = new CRUDOperations<>(User.class);
        Serializable id = crudOps.create(user);
        user.setId((Integer) id);

        User test = crudOps.getObjById((Integer) id);
        assertEquals(user, test, "Object retrieved by Id doesn't match");
        crudOps.delete(user);
    }

    @Test
    @DisplayName("Trying to get object with invalid id test")
    public void invalidIdTest() {
        assertThrows(NullPointerException.class,
                () -> new CRUDOperations<>(User.class).getObjById(-1),
                "Getting object with invalid id doesn't throw error");
    }

    @Test
    @DisplayName("Getting events in time frame test")
    @Disabled("Unfinished")
    public void getEventsInTimeFrameTest() {
        Event event = new Event("for_testing");
        event.setToRepeat(Event.RepeatableType.DAILY);
        event.setTimeOf(new GregorianCalendar(2010, Calendar.JANUARY, 1));
        handler.create(event);

        List<CalendarEvents> events = new CRUDOperations<>(Event.class).getEventsInTimeFrame("2010-01-01", "2010-01-30");
        assertEquals(30, events.size(), "Events list doesn't contain all event repetitions");
        handler.delete(event);
    }

    @Test
    @DisplayName("Getting events in invalid time frame test")
    public void invalidTimeFrameGetTest() {
        assertEquals(0, new CRUDOperations<>(Event.class).getEventsInTimeFrame("2018-01-01", "2017-01-01").size(),
                "Invalid time frame returns non-empty list");
    }

    @Test
    @DisplayName("Deleting events in time frame test test")
    @Disabled("Not working yet")
    public void deleteEventsInTimeFrameTest() {
        CRUDOps<Event> crudOps = new CRUDOperations<>(Event.class);
        Event event = new Event("for_testing");
        event.setToRepeat(Event.RepeatableType.DAILY);
        event.setTimeOf(new GregorianCalendar(2010, Calendar.JANUARY, 1));

        Serializable id = crudOps.create(event);
        crudOps.delete(event);
        handler.create(event);
        event.setId((Integer) id + 1); //not good

        crudOps.deleteEventsInTimeFrame(event, "2010-01-15", "2010-01-30");

        List<CalendarEvents> events = crudOps.getEventsInTimeFrame("2010-01-01", "2010-01-30");
        assertEquals(15, events.size(), "Events list doesn't contain correct amount of event repetitions");
        handler.delete(event);
    }

    @Test
    @DisplayName("Deleting events in invalid time frame test")
    public void invalidTimeFrameDeleteTest() {
        //TODO implement
    }
}
