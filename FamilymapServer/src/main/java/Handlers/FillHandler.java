package Handlers;

import DAO.DatabaseException;
import Requests.FillRequest;
import Responses.FillResponse;
import Services.FillService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

public class FillHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String urlPath = exchange.getRequestURI().toString();

        if (!exchange.getRequestMethod().toLowerCase().equals("post")) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
        else {
            FillService fillService = new FillService();
            Serialization serialize = new Serialization();
            String[] input = serialize.readInput(urlPath);
            Gson gson =  new Gson();
            FillRequest request = new FillRequest();
            FillResponse response = new FillResponse();
            request.setUsername(input[2]);
            try {
                if (input.length == 4) {
                    request.setNumGenerations(Integer.parseInt(input[3]));
                }
                else if (input.length == 3) {
                    request.setNumGenerations(4);
                }
                else {
                    throw new IOException("URL arguments are invalid");
                }

                response = fillService.Fill(request);
                if(response.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                serialize.readOut(gson.toJson(response), exchange.getResponseBody());
                exchange.close();
            } catch (IOException | DatabaseException e) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                response.setSuccess(false);
                response.setMessage(e.toString());
                serialize.readOut(Serialization.serialize(response, FillResponse.class), exchange.getResponseBody());
                exchange.close();
                e.printStackTrace();
            }
        }
    }
}
