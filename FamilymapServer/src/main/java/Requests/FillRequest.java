package Requests;

public class FillRequest {
    String username;
    int numGenerations;

    public FillRequest() {
    }

    /**
     * holds information to be given to the database
     * @param username username of persons tree to be filled
     * @param numGenerations int with number of generations to fill
     */
    public FillRequest(String username, int numGenerations) {
        this.username = username;
        this.numGenerations = numGenerations;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNumGenerations() {
        return numGenerations;
    }

    public void setNumGenerations(int numGenerations) {
        this.numGenerations = numGenerations;
    }
}
