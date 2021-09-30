package Services;

import DAO.AuthTokenDAO;
import DAO.Database;
import DAO.DatabaseException;
import DAO.EventDAO;
import Model.AuthToken;
import Model.Event;
import Requests.EventIdRequest;
import Responses.EventIdResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class EventIdServiceTest {
    private Database database;
    private EventIdService eventIdService;
    private EventIdRequest request;
    private EventIdResponse response;
    private EventDAO eventDAO;
    private AuthTokenDAO authTokenDAO;
    private String username = "LukeSkywalker";
    private String eventID = "GoldLeader";
    private String personID = "Jedi789";
    private double latitude = 320.5;
    private double longitude = 469.7;
    private String country = "Space";
    private String city = "Endor";
    private String eventType = "Battle";
    private int year = 1988;

    @BeforeEach
    public void setUp() throws DatabaseException {
        database = new Database();
        database.openConnection();
        Connection conn = database.getConnection();
        eventIdService = new EventIdService();
        request = new EventIdRequest();
        eventDAO = new EventDAO(conn);
        authTokenDAO = new AuthTokenDAO(conn);
        eventDAO.clearEvents();
        authTokenDAO.clearAuthTokens();

        Event newEvent = new Event();
        newEvent.setEventID(eventID);
        newEvent.setAssociatedUsername(username);
        newEvent.setPersonID(personID);
        newEvent.setLatitude(latitude);
        newEvent.setLongitude(longitude);
        newEvent.setCountry(country);
        newEvent.setCity(city);
        newEvent.setEventType(eventType);
        newEvent.setYear(year);
        eventDAO.insertEvent(newEvent);
        request.setEventID(newEvent.getEventID());
        request.setAuthTokenID("DeathStar");
    }

    @AfterEach
    public void tearDown() throws DatabaseException {
        database.closeConnection(false);
    }

    @Test
    @DisplayName("Positive eventID service test")
    void eventID() throws DatabaseException {
        AuthToken token = new AuthToken();
        token.setToken(request.getAuthTokenID());
        token.setUsername(username);
        authTokenDAO.insertAuthToken(token);

        database.closeConnection(true);
        database.openConnection();

        response = eventIdService.eventID(request);
        EventIdResponse expectedResponse = new EventIdResponse(true, username, eventID, personID, latitude, longitude, country, city, eventType, year);
        assertTrue(response.isSuccess());
        assertEquals(expectedResponse.getEventID(), response.getEventID());
        assertEquals(expectedResponse.getAssociatedUsername(), response.getAssociatedUsername());
        assertEquals(expectedResponse.getPersonID(), response.getPersonID());
    }

    @Test
    void eventIDFail() throws DatabaseException {


        request.setEventID("badInput");
        response = eventIdService.eventID(request);
        assertFalse(response.isSuccess());
        request.setEventID(eventID);

        request.setAuthTokenID("badInput");
        response = eventIdService.eventID(request);
        assertFalse(response.isSuccess());
    }
}