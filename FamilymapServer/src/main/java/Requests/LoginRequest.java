package Requests;

public class LoginRequest {
    private String userName, password;

    /**
     * holds data to be given to the database
     * @param userName Non-empty string
     * @param password Non-empty string
     */
    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
