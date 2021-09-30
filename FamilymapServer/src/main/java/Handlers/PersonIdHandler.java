package Handlers;

import DAO.DatabaseException;
import Requests.PersonIdRequest;
import Responses.PersonIdResponse;
import Services.PersonIdService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

public class PersonIdHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String urlPath = exchange.getRequestURI().toString();

        if (!exchange.getRequestMethod().toLowerCase().equals("get")) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
        else {
            PersonIdService personIdService = new PersonIdService();
            PersonIdRequest request = new PersonIdRequest();
            PersonIdResponse response = new PersonIdResponse();
            Serialization serialize = new Serialization();
            String[] input = serialize.readInput(urlPath);
            request.setPersonID(input[2]);
            request.setAuthTokenID(exchange.getRequestHeaders().getFirst("Authorization"));

            try {
                if (input.length != 3) {
                    throw new IOException("URL arguments are invalid");
                }
                response = personIdService.personID(request);
                if(response.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                serialize.readOut(Serialization.serialize(response, PersonIdResponse.class), exchange.getResponseBody());
                exchange.close();
            } catch (IOException | DatabaseException e) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                response.setSuccess(false);
                response.setMessage("Internal server error");
                serialize.readOut(Serialization.serialize(response, PersonIdResponse.class), exchange.getResponseBody());
                exchange.close();
            }
        }
    }
}
