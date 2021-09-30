package Responses;

public class LoginResponse {
    String message;
    boolean success;
    String authToken;
    String userName;
    String personID;

    /**
     * holds data being sent from database
     * @param success Boolean identifier
     * @param authToken Non-empty auth token string
     * @param userName Username passed in with request
     * @param personID Non-empty string containing the Person ID of the user’s generated Person object
     */
    public LoginResponse(boolean success, String authToken, String userName, String personID) {
        this.success = success;
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
    }

    //constructor encase of no params
    public LoginResponse() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
}
