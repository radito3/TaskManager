package com.sap.exercise.handler;

import com.sap.exercise.model.Task;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class EventsHandlerTest {

    //TODO add db events handler tests

    private static ByteArrayOutputStream out;

    @BeforeAll
    public static void setup() {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @AfterAll
    public static void onEnd() {
        System.setOut(System.out);
    }

    @AfterEach
    public void after() {
        out.reset();
    }

    @Test
    @DisplayName("hacker implementation test")
    public void test() throws IOException {
        Task task = new Task();
        EventsHandler.create(task);

        out.write(task.toString().getBytes());

        assertEquals(out.toString(), EventsHandler.getObject(task).toString(), "hell yeah");
    }

    /*
    @Test
    @DisplayName("Database entry creation test")
    public void dbCreationTest() throws IOException {
        task.create();

        assertNotNull(task.getTask());

        out.write(task.toString().getBytes());
        assertEquals(task.getTask().toString(), out.toString());
    }

    @Test
    @DisplayName("Database entry updating with title only test")
    public void updateWithTitleTest() throws IOException {
        task.create();

        task.update("new title");

        out.write(task.toString().getBytes());
        assertEquals(task.getTask().toString(), out.toString());
    }

    @Test
    @DisplayName("Database entry updating with title and body test")
    public void updateWithTitleAndBodyTest() throws IOException {
        task.create();

        task.update("new title", "new body");

        out.write(task.toString().getBytes());
        assertEquals(task.getTask().toString(), out.toString());
    }

    @Test
    @DisplayName("Database entry updating with invalid input test")
    public void updateWithInvalidInputTest() {
        task.create();

        assertThrows(IllegalArgumentException.class, () -> task.update(), "Wrong amount of arguments");
    }

    @Test
    @DisplayName("Database entry deletion test")
    public void deletionTest() {
        task.create();

        task.delete();

        assertNull(task.getTask());
    }
    */
}
