package Services;

import DAO.AuthTokenDAO;
import DAO.Database;
import DAO.DatabaseException;
import DAO.PersonDAO;
import Model.AuthToken;
import Model.Person;
import Requests.PersonRequest;
import Responses.PersonResponse;

import java.sql.Connection;
import java.util.ArrayList;

public class PersonService {
    public PersonService() {

    }

    /**
     * processes the person request to the database and returns a response from the database
     * @param request request to the database. Holds authToken
     * @return response
     */
    public PersonResponse person(PersonRequest request) throws DatabaseException {
        Database database = new Database();
        PersonResponse response = new PersonResponse();
        Connection conn = null;
        ArrayList<Person> people;

        try {
            conn = database.openConnection();
            PersonDAO personDAO = new PersonDAO(conn);
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);

            AuthToken authToken = authTokenDAO.getAuthToken(request.getTokenID());
            if (authToken == null) {
                throw new DatabaseException("Error: AuthToken does not exist");
            }
            people = personDAO.getFamilyMembers(authToken.getUsername());
            response.setData(people);
            response.setSuccess(true);
            database.closeConnection(true);
        } catch (DatabaseException e) {
            database.closeConnection(false);
            response.setSuccess(false);
            response.setMessage(e.toString());
            e.printStackTrace();
        }

        return response;
    }
}