package Requests;

import Model.AuthToken;

public class EventIdRequest {
    String eventID;
    String authTokenId;

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getAuthTokenID() {
        return authTokenId;
    }

    public void setAuthTokenID(String authTokenId) {
        this.authTokenId = authTokenId;
    }
}
