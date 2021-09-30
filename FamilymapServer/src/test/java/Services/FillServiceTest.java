package Services;

import DAO.*;
import Model.Person;
import Model.User;
import Requests.FillRequest;
import Responses.FillResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class FillServiceTest {
    private Database database;
    FillService fillService;
    FillRequest request;
    FillResponse response;
    UserDAO userDAO;
    PersonDAO personDAO;
    String username = "LightSpeed";

    @BeforeEach
    public void setUp() throws DatabaseException {
        database = new Database();
        Connection conn = database.openConnection();
        fillService = new FillService();
        userDAO = new UserDAO(conn);
        personDAO = new PersonDAO(conn);
        request = new FillRequest(username, 2);
        database.clearTables();
    }

    @AfterEach
    public void tearDown() throws DatabaseException {
        database.closeConnection(false);
    }

    @Test
    @DisplayName("Positive fill test")
    void fill() throws DatabaseException {
        User user = new User();
        user.setUserName(username);
        user.setPassword("HanShotFirst");
        user.setEmail("MillenniumFalcon");
        user.setFirstName("Han");
        user.setLastName("Solo");
        user.setGender("m");
        user.setPersonID("blaster234");
        userDAO.insertUser(user);

        Person person = new Person();
        person.setPersonID(user.getPersonID());
        person.setAssociatedUsername(user.getUserName());
        person.setFirstName(user.getFirstName());
        person.setLastName(user.getLastName());
        person.setGender(user.getGender());
        personDAO.insertPerson(person);

        database.closeConnection(true);
        database.openConnection();

        response = fillService.Fill(request);
        assertTrue(response.isSuccess());
    }

    @Test
    @DisplayName("Negative fill test")
    void fillFail() throws DatabaseException {
        request.setUsername("badInput");
        response = fillService.Fill(request);
        assertFalse(response.isSuccess());
    }
}