package Services;

import DAO.Database;
import DAO.DatabaseException;
import DAO.UserDAO;
import Model.User;
import Requests.RegisterRequest;
import Responses.RegisterResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {
    Database database;
    RegisterService registerService;
    RegisterRequest request;
    RegisterResponse response;
    UserDAO userDAO;
    String username = "Ironman";

    @BeforeEach
    public void setUp() throws DatabaseException {
        database = new Database();
        database.openConnection();
        Connection conn = database.getConnection();
        registerService = new RegisterService();
        request = new RegisterRequest(username, "Avengers", "StarkIndustries", "Tony", "Stark", "m");
        database.clearTables();
        userDAO = new UserDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DatabaseException {
        database.closeConnection(false);
    }

    @Test
    @DisplayName("Positive register test")
    void register() throws DatabaseException {
        database.closeConnection(true);
        response = registerService.register(request);
        assertTrue(response.isSuccess());
        assertEquals(username, response.getUserName());
        assertNotNull(response.getPersonID());
        assertNotNull(response.getAuthToken());
    }

    @Test
    @DisplayName("Negative register test")
    void registerFail() throws DatabaseException {
        request.setGender("badInput");
        response = registerService.register(request);
        assertFalse(response.isSuccess());
        request.setGender("m");

        request.setUserName(null);
        response = registerService.register(request);
        assertFalse(response.isSuccess());
        request.setUserName(username);

        User newUser = new User();
        newUser.setUserName(username);
        newUser.setPassword("Legendary");
        newUser.setEmail("SuitUp!");
        newUser.setFirstName("Barney");
        newUser.setLastName("Stinson");
        newUser.setGender("m");
        newUser.setPersonID("1234-5678");
        userDAO.insertUser(newUser);
        response = registerService.register(request);
        assertFalse(response.isSuccess());
    }
}