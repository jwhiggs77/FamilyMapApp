package Responses;

public class EventIdResponse {
    String message;
    boolean success;
    String associatedUsername, eventID, personID, country, city, eventType;
    double latitude, longitude;
    int year;

    public EventIdResponse() {
    }

    /**
     * holds data being sent from database
     * @param success Boolean identifier
     * @param associatedUsername Username of user account this event belongs to (non-empty string)
     * @param eventID Event’s unique ID (non-empty string)
     * @param personID ID of the person this event belongs to (non-empty string)
     * @param latitude Latitude of the event’s location (number)
     * @param longitude Longitude of the event’s location (number)
     * @param country Name of country where event occurred (non-empty string)
     * @param city Name of city where event occurred (non-empty string)
     * @param eventType Type of event (“birth”, “baptism”, etc.) (non-empty string)
     * @param year Year the event occurred (integer)
     */
    public EventIdResponse(boolean success, String associatedUsername, String eventID, String personID, double latitude, double longitude, String country, String city, String eventType, int year) {
        this.success = success;
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}