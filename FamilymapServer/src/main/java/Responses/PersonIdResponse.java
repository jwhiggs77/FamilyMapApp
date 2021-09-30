package Responses;

public class PersonIdResponse {
    String message;
    boolean success;
    String personID, associatedUsername, firstName, lastName;
    String gender;
    String fatherID, motherID, spouseID;

    public PersonIdResponse() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * holds data being sent from the database
     * @param success Boolean identifier
     * @param associatedUsername Name of user account this person belongs to
     * @param personID Person’s unique ID
     * @param firstName Person’s first name
     * @param lastName Person’s last name
     * @param gender Person’s gender (“m” or “f”)
     * @param fatherID ID of person’s father [OPTIONAL, can be missing]
     * @param motherID ID of person’s mother [OPTIONAL, can be missing]
     * @param spouseID ID of person’s spouse [OPTIONAL, can be missing]
     */
    public PersonIdResponse(boolean success, String personID, String associatedUsername, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this.success = success;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    /**
     * holds data being sent from the database
     * for if optional params are missing
     * @param success Boolean identifier
     * @param associatedUsername Name of user account this person belongs to
     * @param personID Person’s unique ID
     * @param firstName Person’s first name
     * @param lastName Person’s last name
     * @param gender Person’s gender (“m” or “f”)
     */
    public PersonIdResponse(boolean success, String associatedUsername, String personID, String firstName, String lastName, String gender) {
        this.success = success;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
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

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }
}
