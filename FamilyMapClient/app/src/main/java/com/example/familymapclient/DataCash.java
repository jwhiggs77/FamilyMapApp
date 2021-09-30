package com.example.familymapclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Model.AuthToken;
import Model.Event;
import Model.Person;

public class DataCash {
    private static DataCash myCash = new DataCash();
    private String currAuthToken = null;
    private ArrayList<Person> personArrayList;
    private ArrayList<Event> eventArrayList;
    private ArrayList<Event> filteredEvents;
    private ArrayList<Person> fatherSide;
    private ArrayList<Person> motherSide;
    private Boolean isLifeLinesChecked = true;
    private Boolean isFamilyLinesChecked = true;
    private Boolean isSpouseLinesChecked = true;
    private Boolean isFatherSideChecked = true;
    private Boolean isMotherSideChecked = true;
    private Boolean isMaleChecked = true;
    private Boolean isFemaleChecked = true;
    private Event currentEvent = null;

    private DataCash() {
        this.personArrayList = new ArrayList<>();
        this.eventArrayList = new ArrayList<>();
        this.filteredEvents = new ArrayList<>();
        this.fatherSide = new ArrayList<>();
        this.motherSide = new ArrayList<>();
    }

    public void clearData() {
        myCash = null;
    }

    public static DataCash getInstance() {
        if (myCash == null) {
            return new DataCash();
        }
        else {
            return myCash;
        }
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    public String getCurrAuthToken() {
        return currAuthToken;
    }

    public void setCurrAuthToken(String currAuthToken) {
        this.currAuthToken = currAuthToken;
    }

    public void addPeople(ArrayList<Person> data) {
        personArrayList = data;
    }

    public ArrayList<Person> getPeople() {
        return personArrayList;
    }

    public Person getPerson(Event event) {
        for (int i = 0; i < personArrayList.size(); i++) {
            if (personArrayList.get(i).getPersonID().equals(event.getPersonID())) {
                return personArrayList.get(i);
            }
        }
        return null;
    }

    public Person getPerson(String personID) {
        for (int i = 0; i < personArrayList.size(); i++) {
            if (personArrayList.get(i).getPersonID().equals(personID)) {
                return personArrayList.get(i);
            }
        }
        return null;
    }

    public void addEvents(ArrayList<Event> data) {
        eventArrayList = data;
    }

    public Event getEvent(String eventID) {
        for (int i = 0; i < eventArrayList.size(); i++) {
            if (eventArrayList.get(i).getEventID().equals(eventID)) {
                return eventArrayList.get(i);
            }
        }
        return null;
    }

    public ArrayList<Event> getEvents() {
        return  eventArrayList;
    }

    public Event getEarliestEvent(String personID) {
        Event earliestEvent = null;

        for (int i = 0; i < eventArrayList.size(); i++) {
            if (eventArrayList.get(i).getPersonID().equals(personID)) {
                if (eventArrayList.get(i).getEventType().equals("Birth")) {
                    return eventArrayList.get(i);
                }
                else {
                    if (earliestEvent == null || earliestEvent.getYear() > eventArrayList.get(i).getYear()) {
                        earliestEvent = eventArrayList.get(i);
                    }
                }
            }
        }
        return earliestEvent;
    }

    public ArrayList<Event> getLifeEvents(String personID) {
        ArrayList<Event> eventList = myCash.filterEvents();
        ArrayList<Event> lifeEvents = new ArrayList<>();

        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getPersonID().equals(personID)) {
                lifeEvents.add(eventList.get(i));
            }
        }
        Collections.sort(lifeEvents, new SortEvents());
        return lifeEvents;
    }

    public String getUserFirstName() {
        return personArrayList.get(0).getFirstName();
    }

    public String getUserLastName() {
        return personArrayList.get(0).getLastName();
    }

    /**
     * index 0 = father
     * index 1 = mother
     * index 2 = spouse
     * index 3+ = children
     * @param personID ID of the person selected
     * @return a list of the family members of the selected person
     */
    public ArrayList<Person> getFamily(String personID) {
        ArrayList<Person> family = new ArrayList<>();
        Person person = getPerson(personID);
        if (person.getFatherID() != null) {
            family.add(getPerson(person.getFatherID()));
        }
        if (person.getMotherID() != null) {
            family.add(getPerson(person.getMotherID()));
        }
        if (person.getSpouseID() != null) {
            family.add(getPerson(person.getSpouseID()));
        }

        if (person.getGender().equals("m")) {
            for (int i = 0; i < personArrayList.size(); i++) {
                if (personArrayList.get(i).getFatherID() != null) {
                    if (personArrayList.get(i).getFatherID().equals(personID)) {
                        family.add(personArrayList.get(i));
                    }
                }
            }
        }
        else {
            for (int i = 0; i < personArrayList.size(); i++) {
                if (personArrayList.get(i).getMotherID() != null) {
                    if (personArrayList.get(i).getMotherID().equals(personID)) {
                        family.add(personArrayList.get(i));
                    }
                }
            }
        }

        return family;
    }

    public Boolean getLifeLinesChecked() {
        return isLifeLinesChecked;
    }

    public void setLifeLinesChecked(Boolean lifeLinesChecked) {
        isLifeLinesChecked = lifeLinesChecked;
    }

    public Boolean getFamilyLinesChecked() {
        return isFamilyLinesChecked;
    }

    public void setFamilyLinesChecked(Boolean familyLinesChecked) {
        isFamilyLinesChecked = familyLinesChecked;
    }

    public Boolean getSpouseLinesChecked() {
        return isSpouseLinesChecked;
    }

    public void setSpouseLinesChecked(Boolean spouseLinesChecked) {
        isSpouseLinesChecked = spouseLinesChecked;
    }

    public Boolean getFatherSideChecked() {
        return isFatherSideChecked;
    }

    public void setFatherSideChecked(Boolean fatherSideChecked) {
        isFatherSideChecked = fatherSideChecked;
    }

    public Boolean getMotherSideChecked() {
        return isMotherSideChecked;
    }

    public void setMotherSideChecked(Boolean motherSideChecked) {
        isMotherSideChecked = motherSideChecked;
    }

    public Boolean getMaleChecked() {
        return isMaleChecked;
    }

    public void setMaleChecked(Boolean maleChecked) {
        isMaleChecked = maleChecked;
    }

    public Boolean getFemaleChecked() {
        return isFemaleChecked;
    }

    public void setFemaleChecked(Boolean femaleChecked) {
        isFemaleChecked = femaleChecked;
    }

    public ArrayList<Event> filterEvents() {
        filteredEvents.clear();
        fatherSide.clear();
        motherSide.clear();
        Person user = personArrayList.get(0);
        for (int i = 0; i < eventArrayList.size(); i++) {
            if (eventArrayList.get(i).getPersonID().equals(user.getPersonID()) || eventArrayList.get(i).getPersonID().equals(user.getSpouseID())) {
                filteredEvents.add(eventArrayList.get(i));
            }
        }
        if (getFatherSideChecked()) {
            organizeFathersSide();
            for (int i = 0; i < eventArrayList.size(); i++) {
                if (fatherSide.contains(getPerson(eventArrayList.get(i)))) {
                    filteredEvents.add(eventArrayList.get(i));
                }
            }
        }
        if (getMotherSideChecked()) {
            organizeMothersSide();
            for (int i = 0; i < eventArrayList.size(); i++) {
                if (motherSide.contains(getPerson(eventArrayList.get(i)))) {
                    filteredEvents.add(eventArrayList.get(i));
                }
            }
        }
        if (!getMaleChecked()) {
            filterGender("m");
        }
        if (!getFemaleChecked()) {
            filterGender("f");
        }
        return filteredEvents;
    }

    private void filterGender(String gender) {
        for (int i = 0; i < filteredEvents.size(); i++) {
            String tempGender = getPerson(filteredEvents.get(i)).getGender();
            if (tempGender.equals(gender)) {
                filteredEvents.remove(i);
                i--;
            }
        }
    }

    private void organizeFathersSide() {
        Person user = personArrayList.get(0);
        if (user.getFatherID() != null) {
            Person father = getPerson(user.getFatherID());
            fatherSide.add(father);
            organize(father, fatherSide);
        }
    }

    private void organizeMothersSide() {
        Person user = personArrayList.get(0);
        if (user.getMotherID() != null) {
            Person mother = getPerson(user.getMotherID());
            motherSide.add(mother);
            organize(mother, motherSide);
        }
    }

    private void organize(Person person, ArrayList<Person> side) {
        if (person.getFatherID() != null) {
            Person father = getPerson(person.getFatherID());
            side.add(father);
            organize(father, side);
        }
        if (person.getMotherID() != null) {
            Person mother = getPerson(person.getMotherID());
            side.add(mother);
            organize(mother, side);
        }
    }

    public void searchData(String userSearch, List<Person> searchPeopleResults, List<Event> searchEventResults) {
        List<Person> familyList = myCash.getPeople();
        List<Event> eventList;

        if (myCash.getMaleChecked() && myCash.getFemaleChecked() && myCash.getFatherSideChecked() && myCash.getMotherSideChecked()) {
            eventList = myCash.getEvents();
        }
        else {
            eventList = myCash.filterEvents();
        }

        for (int i = 0; i < familyList.size(); i++) {
            if (familyList.get(i).getFirstName().toLowerCase().contains(userSearch)) {
                searchPeopleResults.add(familyList.get(i));
            }
            if (familyList.get(i).getLastName().toLowerCase().contains(userSearch)) {
                searchPeopleResults.add(familyList.get(i));
            }
        }
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getCity().toLowerCase().contains(userSearch)) {
                searchEventResults.add(eventList.get(i));
            }
            if (eventList.get(i).getCountry().toLowerCase().contains(userSearch)) {
                searchEventResults.add(eventList.get(i));
            }
            if (eventList.get(i).getEventType().toLowerCase().contains(userSearch)) {
                searchEventResults.add(eventList.get(i));
            }
            if (Integer.toString(eventList.get(i).getYear()).contains(userSearch)) {
                searchEventResults.add(eventList.get(i));
            }
        }
    }
}
