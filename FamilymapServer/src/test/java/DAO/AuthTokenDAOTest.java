package DAO;

import Model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

class AuthTokenDAOTest {
    private Database database;
    private AuthTokenDAO authTokenDAO;
    private AuthToken testToken;

    @BeforeEach
    public void setUp() throws DatabaseException {
        database = new Database();
        database.openConnection();
        Connection conn = database.getConnection();

        testToken = new AuthToken();
        testToken.setToken(UUID.randomUUID().toString());
        testToken.setUsername("username");

        authTokenDAO = new AuthTokenDAO(conn);
        authTokenDAO.clearAuthTokens();
    }

    @AfterEach
    public void tearDown() throws DatabaseException {
        database.closeConnection(false);
    }

    @Test
    @DisplayName("Positive insertAuthToken test")
    void insertAuthTokenPass() throws DatabaseException {
        authTokenDAO.insertAuthToken(testToken);
        AuthToken compareToken = authTokenDAO.getAuthToken(testToken.getToken());
        assertNotNull(compareToken);
        assertEquals(testToken.getToken(), compareToken.getToken());
        assertEquals(testToken.getUsername(), compareToken.getUsername());
    }

    @Test
    @DisplayName("Negative insertAuthToken test")
    void insertAuthTokenFail() {
        boolean thrown = false;

        testToken.setUsername(null);
        try {
            authTokenDAO.insertAuthToken(testToken);
        } catch (DatabaseException e) {
            thrown = true;
        }
        assertTrue(thrown);

        thrown = false;

        testToken.setUsername("username");
        testToken.setToken(null);
        try {
            authTokenDAO.insertAuthToken(testToken);
        } catch (DatabaseException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    @DisplayName("Positive getAuthToken test")
    void getAuthTokenPass() throws DatabaseException {
        authTokenDAO.insertAuthToken(testToken);
        AuthToken getToken = authTokenDAO.getAuthToken(testToken.getToken());
        assertNotNull(getToken);
        assertEquals(testToken.getToken(), getToken.getToken());
        assertEquals(testToken.getUsername(), getToken.getUsername());
    }

    @Test
    @DisplayName("Negative getAuthToken test")
    void getAuthTokenFail() throws DatabaseException {
        authTokenDAO.insertAuthToken(testToken);
        AuthToken getToken = authTokenDAO.getAuthToken("badInput");
        assertNull(getToken);
    }

    @Test
    void clearAuthTokens() throws DatabaseException {
        authTokenDAO.insertAuthToken(testToken);
        authTokenDAO.clearAuthTokens();
        assertNull(authTokenDAO.getAuthToken(testToken.getToken()));
    }
}