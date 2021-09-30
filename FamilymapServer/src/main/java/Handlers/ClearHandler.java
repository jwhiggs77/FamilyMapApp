package Handlers;

import DAO.DatabaseException;
import Responses.ClearResponse;
import Services.ClearService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

public class ClearHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        ClearResponse response = new ClearResponse();
        try {
            if (!exchange.getRequestMethod().toLowerCase().equals("post")) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.close();
                throw new IOException();
            } else {
                ClearService clearService = new ClearService();
                response = clearService.clear();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                Serialization serialization = new Serialization();
                serialization.readOut(Serialization.serialize(response, ClearResponse.class), exchange.getResponseBody());
            }
            exchange.close();
        } catch (IOException | DatabaseException e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage("Failed to clear database");
            Serialization serialization = new Serialization();
            serialization.readOut(Serialization.serialize(response, ClearResponse.class), exchange.getResponseBody());
        }
    }
}
