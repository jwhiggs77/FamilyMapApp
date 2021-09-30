package Handlers;

import DAO.DatabaseException;
import Requests.PersonRequest;
import Responses.PersonResponse;
import Services.PersonService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

public class PersonHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().toLowerCase().equals("get")) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
        else {
            PersonService personService = new PersonService();
            PersonRequest request = new PersonRequest(exchange.getRequestHeaders().getFirst("Authorization"));
            PersonResponse response = new PersonResponse();
            Serialization serialize = new Serialization();

            try {
                response = personService.person(request);
                if(response.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                serialize.readOut(Serialization.serialize(response, PersonResponse.class), exchange.getResponseBody());
                exchange.close();
            } catch (IOException | DatabaseException e) {
                response.setSuccess(false);
                response.setMessage("Internal server error");
                serialize.readOut(Serialization.serialize(response, PersonResponse.class), exchange.getResponseBody());
                exchange.close();
                e.printStackTrace();
            }
        }
    }
}