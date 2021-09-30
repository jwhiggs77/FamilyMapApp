package Responses;

import Model.Event;

import java.util.ArrayList;

public class EventResponse {
    String message;
    boolean success;
    ArrayList<Event> data;

    /**
     * holds data being sent from database
     * @param success boolean identifier
     * @param events array of events
     */
    public EventResponse(ArrayList<Event> events, boolean success) {
        this.success = success;
        this.data = events;
    }

    public EventResponse() {

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<Event> getData() {
        return data;
    }

    public void setData(ArrayList<Event> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
