package com.example.familymapclient;

import org.junit.*;
import static org.junit.Assert.*;
import java.net.MalformedURLException;
import java.net.URL;

import Requests.EventRequest;
import Requests.LoginRequest;
import Requests.PersonRequest;
import Requests.RegisterRequest;
import Responses.EventResponse;
import Responses.LoginResponse;
import Responses.PersonResponse;
import Responses.RegisterResponse;

public class serverProxyTest {
    private URL testURL;
    private serverProxy proxy;
    RegisterRequest regRequest;
    String username;
    String password;

    @Before
    public void setUp() {
        proxy = new serverProxy();
        username = "DarthVader";
        password = "DeathStar";
        regRequest = new RegisterRequest();
        regRequest.setUserName(username);
        regRequest.setPassword("DeathStar");
        regRequest.setFirstName("Anakin");
        regRequest.setLastName("Skywalker");
        regRequest.setGender("m");
        regRequest.setEmail("r2d2@mail");
    }

    @After
    public void tearDown() {
        try {
            testURL = new URL("http://localhost:8080/clear");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        proxy.clear(testURL);
        proxy = null;
        testURL = null;
    }

    @Test
    public void positiveRegisterTest() {
        try {
            testURL = new URL("http://localhost:8080/user/register");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        RegisterResponse response = proxy.getRegisterResponse(testURL, regRequest);

        assertTrue(response.isSuccess());
        assertEquals(username, response.getUserName());
        assertNotNull(response.getAuthToken());
        assertNotNull(response.getPersonID());
    }

    @Test
    public void positiveLoginTest() {
        try {
            testURL = new URL("http://localhost:8080/user/register");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        proxy.getRegisterResponse(testURL, regRequest);

        try {
            testURL = new URL("http://localhost:8080/user/login");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        LoginRequest request = new LoginRequest();
        request.setUserName(username);
        request.setPassword(password);
        LoginResponse response = proxy.getLoginResponse(testURL, request);

        assertTrue(response.isSuccess());
        assertEquals(username, response.getUserName());
        assertNotNull(response.getAuthToken());
        assertNotNull(response.getPersonID());
    }

    @Test
    public void positiveGetPeopleTest() {
        URL registerURL = null;
        try {
            testURL = new URL("http://localhost:8080/person");
            registerURL = new URL("http://localhost:8080/user/register");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        RegisterResponse registerResponse = proxy.getRegisterResponse(registerURL, regRequest);

        PersonRequest personRequest = new PersonRequest();
        personRequest.setAuthTokenID(registerResponse.getAuthToken());
        PersonResponse personResponse = proxy.getPersonResponse(testURL, personRequest);

        assertTrue(personResponse.isSuccess());
        assertNotNull(personResponse.getData());
    }

    @Test
    public void positiveGetEventsTest() {
        URL registerURL = null;
        try {
            testURL = new URL("http://localhost:8080/person");
            registerURL = new URL("http://localhost:8080/user/register");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        RegisterResponse registerResponse = proxy.getRegisterResponse(registerURL, regRequest);

        EventRequest eventRequest = new EventRequest();
        eventRequest.setAuthTokenID(registerResponse.getAuthToken());
        EventResponse eventResponse = proxy.getEventResponse(testURL, eventRequest);

        assertTrue(eventResponse.isSuccess());
        assertNotNull(eventResponse.getData());
    }
}