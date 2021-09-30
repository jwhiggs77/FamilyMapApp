package Requests;

import Model.AuthToken;

public class EventRequest {
    String tokenID;

    /**
     * sends data to the database
     * @param tokenID Non-empty auth token string
     */
    public EventRequest(String tokenID) {
        this.tokenID = tokenID;
    }

    public EventRequest() {

    }

    public void setAuthTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public String getToken() {
        return tokenID;
    }
}
