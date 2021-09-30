package Services;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Requests.EventIdRequest;
import Requests.PersonIdRequest;
import Responses.EventIdResponse;
import Responses.PersonIdResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class PersonIdServiceTest {
    private Database database;
    private PersonIdService personIdService;
    private PersonIdRequest request;
    private PersonIdResponse response;
    private PersonDAO personDAO;
    private AuthTokenDAO authTokenDAO;
    private String personID = "Jedi789";
    private String associatedUsername = "GoldLeader";
    private String firstName = "Luke";
    private String lastName = "Skywalker";
    private String gender = "m";
    private String fatherID = "DarthVader";
    private String motherID = "PadmeAmidala";

    @BeforeEach
    public void setUp() throws DatabaseException {
        database = new Database();
        database.openConnection();
        Connection conn = database.getConnection();
        personIdService = new PersonIdService();
        request = new PersonIdRequest();
        personDAO = new PersonDAO(conn);
        authTokenDAO = new AuthTokenDAO(conn);
        personDAO.clearPeople();
        authTokenDAO.clearAuthTokens();

        Person newPerson = new Person();
        newPerson.setPersonID(personID);
        newPerson.setAssociatedUsername(associatedUsername);
        newPerson.setFirstName(firstName);
        newPerson.setLastName(lastName);
        newPerson.setGender(gender);
        newPerson.setFatherID(fatherID);
        newPerson.setMotherID(motherID);
        personDAO.insertPerson(newPerson);
        request.setPersonID(newPerson.getPersonID());
        request.setAuthTokenID("X-Wing");
    }

    @AfterEach
    public void tearDown() throws DatabaseException {
        database.closeConnection(false);
    }

    @Test
    @DisplayName("Positive personID test")
    void personID() throws DatabaseException {
        AuthToken token = new AuthToken();
        token.setToken(request.getAuthTokenID());
        token.setUsername(associatedUsername);
        authTokenDAO.insertAuthToken(token);

        database.closeConnection(true);
        database.openConnection();

        response = personIdService.personID(request);
        PersonIdResponse expectedResponse = new PersonIdResponse(true, personID, associatedUsername, firstName, lastName,gender, fatherID, motherID, null);
        assertTrue(response.isSuccess());
        assertEquals(expectedResponse.getAssociatedUsername(), response.getAssociatedUsername());
        assertEquals(expectedResponse.getPersonID(), response.getPersonID());
    }

    @Test
    @DisplayName("Negative personID test")
    void personIDFail() throws DatabaseException {
        request.setPersonID("badInput");
        response = personIdService.personID(request);
        assertFalse(response.isSuccess());
        request.setPersonID(personID);
        request.setAuthTokenID("badInput");
        response = personIdService.personID(request);
        assertFalse(response.isSuccess());
    }
}