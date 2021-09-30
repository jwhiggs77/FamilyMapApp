package Services;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Requests.EventRequest;
import Responses.EventResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EventServiceTest {
    private Database database;
    EventService eventService;
    private EventRequest request;
    private EventResponse response;
    private ArrayList<Event> eventArrayList;
    private EventDAO eventDAO;
    private AuthTokenDAO authTokenDAO;
    private String tokenID = "tkn2476";

    @BeforeEach
    public void setUp() throws DatabaseException {
        database = new Database();
        database.openConnection();
        Connection conn = database.getConnection();
        eventService = new EventService();
        eventArrayList = new ArrayList<>();
        eventDAO = new EventDAO(conn);
        authTokenDAO = new AuthTokenDAO(conn);
        eventDAO.clearEvents();
        authTokenDAO.clearAuthTokens();

        //insert Events into database
        Event newEvent = new Event();
        newEvent.setEventType("birth");
        newEvent.setPersonID("Sheila_Parker");
        newEvent.setCity("Melbourne");
        newEvent.setCountry("Australia");
        newEvent.setLatitude(-36.1833);
        newEvent.setLongitude(144.9667);
        newEvent.setYear(1970);
        newEvent.setEventID("Sheila_Birth");
        newEvent.setAssociatedUsername("sheila");
        eventArrayList.add(newEvent);
        eventDAO.insertEvent(newEvent);

        newEvent = new Event();
        newEvent.setEventType("marriage");
        newEvent.setPersonID("Sheila_Parker");
        newEvent.setCity("Los Angeles");
        newEvent.setCountry("United States");
        newEvent.setLatitude(-34.0500);
        newEvent.setLongitude(117.7500);
        newEvent.setYear(1970);
        newEvent.setEventID("Sheila_Marriage");
        newEvent.setAssociatedUsername("sheila");
        eventArrayList.add(newEvent);
        eventDAO.insertEvent(newEvent);

        newEvent = new Event();
        newEvent.setEventType("completed asteroids");
        newEvent.setPersonID("Sheila_Parker");
        newEvent.setCity("Qaanaaq");
        newEvent.setCountry("Denmark");
        newEvent.setLatitude(77.4667);
        newEvent.setLongitude(68.7667);
        newEvent.setYear(1970);
        newEvent.setEventID("Sheila_Asteroids");
        newEvent.setAssociatedUsername("sheila");
        eventArrayList.add(newEvent);
        eventDAO.insertEvent(newEvent);

        newEvent = new Event();
        newEvent.setEventType("COMPLETED ASTEROIDS");
        newEvent.setPersonID("Sheila_Parker");
        newEvent.setCity("Qaanaaq");
        newEvent.setCountry("Denmark");
        newEvent.setLatitude(74.4667);
        newEvent.setLongitude(-60.7667);
        newEvent.setYear(2014);
        newEvent.setEventID("Other_Asteroids");
        newEvent.setAssociatedUsername("sheila");
        eventArrayList.add(newEvent);
        eventDAO.insertEvent(newEvent);

        newEvent = new Event();
        newEvent.setEventType("death");
        newEvent.setPersonID("Sheila_Parker");
        newEvent.setCity("Provo");
        newEvent.setCountry("United States");
        newEvent.setLatitude(40.2444);
        newEvent.setLongitude(111.6608);
        newEvent.setYear(2015);
        newEvent.setEventID("Sheila_Death");
        newEvent.setAssociatedUsername("sheila");
        eventArrayList.add(newEvent);
        eventDAO.insertEvent(newEvent);

        request = new EventRequest(tokenID);
    }

    @AfterEach
    public void tearDown() throws DatabaseException {
        database.closeConnection(false);
    }

    @Test
    @DisplayName("Positive event service test")
    void event() throws DatabaseException {
        AuthToken token = new AuthToken();
        token.setToken(tokenID);
        token.setUsername("sheila");
        authTokenDAO.insertAuthToken(token);
        database.closeConnection(true);
        database.openConnection();
        EventResponse expectedResponse = new EventResponse(eventArrayList,true);
        response = eventService.event(request);
        assertTrue(response.isSuccess());
        for (int i = 0; i < eventArrayList.size(); i++) {
            assertEquals(expectedResponse.getData().get(i).getEventID(), response.getData().get(i).getEventID());
            assertEquals(expectedResponse.getData().get(i).getPersonID(), response.getData().get(i).getPersonID());
        }
    }

    @Test
    @DisplayName("Negative event service test")
    void eventFail() throws DatabaseException {
        request.setAuthTokenID("badInput");
        response = eventService.event(request);
        assertFalse(response.isSuccess());
        assertNull(response.getData());
    }
}