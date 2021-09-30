package Responses;

public class RegisterResponse {
    String message;
    boolean success;
    String authToken;
    String userName;
    String personID;

    /**
     * holds data being sent from the database
     * @param success boolean identifier
     * @param authToken Non-empty auth token string
     * @param userName username of the new user
     * @param personID the unique person ID
     */
    public RegisterResponse(boolean success, String authToken, String userName, String personID) {
        this.success = success;
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
    }

    //constructor encase of no params
    public RegisterResponse() {

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setMessage(String e) {
        message = e.toString();
    }
}
