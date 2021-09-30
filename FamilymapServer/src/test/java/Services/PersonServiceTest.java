package Services;

import DAO.*;
import Model.AuthToken;
import Model.Person;
import Requests.PersonRequest;
import Responses.PersonResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {
    private Database database;
    PersonService personService;
    private PersonRequest request;
    private PersonResponse response;
    private PersonResponse expectedResponse;
    private ArrayList<Person> personArrayList;
    private PersonDAO personDAO;
    private AuthTokenDAO authTokenDAO;
    private String tokenID = "tkn2476";

    @BeforeEach
    public void setUp() throws DatabaseException {
        database = new Database();
        database.openConnection();
        Connection conn = database.getConnection();
        personService = new PersonService();
        personArrayList = new ArrayList<>();
        personDAO = new PersonDAO(conn);
        authTokenDAO = new AuthTokenDAO(conn);
        personDAO.clearPeople();
        authTokenDAO.clearAuthTokens();

        //insert people into database
        Person newPerson = new Person();
        newPerson.setFirstName("Sheila");
        newPerson.setLastName("Parker");
        newPerson.setGender("f");
        newPerson.setPersonID("Sheila_Parker");
        newPerson.setSpouseID("Davis_Hyer");
        newPerson.setFatherID("Blaine_McGary");
        newPerson.setMotherID("Betty_White");
        newPerson.setAssociatedUsername("sheila");
        personDAO.insertPerson(newPerson);
        personArrayList.add(newPerson);

        newPerson = new Person();
        newPerson.setFirstName("Davis");
        newPerson.setLastName("Hyer");
        newPerson.setGender("m");
        newPerson.setPersonID("Davis_Hyer");
        newPerson.setSpouseID("Sheila_Parker");
        newPerson.setAssociatedUsername("sheila");
        personDAO.insertPerson(newPerson);
        personArrayList.add(newPerson);

        newPerson = new Person();
        newPerson.setFirstName("Blaine");
        newPerson.setLastName("McGary");
        newPerson.setGender("m");
        newPerson.setPersonID("Blaine_McGary");
        newPerson.setSpouseID("Betty_White");
        newPerson.setFatherID("Ken_Rodham");
        newPerson.setMotherID("Mrs_Rodham");
        newPerson.setAssociatedUsername("sheila");
        personDAO.insertPerson(newPerson);
        personArrayList.add(newPerson);

        newPerson = new Person();
        newPerson.setFirstName("Betty");
        newPerson.setLastName("White");
        newPerson.setGender("f");
        newPerson.setPersonID("Betty_White");
        newPerson.setSpouseID("Blaine_McGary");
        newPerson.setFatherID("Frank_Jones");
        newPerson.setMotherID("Mrs_Jones");
        newPerson.setAssociatedUsername("sheila");
        personDAO.insertPerson(newPerson);
        personArrayList.add(newPerson);

        request = new PersonRequest(tokenID);
    }

    @AfterEach
    public void tearDown() throws DatabaseException {
        database.closeConnection(false);
    }

    @Test
    @DisplayName("Positive person service test")
    void person() throws DatabaseException {
        AuthToken token = new AuthToken();
        token.setToken(tokenID);
        token.setUsername("sheila");
        authTokenDAO.insertAuthToken(token);

        database.closeConnection(true);
        database.openConnection();

        expectedResponse = new PersonResponse(personArrayList, true);
        response = personService.person(request);
        assertTrue(response.isSuccess());
        for (int i = 0; i < personArrayList.size(); i++) {
            assertEquals(expectedResponse.getData().get(i).getPersonID(), response.getData().get(i).getPersonID());
            assertEquals(expectedResponse.getData().get(i).getFirstName(), response.getData().get(i).getFirstName());
        }
    }

    @Test
    @DisplayName("Negative person service test")
    void personFail() throws DatabaseException {
        request.setAuthTokenID("badInput");
        response = personService.person(request);
        assertFalse(response.isSuccess());
        assertNull(response.getData());
    }
}