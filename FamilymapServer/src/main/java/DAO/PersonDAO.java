package DAO;

import Model.Person;

import java.sql.*;
import java.util.ArrayList;

public class PersonDAO {

    private Connection conn;

    public PersonDAO(Connection connection) {
        conn = connection;
    }

    /**
     * takes in a person object and adds it to the database
     @param newPerson person to be inserted
     @throws DatabaseException throws if fails to insert the person
     */
    public void insertPerson(Person newPerson) throws DatabaseException {
        String insertSQL = "INSERT INTO Persons (PersonID, AssociatedUsername, FirstName, LastName, Gender, FatherID, MotherID, SpouseID) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
            if (newPerson.getGender().equals("m") == false && newPerson.getGender().equals("f") == false) {
                throw new DatabaseException("Gender is invalid");
            }

            preparedStatement.setString(1, newPerson.getPersonID());
            preparedStatement.setString(2, newPerson.getAssociatedUsername());
            preparedStatement.setString(3, newPerson.getFirstName());
            preparedStatement.setString(4, newPerson.getLastName());
            preparedStatement.setString(5, newPerson.getGender());
            preparedStatement.setString(6, newPerson.getFatherID());
            preparedStatement.setString(7, newPerson.getMotherID());
            preparedStatement.setString(8, newPerson.getSpouseID());

            if (preparedStatement.executeUpdate() != 1) {
                throw new DatabaseException("Error: Failed to insert newPerson");
            }
        }
        catch (SQLException throwables) {
            throw new DatabaseException("Error: Missing information for Person object");
        }
    }

    /**
     * gets all family members of a person
     * @param username username of user
     * @return array of person objects
     * @throws DatabaseException throws if fails to get family members
     */
    public ArrayList<Person> getFamilyMembers(String username) throws DatabaseException {
        ArrayList<Person> familyMembers = new ArrayList<>();
        String getSQL = "SELECT * FROM Persons WHERE AssociatedUsername = '" + username + "'";

        try (PreparedStatement preparedStatement = conn.prepareStatement(getSQL);){
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                Person person = new Person();
                person.setPersonID(result.getString(1));
                person.setAssociatedUsername(result.getString(2));
                person.setFirstName(result.getString(3));
                person.setLastName(result.getString(4));
                person.setGender(result.getString(5));
                person.setFatherID(result.getString(6));
                person.setMotherID(result.getString(7));
                person.setSpouseID(result.getString(8));
                familyMembers.add(person);
            }
        } catch (SQLException throwables) {
            throw new DatabaseException("getFamilyMembers has SQL exception");
        }

        return familyMembers;
    }

    /**
     * Updates the father of a person
     * @param personID ID of the child
     * @param fatherID ID of the father
     * @throws DatabaseException throws if fails to update
     */
    public void addFather(String personID, String fatherID) throws DatabaseException {
        String addSQL = "UPDATE Persons SET FatherID = '" + fatherID + "' WHERE PersonID = '" + personID + "'";

        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate(addSQL);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DatabaseException("Failed to add father");
        }
    }

    /**
     * Updates the mother of a person
     * @param personID ID of the child
     * @param motherID ID of the mother
     * @throws DatabaseException throws if fails to update
     */
    public void addMother(String personID, String motherID) throws DatabaseException {
        String addSQL = "UPDATE Persons SET MotherID = '" + motherID + "' WHERE PersonID = '" + personID + "'";

        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate(addSQL);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DatabaseException("Failed to add mother");
        }
    }

    /**
     * gets a single person object
     @param personID ID of the person to retrieve
     @throws DatabaseException throws if fails to retrieve person
     @return person object from database
     */
    public Person getPerson(String personID) throws DatabaseException {
        String getSQL = "SELECT * FROM Persons WHERE PersonID = '" + personID + "'";
        Person person = null;

        try (PreparedStatement preparedStatement = conn.prepareStatement(getSQL)){
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                person = new Person();
                person.setPersonID(result.getString(1));
                person.setAssociatedUsername(result.getString(2));
                person.setFirstName(result.getString(3));
                person.setLastName(result.getString(4));
                person.setGender(result.getString(5));
                person.setFatherID(result.getString(6));
                person.setMotherID(result.getString(7));
                person.setSpouseID(result.getString(8));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DatabaseException("Error: Failed to retrieve person");
        }

        return person;
    }

    /**
     * Clears all data that is connected to a specified username
     * @param username username of specified user
     * @param personID personIF of specified user
     * @throws DatabaseException throws if failed to clear
     */
    public void clearAssociationData(String username, String personID) throws DatabaseException {
        try {
            Statement statement = conn.createStatement();
            String sqlDeletePersons = "DELETE FROM Persons WHERE AssociatedUsername = '" + username + "' AND NOT PersonID = '" + personID + "'";
            statement.executeUpdate(sqlDeletePersons);
            String sqlDeleteEvents = "DELETE FROM Events WHERE AssociatedUsername = '" + username + "' AND NOT PersonID = '" + personID + "'";
            statement.executeUpdate(sqlDeleteEvents);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DatabaseException("Error: Failed to clear table");
        }
    }

    /**
     * clears all people from the database
     * @throws DatabaseException if fails to clear
     */
    public void clearPeople() throws DatabaseException {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM Persons");
            //close statement
            statement.close();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DatabaseException("Error: Failed to clear table");
        }
    }
}
