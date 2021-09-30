package Services;

import DAO.Database;
import DAO.DatabaseException;
import DAO.UserDAO;
import Model.User;
import Requests.LoginRequest;
import Responses.LoginResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
class LoginServiceTest {
    private Database database;
    private LoginService loginService;
    private LoginRequest request;
    private LoginResponse response;
    private UserDAO userDAO;
    private String username = "BarneyStinson";
    private String personID = "suit83";

    @BeforeEach
    public void setUp() throws DatabaseException {
        database = new Database();
        Connection conn = database.openConnection();
        loginService = new LoginService();
        request = new LoginRequest(username, "Legendary");
        userDAO = new UserDAO(conn);
        userDAO.clearUsers();
    }

    @AfterEach
    public void tearDown() throws DatabaseException {
        database.closeConnection(false);
    }

    @Test
    @DisplayName("Positive login test")
    void login() throws DatabaseException {
        User newUser = new User();
        newUser.setUserName("BarneyStinson");
        newUser.setPassword("Legendary");
        newUser.setEmail("SuitUp!");
        newUser.setFirstName("Barney");
        newUser.setLastName("Stinson");
        newUser.setGender("m");
        newUser.setPersonID(personID);
        userDAO.insertUser(newUser);

        database.closeConnection(true);
        database.openConnection();

        response = loginService.login(request);
        assertTrue(response.isSuccess());
        assertEquals(username, response.getUserName());
        assertEquals(personID, response.getPersonID());
    }

    @Test
    @DisplayName("Negative login test")
    void loginFail() throws DatabaseException {
        response = loginService.login(request);
        assertFalse(response.isSuccess());
    }
}