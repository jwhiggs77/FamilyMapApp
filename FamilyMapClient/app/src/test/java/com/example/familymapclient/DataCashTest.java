package com.example.familymapclient;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import Model.Event;
import Model.Person;
import Requests.EventRequest;
import Requests.PersonRequest;
import Requests.RegisterRequest;
import Responses.EventResponse;
import Responses.PersonResponse;
import Responses.RegisterResponse;

public class DataCashTest {
    serverProxy proxy;
    DataCash myCash;
    String personID;

    @Before
    public void setUp() {
        myCash = DataCash.getInstance();
        String username = "Ironman";
        String password = "Avengers";
        RegisterRequest request = new RegisterRequest();
        request.setUserName(username);
        request.setPassword(password);
        request.setFirstName("Tony");
        request.setLastName("Stark");
        request.setGender("m");
        request.setEmail("StarkIndustries@mail");
        proxy = new serverProxy();
        URL registerURL = null;
        URL personURL = null;
        URL eventURL = null;
        try {
            registerURL = new URL("http://localhost:8080/user/register");
            personURL = new URL("http://localhost:8080/person");
            eventURL = new URL("http://localhost:8080/event");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        RegisterResponse registerResponse = proxy.getRegisterResponse(registerURL, request);
        personID = registerResponse.getPersonID();
        PersonResponse personResponse = proxy.getPersonResponse(personURL, new PersonRequest(registerResponse.getAuthToken()));
        myCash.addPeople(personResponse.getData());
        EventResponse eventResponse = proxy.getEventResponse(eventURL, new EventRequest(registerResponse.getAuthToken()));
        myCash.addEvents(eventResponse.getData());

        myCash.setFatherSideChecked(true);
        myCash.setMotherSideChecked(true);
        myCash.setMaleChecked(true);
        myCash.setFemaleChecked(true);
    }


    @After
    public void tearDown() {
        URL clearURL = null;
        try {
            clearURL = new URL("http://localhost:8080/clear");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        proxy.clear(clearURL);
    }

    @Test
    public void testGetLifeEvents() {
        ArrayList<Event> lifeEvents = myCash.getLifeEvents(personID);
        for (int i = 1; i < lifeEvents.size(); i++) {
            assertTrue(lifeEvents.get(i-1).getYear() > lifeEvents.get(i).getYear());
        }
    }

    @Test
    public void testGetFamily() {
        ArrayList<Person> family = myCash.getFamily(personID);
        Person user = myCash.getPerson(personID);
        assertTrue(family.contains(myCash.getPerson(user.getFatherID())));
        assertTrue(family.contains(myCash.getPerson(user.getMotherID())));
    }

    @Test
    public void testGetFilteredEvents() {
        Person user = myCash.getPerson(personID);
        ArrayList<Event> filteredEvents;

        myCash.setMaleChecked(false);
        filteredEvents = myCash.filterEvents();
        for (Event event : filteredEvents) {
            Person person = myCash.getPerson(event);
            assertFalse(person.getGender().equals("m"));
        }
        myCash.setMaleChecked(true);

        myCash.setFemaleChecked(false);
        filteredEvents = myCash.filterEvents();
        for (Event event : filteredEvents) {
            Person person = myCash.getPerson(event);
            assertFalse(person.getGender().equals("f"));
        }

        myCash.setFemaleChecked(true);
        myCash.setMotherSideChecked(false);
        filteredEvents = myCash.filterEvents();
        Person father = myCash.getPerson(user.getFatherID());
        assertFalse(filteredEvents.contains(myCash.getEarliestEvent(user.getMotherID())));
        assertTrue(filteredEvents.contains(myCash.getEarliestEvent(father.getPersonID())));
        assertTrue(filteredEvents.contains(myCash.getEarliestEvent(father.getFatherID())));
        assertTrue(filteredEvents.contains(myCash.getEarliestEvent(father.getMotherID())));
        myCash.setMotherSideChecked(true);

        myCash.setFatherSideChecked(false);
        filteredEvents = myCash.filterEvents();
        Person mother = myCash.getPerson(user.getMotherID());
        assertFalse(filteredEvents.contains(myCash.getEarliestEvent(user.getFatherID())));
        assertTrue(filteredEvents.contains(myCash.getEarliestEvent(mother.getPersonID())));
        assertTrue(filteredEvents.contains(myCash.getEarliestEvent(mother.getFatherID())));
        assertTrue(filteredEvents.contains(myCash.getEarliestEvent(mother.getMotherID())));
    }

    @Test
    public void testSearchData () {
        String userSearch = "a";
        ArrayList<Person> people = new ArrayList<>();
        ArrayList<Event> events = new ArrayList<>();
        myCash.searchData(userSearch, people, events);
        assertNotNull(people);
        assertNotNull(events);
        userSearch = "tony";
        myCash.searchData(userSearch, people, events);
        assertTrue(people.contains(myCash.getPerson(personID)));
    }
}