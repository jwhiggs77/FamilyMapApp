package Services;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Requests.EventIdRequest;
import Responses.EventIdResponse;

import java.sql.Connection;

public class EventIdService {
    
    public EventIdService(){}

    /**
     * processes an eventID request to the database and returns a response from the database
     * @param request holds eventID and authTokenID
     * @return response
     */
     public EventIdResponse eventID(EventIdRequest request) throws DatabaseException {
         Database database = new Database();
         EventIdResponse response = new EventIdResponse();
         Connection conn = null;

         try {
             conn = database.openConnection();
             EventDAO eventDAO = new EventDAO(conn);
             AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);

             Event returnEvent = eventDAO.getEvent(request.getEventID());
             AuthToken token = authTokenDAO.getAuthToken(request.getAuthTokenID());
             if (token == null) {
                 throw new DatabaseException("Error: Invalid auth token");
             }
             else if (returnEvent == null) {
                 throw new DatabaseException("Error: Event not found in database");
             }
             else if (!returnEvent.getAssociatedUsername().equals(token.getUsername())) {
                throw new DatabaseException("Error: Requested event does not belong to this user");
             }
             response = new EventIdResponse(true, returnEvent.getAssociatedUsername(), returnEvent.getEventID(), returnEvent.getPersonID(), returnEvent.getLatitude(), returnEvent.getLongitude(), returnEvent.getCountry(), returnEvent.getCity(), returnEvent.getEventType(), returnEvent.getYear());
             database.closeConnection(true);
         } catch (DatabaseException e) {
             database.closeConnection(false);
             response.setMessage(e.toString());
             response.setSuccess(false);
             e.printStackTrace();
         }

         return response;
     }
}
