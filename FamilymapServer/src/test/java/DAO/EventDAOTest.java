package DAO;

import Model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventDAOTest {
    private Database database;
    private Event testEvent;
    private EventDAO eventDAO;

    @BeforeEach
    public void setUp() throws DatabaseException {
        database = new Database();

        testEvent = new Event();
        testEvent.setEventID("1234-4321");
        testEvent.setAssociatedUsername("username");
        testEvent.setPersonID("Gale123A");
        testEvent.setLatitude(35.9f);
        testEvent.setLongitude(140.1f);
        testEvent.setCountry("Japan");
        testEvent.setCity("Ushiku");
        testEvent.setEventType("Biking_Around");
        testEvent.setYear(2016);
        database.openConnection();
        Connection conn = database.getConnection();
        eventDAO = new EventDAO(conn);
        eventDAO.clearEvents();
    }

    @AfterEach
    public void tearDown() throws DatabaseException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        database.closeConnection(false);
    }

    @Test
    @DisplayName("Positive insertEvent test")
    public void insertEventPass() throws DatabaseException {
        eventDAO.insertEvent(testEvent);
        Event compareTest = eventDAO.getEvent(testEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(testEvent.getEventID(), compareTest.getEventID());
    }

    @Test
    @DisplayName("Negative insertEvent test")
    public void insertEventFail() throws DatabaseException {
        boolean thrown = false;

        testEvent.setAssociatedUsername(null);
        try {
            eventDAO.insertEvent(testEvent);
        } catch (DatabaseException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    @DisplayName("Positive getEvent test")
    void getEventPass() throws DatabaseException {
        eventDAO.insertEvent(testEvent);
        Event getEvent = eventDAO.getEvent(testEvent.getEventID());
        assertNotNull(getEvent);
        assertEquals(testEvent.getEventID(), getEvent.getEventID());
    }

    @Test
    @DisplayName("Negative getEvent test")
    void getEventFail() throws DatabaseException {
        eventDAO.insertEvent(testEvent);
        Event getEvent = eventDAO.getEvent("badInput");
        assertNull(getEvent);
    }

    @Test
    @DisplayName("Positive getAllEvents test")
    void getAllEventsPass() throws DatabaseException {
        ArrayList<Event> events = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            Event newEvent = new Event();
            newEvent.setEventID(UUID.randomUUID().toString());
            newEvent.setAssociatedUsername("getAllEventsUser");
            newEvent.setPersonID(UUID.randomUUID().toString());
            newEvent.setLatitude(random.nextDouble());
            newEvent.setLongitude(random.nextDouble());
            newEvent.setCountry("Provo");
            newEvent.setCity("Utah");
            newEvent.setEventType("marriage");
            newEvent.setYear(random.nextInt());
            events.add(newEvent);
            eventDAO.insertEvent(newEvent);
        }

        ArrayList<Event> compareEvents = eventDAO.getAllEvents("getAllEventsUser");
        assertEquals(5, compareEvents.size());
        for (int i = 0; i < compareEvents.size(); i++) {
            assertEquals(events.get(i).getEventID(), compareEvents.get(i).getEventID());
            assertEquals(events.get(i).getAssociatedUsername(), compareEvents.get(i).getAssociatedUsername());
            assertEquals(events.get(i).getPersonID(), compareEvents.get(i).getPersonID());
            assertEquals(events.get(i).getYear(), compareEvents.get(i).getYear());

        }
    }

    @Test
    @DisplayName("Positive getAllEvents test")
    void getAllEventsFail() throws DatabaseException {
        ArrayList<Event> events = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            Event newEvent = new Event();
            newEvent.setEventID(UUID.randomUUID().toString());
            newEvent.setAssociatedUsername("getAllEventsUser");
            newEvent.setPersonID(UUID.randomUUID().toString());
            newEvent.setLatitude(random.nextDouble());
            newEvent.setLongitude(random.nextDouble());
            newEvent.setCountry("Provo");
            newEvent.setCity("Utah");
            newEvent.setEventType("marriage");
            newEvent.setYear(random.nextInt());
            events.add(newEvent);
            eventDAO.insertEvent(newEvent);
        }

        ArrayList<Event> compareEvents = eventDAO.getAllEvents("getAllEventsUser");
        assertEquals(5, compareEvents.size());
        for (int i = 0; i < compareEvents.size(); i++) {
            assertEquals(events.get(i).getEventID(), compareEvents.get(i).getEventID());
            assertEquals(events.get(i).getAssociatedUsername(), compareEvents.get(i).getAssociatedUsername());
            assertEquals(events.get(i).getPersonID(), compareEvents.get(i).getPersonID());
            assertEquals(events.get(i).getYear(), compareEvents.get(i).getYear());

        }
    }

    @Test
    @DisplayName("Positive getEventYear test")
    void getEventYearPass() throws DatabaseException {
        eventDAO.insertEvent(testEvent);
        int year = eventDAO.getEventYear(testEvent.getPersonID(), testEvent.getEventType());
        assertNotNull(year);
        assertEquals(testEvent.getYear(), year);
    }

    @Test
    @DisplayName("Negative getEventYear test")
    void getEventYearFail() throws DatabaseException {
        boolean thrown= false;
        int year = 0;
        eventDAO.insertEvent(testEvent);

        try {
            year = eventDAO.getEventYear(testEvent.getPersonID(), null);
        } catch (DatabaseException e) {
            thrown = true;
            e.printStackTrace();
        }
        assertNotEquals(testEvent.getYear(), year);
        assertTrue(thrown);
        try {
            year = eventDAO.getEventYear("badInput", testEvent.getEventType());
        } catch (DatabaseException e) {
            thrown = true;
            e.printStackTrace();
        }
        assertNotEquals(testEvent.getYear(), year);
        assertTrue(thrown);
    }

    @Test
    @DisplayName("Clear test")
    void clearEvents() throws DatabaseException {
        eventDAO.insertEvent(testEvent);
        eventDAO.clearEvents();
        assertNull(eventDAO.getEvent(testEvent.getEventID()));
    }
}