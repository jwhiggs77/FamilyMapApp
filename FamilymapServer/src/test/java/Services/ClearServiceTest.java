package Services;

import DAO.Database;
import DAO.DatabaseException;
import Responses.ClearResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {
    Database database;
    ClearService clearService;
    ClearResponse response;

    @BeforeEach
    public void setUp() throws DatabaseException {
        database = new Database();
        database.openConnection();
        clearService = new ClearService();
    }

    @AfterEach
    public void tearDown() throws DatabaseException {
        database.closeConnection(false);
    }

    @Test
    @DisplayName("Clear test")
    void clear() throws DatabaseException {
        response = clearService.clear();
        assertTrue(response.isSuccess());
        assertEquals("clear succeeded", response.getMessage().toLowerCase());
    }
}