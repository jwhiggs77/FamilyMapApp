package Services;

import DAO.*;
import Model.AuthToken;
import Model.User;
import Responses.LoginResponse;
import Requests.LoginRequest;

import java.sql.Connection;
import java.util.UUID;

public class LoginService {

    public LoginService() {

    }

    /**
     * //processes the login request to the database and returns a response from the database
     * @param request request to the database
     * @return response
     */
    public LoginResponse login(LoginRequest request) throws DatabaseException {
        Database database = new Database();
        LoginResponse response = new LoginResponse();
        Connection conn = null;

        try {
            conn = database.openConnection();
            UserDAO userDAO = new UserDAO(conn);
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
            User loginUser;

            if (userDAO.getUser(request.getUserName()) != null) { //finds the username
                loginUser = userDAO.getUser(request.getUserName());
                if (loginUser.getPassword().equals(request.getPassword())) {
                    response.setSuccess(true);
                    AuthToken returnToken = new AuthToken();
                    returnToken.setUsername(loginUser.getUserName());
                    returnToken.setToken(UUID.randomUUID().toString());
                    authTokenDAO.insertAuthToken(returnToken);
                    response.setAuthToken(returnToken.getToken());
                    response.setUserName(loginUser.getUserName());
                    response.setPersonID(loginUser.getPersonID());
                }
                else {
                    throw new DatabaseException("Error: Password property missing or has invalid value");
                }
            }
            else {
                throw new DatabaseException("Error: Username property missing or has invalid value");
            }

            database.closeConnection(true);
        } catch (DatabaseException e) {
            database.closeConnection(false);
            response.setSuccess(false);
            response.setMessage(e.toString());
        }

        return response;
    }

}
