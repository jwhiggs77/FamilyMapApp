import Handlers.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    public static void main(String[] args) {
        String portNum = args[0];
        try {
            new Server().startServer(Integer.parseInt(portNum));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startServer(int port) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(serverAddress, 10);
        registerHandlers(server);
        server.start();
        System.out.println("Family Map server listening on port " + port);
    }

    private void registerHandlers(HttpServer server) {
        server.createContext("/", new FileHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill/", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person/", new PersonIdHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/event/", new EventIdHandler());
        server.createContext("/event", new EventHandler());
    }
}
