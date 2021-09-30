package Responses;

import Requests.LoadRequest;

public class LoadResponse {
    String message;
    boolean success;

    /**
     * holds data being sent from database
     * @param success boolean identifier
     * @param request holds the arrays to get the sizes for the message output
     */
    public LoadResponse(boolean success, LoadRequest request) {
        this.success = success;
        message = "Successfully added " + request.getUsers().size() + " users, " + request.getPersons().size() + " persons, and " + request.getEvents().size() + " events to the database.";
    }

    public LoadResponse() {

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
