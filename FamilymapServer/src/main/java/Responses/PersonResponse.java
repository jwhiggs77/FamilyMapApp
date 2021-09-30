package Responses;

import Model.Person;

import java.util.ArrayList;

public class PersonResponse {
    ArrayList<Person> data;
    String message;
    boolean success;

    /**
     * holds data being sent from the database
     * @param people array of persons
     * @param success boolean identifier
     */
    public PersonResponse(ArrayList<Person> people, boolean success) {
        this.data = people;
        this.success = success;
    }

    public PersonResponse() {

    }

    public ArrayList<Person> getData() {
        return data;
    }

    public void setData(ArrayList<Person> data) {
        this.data = data;
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
}
