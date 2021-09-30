package DAO;

import Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDAOTest {
    private Database database;
    private UserDAO userDAO;
    private User testUser;

    @BeforeEach
    public void setUp() throws DatabaseException {
        database = new Database();
        database.openConnection();
        Connection conn = database.getConnection();

        testUser = new User();
        testUser.setUserName("Batman");
        testUser.setPassword("batcave");
        testUser.setEmail("imbatman@me.com");
        testUser.setFirstName("Bruce");
        testUser.setLastName("Wayne");
        testUser.setGender("m");
        testUser.setPersonID("bman1234");

        userDAO = new UserDAO(conn);
        userDAO.clearUsers();
    }

    @AfterEach
    public void tearDown() throws DatabaseException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        database.closeConnection(false);
    }

    @Test
    @DisplayName("Positive insertUser test")
    void insertUserPass() throws DatabaseException, SQLException {
        userDAO.insertUser(testUser);
        User compareTest = userDAO.getUser(testUser.getUserName());
        assertNotNull(compareTest);
        assertEquals(compareTest.getUserName(), testUser.getUserName());
    }

    @Test
    @DisplayName("Negative insertUser test")
    void insertUserFail() throws DatabaseException, SQLException {
        boolean thrown = false;

        testUser.setGender("badInput");
        try {
            userDAO.insertUser(testUser);
        } catch (DatabaseException e) {
            thrown = true;
        }
        assertTrue(thrown);

        thrown = false;
        testUser.setUserName(null);
        testUser.setGender("m");
        try {
            userDAO.insertUser(testUser);
        } catch (DatabaseException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    @DisplayName("Positive getUser test")
    void getUserPass() throws DatabaseException, SQLException {
        userDAO.insertUser(testUser);
        User getUser = userDAO.getUser(testUser.getUserName());
        assertNotNull(getUser);
        assertEquals(testUser.getUserName(), getUser.getUserName());
        assertEquals(testUser.getPersonID(), getUser.getPersonID());
    }

    @Test
    @DisplayName("Negative getUser test")
    void getUserFail() throws DatabaseException, SQLException {
        userDAO.insertUser(testUser);
        User getUser = userDAO.getUser("badInput");
        assertNull(getUser);
    }

    @Test
    @DisplayName("Clear Test for UserDAO")
    void clearUsers() throws DatabaseException, SQLException {
        userDAO.insertUser(testUser);
        userDAO.clearUsers();
        assertNull(userDAO.getUser(testUser.getUserName()));
    }
}