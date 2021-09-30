package DAO;

import Model.User;

import java.sql.*;

public class UserDAO {

    private Connection conn;

    public UserDAO() {

    }

    public UserDAO(Connection connection) {
        conn = connection;
    }

    /**
     * inserts new user into database
     * @param newUser User to be inserted into the database
     * @throws DatabaseException throws if fails to insert the User
     */
    public void insertUser(User newUser) throws DatabaseException {
        String insertSQL = "INSERT INTO Users (Username, Password, Email, FirstName, LastName, Gender, PersonID) VALUES (?,?,?,?,?,?,?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
            if (newUser.getGender().equals("m") == false && newUser.getGender().equals("f") == false) {
                throw new DatabaseException("Error: Gender is invalid");
            }

            preparedStatement.setString(1, newUser.getUserName());
            preparedStatement.setString(2, newUser.getPassword());
            preparedStatement.setString(3, newUser.getEmail());
            preparedStatement.setString(4, newUser.getFirstName());
            preparedStatement.setString(5, newUser.getLastName());
            preparedStatement.setString(6, newUser.getGender());
            preparedStatement.setString(7, newUser.getPersonID());

            if (preparedStatement.executeUpdate() != 1) {
                throw new DatabaseException("Error: Failed to insert newUser");
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            if (throwables.toString().equals("org.sqlite.SQLiteException: [SQLITE_CONSTRAINT_PRIMARYKEY]  A PRIMARY KEY constraint failed (UNIQUE constraint failed: Users.UserName)")) {
                throw new DatabaseException("Error: User already exists");
            }
            else {
                throwables.printStackTrace();
                throw new DatabaseException("Error: Missing user information");
            }
        }
    }

    /**
     * gets a user login info and returns a User
     * @param username username of User
     * @throws DatabaseException throws if unable to get the User
     * @return User returns user from database if found
     */
    public User getUser(String username) throws DatabaseException {
        User user = null;
        String getSQL = "SELECT * FROM Users WHERE Username = '" + username + "'";

        try (PreparedStatement preparedStatement = conn.prepareStatement(getSQL)) {
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                user = new User();
                user.setUserName(result.getString(1));
                user.setPassword(result.getString(2));
                user.setEmail(result.getString(3));
                user.setFirstName(result.getString(4));
                user.setLastName(result.getString(5));
                user.setGender(result.getString(6));
                user.setPersonID(result.getString(7));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DatabaseException("Error: Failed to retrieve user");
        }

        return user;
    }

    /**
     * clears all users from database
     * @throws DatabaseException throws if fails to clear Users from database
     */
    public void clearUsers() throws DatabaseException {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM Users");
            //close statement
            statement.close();
        }
        catch (SQLException throwables) {
            throw new DatabaseException("Error: Failed to clear users from database");
        }
    }
}
