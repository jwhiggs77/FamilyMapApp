package Requests;

import Model.AuthToken;

public class PersonRequest {
    String tokenID;

    /**
     * sends data to the database
     * @param tokenID Non-empty auth token string
     */
    public PersonRequest(String tokenID) {
        this.tokenID = tokenID;
    }

    public PersonRequest() {

    }

    public void setAuthTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public String getTokenID() {
        return tokenID;
    }
}

