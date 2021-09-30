package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String urlPath = exchange.getRequestURI().toString();

        if (!exchange.getRequestMethod().toLowerCase().equals("get")) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
        else {
            if (urlPath == null || urlPath.equals("/")) {
                urlPath = new String("web/index.html");
                Path filePath = FileSystems.getDefault().getPath(urlPath);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                Files.copy(filePath, exchange.getResponseBody());
                exchange.getResponseBody().close();
            }
            else if (urlPath.equals("/index.html") || urlPath.equals("/css/main.css")){
                urlPath = "web" + urlPath;
                Path filePath = FileSystems.getDefault().getPath(urlPath);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                Files.copy(filePath, exchange.getResponseBody());
                exchange.getResponseBody().close();
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                urlPath = "/Users/joshuahiggins/Downloads/FamilyMapServerStudent-master/web/HTML/404.html";
                Path filePath = FileSystems.getDefault().getPath(urlPath);
                Files.copy(filePath, exchange.getResponseBody());
            }
            exchange.close();
        }
    }
}
