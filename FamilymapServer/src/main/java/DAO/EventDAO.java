package DAO;

import Model.Event;

import java.sql.*;
import java.util.ArrayList;

public class EventDAO {
    Connection conn;

    public EventDAO(Connection connection) {
        conn = connection;
    }

    /**
     * inserts a new event into the database
     * @param newEvent the event to be inserted
     * @throws DatabaseException throws if missing information or if fails to insert
     */
    public void insertEvent(Event newEvent) throws DatabaseException {//        PreparedStatement preparedStatement = null;
        String insertSQL = "INSERT INTO Events (EventID, AssociatedUsername, PersonID, Latitude, Longitude, Country, City, EventType, Year) VALUES (?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, newEvent.getEventID());
            preparedStatement.setString(2, newEvent.getAssociatedUsername());
            preparedStatement.setString(3, newEvent.getPersonID());
            preparedStatement.setDouble(4, newEvent.getLatitude());
            preparedStatement.setDouble(5, newEvent.getLongitude());
            preparedStatement.setString(6, newEvent.getCountry());
            preparedStatement.setString(7, newEvent.getCity());
            preparedStatement.setString(8, newEvent.getEventType());
            preparedStatement.setInt(9, newEvent.getYear());

            if (preparedStatement.executeUpdate() != 1) {
                throw new DatabaseException("Error: Failed to insert newEvent");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DatabaseException("Error: Missing information");
        }

    }

    /**
     * takes in a specific ID and returns a specific event
     * @param eventID ID of the event to be retrieved
     * @return Event from the database
     * @throws DatabaseException throws if fails to retrieve event
     */
    public Event getEvent(String eventID) throws DatabaseException {
        Event event = null;
        String getSQL = "SELECT * FROM Events WHERE EventID = '" + eventID + "'";

        try (PreparedStatement preparedStatement = conn.prepareStatement(getSQL)) {
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                event = new Event();
                event.setEventID(result.getString(1));
                event.setAssociatedUsername(result.getString(2));
                event.setPersonID(result.getString(3));
                event.setLatitude(result.getDouble(4));
                event.setLongitude(result.getDouble(5));
                event.setCountry(result.getString(6));
                event.setCity(result.getString(7));
                event.setEventType(result.getString(8));
                event.setYear(result.getInt(9));
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DatabaseException("Error: Failed to retrieve event from database");
        }

        return event;
    }

    /**
     * takes in a username and returns all events for all family members associated with that user
     * @param username username of current user
     * @return allEvents every event for that user
     * @throws DatabaseException SQL exception
     */
    public ArrayList<Event> getAllEvents(String username) throws DatabaseException {
        ArrayList<Event> allEvents = new ArrayList<>();
        String getSQL = "SELECT * FROM Events WHERE AssociatedUsername = '" + username + "'";

        try (PreparedStatement preparedStatement = conn.prepareStatement(getSQL);){
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                Event event = new Event();
                event.setEventID(result.getString(1));
                event.setAssociatedUsername(result.getString(2));
                event.setPersonID(result.getString(3));
                event.setLatitude(result.getDouble(4));
                event.setLongitude(result.getDouble(5));
                event.setCountry(result.getString(6));
                event.setCity(result.getString(7));
                event.setEventType(result.getString(8));
                event.setYear(result.getInt(9));
                allEvents.add(event);
            }
        } catch (SQLException throwables) {
            throw new DatabaseException("Error: getAllEvents has SQL exception");
        }

        return allEvents;
    }

    /**
     * gets the year of a specified event
     * @param personID ID of the person the event is attached too
     * @param eventType the type of event
     * @return year Returns the year of the event
     */
    public int getEventYear(String personID, String eventType) throws DatabaseException {
        String getSQL = "SELECT * FROM Events WHERE PersonID = '" + personID + "' AND EventType = '" + eventType + "'";
        int year = 0;

        try (Statement statement = conn.createStatement()) {
            ResultSet result = statement.executeQuery(getSQL);
            while (result.next()) {
                year = result.getInt(9);
            }
            if (year == 0) throw new DatabaseException("Error: Failed to retrieve year of the event");
        } catch  (SQLException throwables) {
            throwables.printStackTrace();
        }

        return year;
    }

    /**
     * Clears all events from the databse
     * @throws DatabaseException if fails to clear
     */
    public void clearEvents() throws DatabaseException {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM Events");
            statement.close();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DatabaseException("Error: Failed to clear table");
        }
    }

}
