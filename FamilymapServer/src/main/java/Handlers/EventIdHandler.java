package Handlers;

import DAO.DatabaseException;
import Requests.EventIdRequest;
import Responses.EventIdResponse;
import Services.EventIdService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

public class EventIdHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String urlPath = exchange.getRequestURI().toString();

        if (!exchange.getRequestMethod().toLowerCase().equals("get")) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
        else {
            EventIdService eventIdService = new EventIdService();
            EventIdRequest request = new EventIdRequest();
            EventIdResponse response = new EventIdResponse();
            Serialization serialize = new Serialization();
            String[] input = serialize.readInput(urlPath);
            request.setEventID((input[2]));
            request.setAuthTokenID(exchange.getRequestHeaders().getFirst("Authorization"));

            //create and send response
            try {
                if (input.length != 3) {
                    throw new IOException("URL arguments are invalid");
                }
                response = eventIdService.eventID(request);
                if(response.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                Gson gson = new Gson();
                serialize.readOut(gson.toJson(response), exchange.getResponseBody());
                exchange.close();
            } catch (IOException | DatabaseException e) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                response.setSuccess(false);
                response.setMessage("Internal server error");
                serialize.readOut(Serialization.serialize(response, EventIdResponse.class), exchange.getResponseBody());
                exchange.close();
            }
        }
    }
}
