package Services;

import DAO.Database;
import DAO.DatabaseException;
import DAO.UserDAO;
import Model.Event;
import Model.Person;
import Model.User;
import Requests.FillRequest;
import Requests.LoadRequest;
import Responses.FillResponse;
import Responses.LoadResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LoadServiceTest {
    private Database database;
    private LoadService loadService;
    private LoadRequest request;
    private LoadResponse response;
    private User[] users;
    private Person[] persons;
    private Event[] events;

    @BeforeEach
    public void setUp() throws DatabaseException {
        database = new Database();
        database.openConnection();
        loadService = new LoadService();
        Gson gson = new Gson();

        try {
            Reader LoadData = new FileReader("/Users/joshuahiggins/Downloads/FamilyMapServerStudent-master/passoffFiles/LoadData.json");
            JsonObject LoadDataJson = gson.fromJson(LoadData, JsonObject.class);
            users = gson.fromJson(LoadDataJson.get("users"), User[].class);
            persons = gson.fromJson(LoadDataJson.get("persons"), Person[].class);
            events = gson.fromJson(LoadDataJson.get("events"), Event[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<User> userArrayList = new ArrayList<>();
        ArrayList<Person> personArrayList = new ArrayList<>();
        ArrayList<Event> eventArrayList = new ArrayList<>();
        for (int i = 0; i < users.length; i++) {
            userArrayList.add(users[i]);
        }
        for (int i = 0; i < persons.length; i++) {
            personArrayList.add(persons[i]);
        }
        for (int i = 0; i < events.length; i++) {
            eventArrayList.add(events[i]);
        }
        request = new LoadRequest(userArrayList, personArrayList, eventArrayList);
    }

    @AfterEach
    public void tearDown() throws DatabaseException {
        database.closeConnection(false);
    }

    @Test
    @DisplayName("Positive load test")
    void load() throws DatabaseException {
        response = loadService.load(request);
        assertTrue(response.isSuccess());
        assertEquals("Successfully added " + request.getUsers().size() + " users, " + request.getPersons().size() + " persons, and " + request.getEvents().size() + " events to the database.", response.getMessage());
    }

    @Test
    @DisplayName("Negative load test")
    void loadFail() throws DatabaseException {
        request.setUsers(null);
        response = loadService.load(request);
        assertFalse(response.isSuccess());
    }
}