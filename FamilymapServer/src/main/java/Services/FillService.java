package Services;

import DAO.*;
import Model.User;
import Requests.FillRequest;
import Responses.FillResponse;
import Services.Fill.GenerateData;

import java.sql.Connection;

public class FillService {
    public FillService(){

    }

    /**
     * fills the family tree of the user
     * @param request holds username of user and number of generations to fill
     * @return response
     */
    public FillResponse Fill(FillRequest request) throws DatabaseException {
        Database database = new Database();
        FillResponse response = new FillResponse();
        Connection conn = null;

        try {
            conn = database.openConnection();
            PersonDAO personDAO = new PersonDAO(conn);
            UserDAO userDAO = new UserDAO(conn);
            EventDAO eventDAO = new EventDAO(conn);
            GenerateData newData = new GenerateData();
            User requestUser  = userDAO.getUser(request.getUsername());
            if (requestUser == null) {
                throw new DatabaseException("Error: User does not exist");
            }
            //clear data connected to username
            personDAO.clearAssociationData(request.getUsername(), requestUser.getPersonID());
            newData.SetPersonDAO(personDAO);
            newData.SetEventDAO(eventDAO);
            newData.FillGenerations(personDAO.getPerson(requestUser.getPersonID()), request.getNumGenerations());
            database.closeConnection(true);
            response.setSuccess(true);
            response.setMessage("Successfully added " + newData.getNumPersons() + " persons and " + newData.getNumEvents() + " events to the database.");
        } catch (DatabaseException e) {
            database.closeConnection(false);
            response.setSuccess(false);
            response.setMessage(e.toString());
            e.printStackTrace();
        }

        return response;
    }
}