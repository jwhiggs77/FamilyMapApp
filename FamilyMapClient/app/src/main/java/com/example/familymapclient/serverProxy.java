package com.example.familymapclient;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.Gson;

import Requests.EventRequest;
import Requests.LoginRequest;
import Requests.PersonRequest;
import Requests.RegisterRequest;
import Responses.ClearResponse;
import Responses.EventResponse;
import Responses.LoginResponse;
import Responses.PersonResponse;
import Responses.RegisterResponse;

public class serverProxy {
    public RegisterResponse getRegisterResponse(URL url, RegisterRequest request) {
        RegisterResponse response = new RegisterResponse();
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            postConnection(connection);
            serialize(connection, request);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                response = deserialize(in, RegisterResponse.class);
            }
            else {
                response.setMessage(connection.getResponseMessage());
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        return response;
    }

    public LoginResponse getLoginResponse(URL url, LoginRequest request) {
        LoginResponse response = new LoginResponse();
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            postConnection(connection);
            serialize(connection, request);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                response = deserialize(in, LoginResponse.class);
            }
            else {
                response.setMessage(connection.getResponseMessage());
            }

        } catch (IOException e) {
            System.out.println(e);
        }
        return response;
    }

    public PersonResponse getPersonResponse(URL url, PersonRequest request) {
        PersonResponse response = new PersonResponse();

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", request.getTokenID());
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                response = deserialize(in, PersonResponse.class);
            }
            else {
                response.setMessage(connection.getResponseMessage());
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        return response;
    }

    public EventResponse getEventResponse(URL url, EventRequest request) {
        EventResponse response = new EventResponse();

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Authorization", request.getToken());
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                response = deserialize(in, EventResponse.class);
            }
            else {
                response.setMessage(connection.getResponseMessage());
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        return response;
    }

    public ClearResponse clear(URL url) {
        ClearResponse response = new ClearResponse();
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            postConnection(connection);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                response = deserialize(in, ClearResponse.class);
            }
            else {
                response.setMessage(connection.getResponseMessage());
            }

        } catch (IOException e) {
            System.out.println(e);
        }
        return response;
    }

    private void postConnection(HttpURLConnection connection) {
        try {
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static <T> void serialize(HttpURLConnection connection, T request) throws IOException {
        String json = new Gson().toJson(request);
        OutputStream os = new BufferedOutputStream(connection.getOutputStream());
        os.write(json.getBytes());
        os.flush();
        connection.getOutputStream().close();
    }

    private static <T> T deserialize(InputStream connection, Class<T> returnClass) {
        Scanner readJson = new Scanner(connection);
        StringBuilder jsonString = new StringBuilder();
        while(readJson.hasNext()) {
            jsonString.append(readJson.nextLine());
        }
        return (new Gson()).fromJson(jsonString.toString(), returnClass);
    }
}
