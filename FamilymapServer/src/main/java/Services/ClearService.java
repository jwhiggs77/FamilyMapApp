package Services;

import DAO.Database;
import DAO.DatabaseException;
import Responses.ClearResponse;

public class ClearService {

    public ClearService() {

    }

    /**
     * clears entire database
     * @return response
     */
    public ClearResponse clear() throws DatabaseException {
        Database database = new Database();
        ClearResponse response =  new ClearResponse();

        try {
            database.openConnection();
            database.clearTables();
            database.closeConnection(true);
            response.setSuccess(true);
            response.setMessage("Clear succeeded");
        } catch (DatabaseException e) {
            database.closeConnection(false);
            response.setSuccess(false);
            response.setMessage("Error: Failed to clear database");
        }

        return response;
    }
}
