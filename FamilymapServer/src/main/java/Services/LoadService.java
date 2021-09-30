package Services;

import DAO.*;
import Model.Event;
import Model.Person;
import Model.User;
import Requests.LoadRequest;
import Responses.LoadResponse;

import java.sql.Connection;

public class LoadService {

    /**
     * processes the login request to the database and returns a response from the database
     * @param request request to the database
     * @return response
     */
    public LoadResponse load(LoadRequest request) throws DatabaseException {
        Database database = new Database();
        LoadResponse response = new LoadResponse();
        Connection conn = null;

        try {
            conn = database.openConnection();
            UserDAO userDAO = new UserDAO(conn);
            PersonDAO personDAO = new PersonDAO(conn);
            EventDAO eventDAO = new EventDAO(conn);
            database.clearTables();

            if (request.getUsers() == null || request.getPersons() == null || request.getEvents() == null) {
                throw new DatabaseException("Error: Request has a null parameter");
            }

            for (int i = 0; i < request.getUsers().size(); i++) {
                User newUser = request.getUsers().get(i);
                userDAO.insertUser(newUser);
            }

            for (int i = 0; i < request.getPersons().size(); i++) {
                Person newPerson = request.getPersons().get(i);
                personDAO.insertPerson(newPerson);
            }

            for (int i = 0; i < request.getEvents().size(); i++) {
                Event newEvent = request.getEvents().get(i);
                eventDAO.insertEvent(newEvent);
            }

            response.setSuccess(true);
            response.setMessage("Successfully added " + request.getUsers().size() + " users, " + request.getPersons().size() + " persons, and " + request.getEvents().size() + " events to the database.");
            database.closeConnection(true);
        } catch (DatabaseException e) {
            database.closeConnection(false);
            response.setSuccess(false);
            response.setMessage(e.toString());
        }

        return response;
    }
}
