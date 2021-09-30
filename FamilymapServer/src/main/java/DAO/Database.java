package DAO;

import java.sql.*;

public class Database {
    private Connection conn;

    public Database() {

    }

    /**
     * opens connection to the database
     * @return Returns the connection
     * @throws DatabaseException throws if fails to open connection
     */
    public Connection openConnection() throws DatabaseException { //throws DatabaseException
        try {
            String ConnURL = "jdbc:sqlite:FamilyMapDatabase.sqlite";
            conn = DriverManager.getConnection(ConnURL);

            conn.setAutoCommit(false);
        } catch (Exception e) {
            throw new DatabaseException();
        }

        return conn;
    }

    /**
     * Closes the database
     * @param commit determines if changes are kept or not
     * @throws DatabaseException
     */
    public void closeConnection(boolean commit) throws DatabaseException { //throws DatabaseException
        try {
            if (commit) {
                conn.commit();
            }
            else {
                conn.rollback();
            }
        } catch(SQLException e) {
            throw new DatabaseException("Failed to close connection");
        }
    }

    /**
     * Clears all tables in database
     * @throws DatabaseException throws to clear service if Database is not cleared
     */
    public void clearTables() throws DatabaseException { //throws DatabaseException
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM AuthTokens");
            statement.executeUpdate("DELETE FROM Events");
            statement.executeUpdate("DELETE FROM Persons");
            statement.executeUpdate("DELETE FROM Users");
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DatabaseException("Failed to clear all tables");
        }
    }

    public Connection getConnection() {
        return conn;
    }

}
