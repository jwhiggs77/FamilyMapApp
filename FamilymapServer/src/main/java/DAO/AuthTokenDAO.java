package DAO;

import Model.AuthToken;

import java.sql.*;

public class AuthTokenDAO {
    Connection conn;

    public AuthTokenDAO(Connection connection) {
        conn = connection;
    }

    /**
     * inserts an authToken
     @param token AuthToken to be inserted into database
     @throws DatabaseException throws upon failure to insert the token
     */
    public void insertAuthToken(AuthToken token) throws DatabaseException {
        String insertSQL = "INSERT INTO AuthTokens (Username, Token) VALUES (?,?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, token.getUsername());
            preparedStatement.setString(2, token.getToken());

            if (preparedStatement.executeUpdate() != 1) {
                throw new DatabaseException("Error: Failed to insert authToken");
            }
        }
        catch (SQLException throwables) {
            throw new DatabaseException("Error: SQL insertion error");
        }
    }

    /**
     @param token a string containing the ID of the token
     @throws DatabaseException throws if not able to get the authToken
     @return getToken returns a Token from the database
     */
    public AuthToken getAuthToken(String token) throws DatabaseException {
        AuthToken getToken = null;

        String getSQL = "SELECT * FROM AuthTokens WHERE Token = '" + token + "'";
            try (PreparedStatement preparedStatement = conn.prepareStatement(getSQL);) {
                ResultSet result = preparedStatement.executeQuery();
                while (result.next()) {
                    getToken = new AuthToken();
                    getToken.setUsername(result.getString(1));
                    getToken.setToken(result.getString(2));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new DatabaseException("Error: Failed to retrieve authToken");
            }

        return getToken;
    }

    /**
     * clears authTokens from database
     * @throws DatabaseException throws if failed to clear authTokens
     */
    public void clearAuthTokens() throws DatabaseException {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM AuthTokens");
            //close statement
            statement.close();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DatabaseException("Error: Failed to clear table");
        }
    }
}
