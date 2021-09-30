package Responses;

public class ClearResponse {
    boolean success;
    String message;

    /**
     * holds data being sent from database
     * @param success boolean identifier
     */
    public ClearResponse(boolean success) {
        this.success = success;
        message = "Clear succeeded";
    }

    //for if no params
    public ClearResponse() {

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
