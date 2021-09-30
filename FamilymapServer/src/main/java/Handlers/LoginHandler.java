package Handlers;

import DAO.DatabaseException;
import Requests.LoginRequest;
import Responses.LoginResponse;
import Services.LoginService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

public class LoginHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().toLowerCase().equals("post")) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
        else {
            LoginService loginService = new LoginService();
            Serialization serialize = new Serialization();
            String jsonString = serialize.readInput(exchange.getRequestBody());
            LoginRequest request = Serialization.deserialize(jsonString, LoginRequest.class);
            LoginResponse response = new LoginResponse();

            try {
                response = loginService.login(request);
                if(response.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                serialize.readOut(Serialization.serialize(response, LoginResponse.class), exchange.getResponseBody());
                exchange.close();
            } catch (IOException | DatabaseException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                serialize.readOut(Serialization.serialize(response, LoginResponse.class), exchange.getResponseBody());
                exchange.close();
            }
        }
    }
}
