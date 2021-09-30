package Services;

import DAO.AuthTokenDAO;
import DAO.Database;
import DAO.DatabaseException;
import DAO.PersonDAO;
import Model.AuthToken;
import Model.Person;
import Requests.PersonIdRequest;
import Responses.PersonIdResponse;

import java.sql.Connection;

public class PersonIdService {

    public PersonIdService(){

    }

    /**
     * processes the personID request to the database and returns a response from the database
     * @param request holds the personID and authTokenID
     * @return response sends back person information
     */
    public PersonIdResponse personID(PersonIdRequest request) throws DatabaseException {
        Database database = new Database();
        PersonIdResponse response = new PersonIdResponse();
        Connection conn = null;

        try {
            conn = database.openConnection();
            PersonDAO personDAO = new PersonDAO(conn);
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);

            Person returnPerson = personDAO.getPerson(request.getPersonID());
            AuthToken token = authTokenDAO.getAuthToken(request.getAuthTokenID());
            if (token == null) {
                throw new DatabaseException("Error: Invalid auth token");
            }
            else if (returnPerson == null) {
                throw new DatabaseException("Error: Person not found in database");
            }
            else if (!returnPerson.getAssociatedUsername().equals(token.getUsername())) {
                throw new DatabaseException("Error: Requested person does not belong to this user");
            }
            response = new PersonIdResponse(true, returnPerson.getPersonID(), returnPerson.getAssociatedUsername(), returnPerson.getFirstName(), returnPerson.getLastName(), returnPerson.getGender(), returnPerson.getFatherID(), returnPerson.getMotherID(), returnPerson.getSpouseID());

            database.closeConnection(true);
        } catch (DatabaseException e) {
            database.closeConnection(false);
            response.setMessage(e.toString());
            response.setSuccess(false);
            e.printStackTrace();
        }

        return response;
    }
}
