package Handlers;

import DAO.DatabaseException;
import Requests.EventRequest;
import Responses.EventResponse;
import Services.EventService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

public class EventHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().toLowerCase().equals("get")) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
        else {
            EventService eventService = new EventService();
            EventRequest request = new EventRequest(exchange.getRequestHeaders().getFirst("Authorization"));
            EventResponse response = new EventResponse();
            Serialization serialize = new Serialization();

            try {
                response = eventService.event(request);
                if(response.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                serialize.readOut(Serialization.serialize(response, EventResponse.class), exchange.getResponseBody());
                exchange.close();
            } catch (IOException | DatabaseException e) {
                response.setSuccess(false);
                response.setMessage("Error: Internal server error");
                serialize.readOut(Serialization.serialize(response, EventResponse.class), exchange.getResponseBody());
                exchange.close();
                e.printStackTrace();
            }
        }
    }
}
