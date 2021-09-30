package Handlers;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class Serialization {
    /**
     * Reads in Json
     * @param requestBody the Http exchange
     * @return a String with the request body
     */
    String readInput(InputStream requestBody) {
        Scanner readJson = new Scanner(requestBody);
        StringBuilder jsonString = new StringBuilder();
        while(readJson.hasNext()) {
            jsonString.append(readJson.nextLine());
        }

        return jsonString.toString();
    }

    /**
     * parses url and provides input
     * @param requestedURL exchange.getRequestURI().toString();
     * @return a String array with the input
     */
    String[] readInput(String requestedURL) {
        StringBuilder url = new StringBuilder(requestedURL);
        String[] input;
        input = url.toString().split("/");
        return input;
    }

    /**
     * creates a response to turn into Json
     * @param json Json string
     * @param responseBody the  response body
     * @throws IOException throws if fails to write or close
     */
    public void readOut(String json, OutputStream responseBody) throws IOException {
        responseBody.write(json.getBytes());
        responseBody.close();
    }

    public static <T> T deserialize(String jsonString, Class<T> returnClass) {
        return (new Gson()).fromJson(jsonString, returnClass);
    }

    public static <T> String serialize(T response, Class<T> classType) {
        return (new Gson()).toJson(response, classType);
    }
}
