package Handlers;

import DAO.DatabaseException;
import Requests.LoadRequest;
import Responses.LoadResponse;
import Services.LoadService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

public class LoadHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().toLowerCase().equals("post")) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
        else {
            LoadService loadService = new LoadService();
            Serialization serialize = new Serialization();
            String jsonString = serialize.readInput(exchange.getRequestBody());
            LoadRequest request = Serialization.deserialize(jsonString, LoadRequest.class);
            LoadResponse response = new LoadResponse();

            try {
                response = loadService.load(request);
                if(response.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                serialize.readOut(Serialization.serialize(response, LoadResponse.class), exchange.getResponseBody());
                exchange.close();
            } catch (IOException | DatabaseException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                serialize.readOut(Serialization.serialize(response, LoadResponse.class), exchange.getResponseBody());
                exchange.close();
            }
        }
    }
}
