package DAO;

import Model.Person;
import Model.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class PersonDAOTest {
    private Database database;
    private PersonDAO personDAO;
    private Person testPerson;

    @BeforeEach
    public void setUp() throws DatabaseException {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        database = new Database();
        //and a new user with random data
        testPerson = new Person();
        testPerson.setPersonID("avenger01");
        testPerson.setAssociatedUsername("Ironman");
        testPerson.setFirstName("Tony");
        testPerson.setLastName("Stark");
        testPerson.setGender("m");
        testPerson.setFatherID("howardStark1");
        testPerson.setMotherID("supermom678");
        testPerson.setSpouseID("pepperpots44");
        //Here, we'll open the connection in preparation for the test case to use
        database.openConnection();
        Connection conn = database.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
//        database.clearTables();
        //Then we pass that connection to the UserDAO so it can access the database
        personDAO = new PersonDAO(conn);
        personDAO.clearPeople();
    }

    @AfterEach
    public void tearDown() throws DatabaseException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        database.closeConnection(false);
    }

    @Test
    @DisplayName("Positive insertPerson test")
    void insertPersonPass() throws DatabaseException {
        personDAO.insertPerson(testPerson);
        Person comparePerson = personDAO.getPerson(testPerson.getPersonID());
        assertNotNull(comparePerson);
        assertEquals(comparePerson.getPersonID(), testPerson.getPersonID());
    }

    @Test
    @DisplayName("Negative insertPerson test")
    void insertPersonFail() {
        boolean thrown = false;

        testPerson.setGender("badInput");
        try {
            personDAO.insertPerson(testPerson);
        } catch (DatabaseException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    @DisplayName("Positive getPerson test")
    void getPersonPass() throws DatabaseException {
        personDAO.insertPerson(testPerson);
        Person getPerson = personDAO.getPerson(testPerson.getPersonID());
        assertNotNull(getPerson);
        assertEquals(testPerson.getPersonID(), getPerson.getPersonID());
    }

    @Test
    @DisplayName("Negative getPerson test")
    void getPersonFail() throws DatabaseException {
        personDAO.insertPerson(testPerson);
        Person getPerson = personDAO.getPerson("badInput");
        assertNull(getPerson);
    }

    @Test
    @DisplayName("Clear test")
    void clearPeople() throws DatabaseException {
        personDAO.insertPerson(testPerson);
        personDAO.clearPeople();
        assertNull(personDAO.getPerson(testPerson.getPersonID()));
    }
}