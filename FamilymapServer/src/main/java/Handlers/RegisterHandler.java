package Handlers;

import DAO.DatabaseException;
import Requests.RegisterRequest;
import Responses.RegisterResponse;
import Services.RegisterService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

import com.google.gson.*;

public class RegisterHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().toLowerCase().equals("post")) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
        else {
            RegisterService registerService = new RegisterService();
            Serialization serialize = new Serialization();
            String jsonString = serialize.readInput(exchange.getRequestBody());
            RegisterRequest request = Serialization.deserialize(jsonString, RegisterRequest.class);
            RegisterResponse response = new RegisterResponse();

            try {
                response = registerService.register(request);
                if(response.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                serialize.readOut(Serialization.serialize(response, RegisterResponse.class), exchange.getResponseBody());
                exchange.close();
            } catch (IOException | DatabaseException throwables) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                response.setSuccess(false);
                response.setMessage("Internal server error");
                serialize.readOut(Serialization.serialize(response, RegisterResponse.class), exchange.getResponseBody());
                exchange.close();
            }
        }
    }
}
