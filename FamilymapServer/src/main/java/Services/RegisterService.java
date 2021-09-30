package Services;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Requests.FillRequest;
import Requests.RegisterRequest;
import Responses.RegisterResponse;
import Services.Fill.GenerateData;

import java.util.UUID;

import java.sql.Connection;

public class RegisterService {
    public RegisterService() { }

    /**
     * processes the register request to the database and returns a response from the database
     * @param request request to the database
     * @return response
     */
    public RegisterResponse register(RegisterRequest request) throws DatabaseException {
        Database database = new Database();
        RegisterResponse response = new RegisterResponse();
        Connection conn = null;

        User user = new User();
        Person person = new Person();
        AuthToken authToken = new AuthToken();

        try {
            conn = database.openConnection();
            UserDAO userDAO = new UserDAO(conn);
            PersonDAO personDAO = new PersonDAO(conn);
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
            EventDAO eventDAO = new EventDAO(conn);

            //create new user and insert it into database
            user.setUserName(request.getUserName());
            user.setPassword(request.getPassword());
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setGender(request.getGender());
            user.setPersonID(UUID.randomUUID().toString());
            userDAO.insertUser(user);

            //create a person for the user and insert into database
            person.setPersonID(user.getPersonID());
            person.setAssociatedUsername(request.getUserName());
            person.setFirstName(request.getFirstName());
            person.setLastName(request.getLastName());
            person.setGender(request.getGender());
            person.setFatherID(null);
            person.setMotherID(null);
            person.setSpouseID(null);
            personDAO.insertPerson(person);

            //fill events for the person
            Event birth = new Event();
            GenerateData newData = new GenerateData();

            birth.setEventID(UUID.randomUUID().toString());
            birth.setAssociatedUsername(user.getUserName());
            birth.setPersonID(user.getPersonID());
            newData.MakeLocation(birth);
            birth.setEventType("Birth");
            birth.setYear(((int)(Math.random() * (2020 - 1940 + 1)) + 1940));
            eventDAO.insertEvent(birth);

            //return authToken
            authToken.setUsername(request.getUserName());
            authToken.setToken(UUID.randomUUID().toString());
            authTokenDAO.insertAuthToken(authToken);

            response.setSuccess(true);
            response.setAuthToken(authToken.getToken());
            response.setUserName(user.getUserName());
            response.setPersonID(person.getPersonID());
            database.closeConnection(true);

            FillService fill = new FillService();
            FillRequest fillRequest = new FillRequest(user.getUserName(), 4);
            fill.Fill(fillRequest);

        } catch (DatabaseException e) {
            database.closeConnection(false);
            response.setSuccess(false);
            response.setMessage(e.toString());
            e.printStackTrace();
        }

        return response;
    }
}