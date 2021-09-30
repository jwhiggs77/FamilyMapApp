package Services;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Requests.EventRequest;
import Responses.EventResponse;

import java.sql.Connection;
import java.util.ArrayList;

public class EventService {
    public EventService() {
    }

    /**
     * processes the event request to the database and returns a response from the database
     * @param request Non-empty auth token string
     * @return response all events
     */
    public EventResponse event(EventRequest request) throws DatabaseException {
        Database database = new Database();
        EventResponse response = new EventResponse();
        Connection conn = null;
        ArrayList<Event> events;

        try {
            conn = database.openConnection();
            EventDAO eventDAO = new EventDAO(conn);
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);

            AuthToken authToken = authTokenDAO.getAuthToken(request.getToken());
            if (authToken == null) {
                throw new DatabaseException("Error: AuthToken does not exist");
            }
            events = eventDAO.getAllEvents(authToken.getUsername());
            response.setData(events);
            response.setSuccess(true);
            database.closeConnection(true);
        } catch (DatabaseException e) {
            database.closeConnection(false);
            response.setSuccess(false);
            response.setMessage(e.toString());
            e.printStackTrace();
        }

        return response;
    }
}