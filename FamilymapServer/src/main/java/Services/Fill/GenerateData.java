package Services.Fill;

import DAO.DatabaseException;
import DAO.EventDAO;
import DAO.PersonDAO;
import Model.Event;
import Model.Person;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.*;

public class GenerateData {
    String[] maleFirstNames;
    String[] femaleFirstNames;
    String[] lastNames;
    Location[] locationArray;
    PersonDAO personDAO;
    EventDAO eventDAO;
    int numPersons = 1;
    int numEvents = 0;

    public GenerateData() throws DatabaseException {
        Gson gson = new Gson();
        try {
            Reader maleNames = new FileReader("/Users/joshuahiggins/Downloads/FamilyMapServerStudent-master/json/mnames.json");
            JsonObject maleJson = gson.fromJson(maleNames, JsonObject.class);
            maleFirstNames = gson.fromJson(maleJson.get("data"), String[].class);

            Reader femaleNames = new FileReader("/Users/joshuahiggins/Downloads/FamilyMapServerStudent-master/json/fnames.json");
            JsonObject femaleJson = gson.fromJson(femaleNames, JsonObject.class);
            femaleFirstNames = gson.fromJson(femaleJson.get("data"), String[].class);

            Reader sNames = new FileReader("/Users/joshuahiggins/Downloads/FamilyMapServerStudent-master/json/snames.json");
            JsonObject surJson = gson.fromJson(sNames, JsonObject.class);
            lastNames = gson.fromJson(surJson.get("data"), String[].class);

            Reader locations = new FileReader("/Users/joshuahiggins/Downloads/FamilyMapServerStudent-master/json/locations.json");
            JsonObject locJson = gson.fromJson(locations, JsonObject.class);
            locationArray = gson.fromJson(locJson.get("data"), Location[].class);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new DatabaseException("FileNotFoundException: unable to locate json files");
        }
    }

    public void FillGenerations(Person rootPerson, int numGenerations) throws DatabaseException {
        CreateGenerations(rootPerson, numGenerations);
    }

    private void CreateGenerations(Person child, int numGenerations) throws DatabaseException {
        if (child == null) throw new DatabaseException("Error: User does not exist in the database");
        String lastName = MakeLastName();
        Person father = CreateMale(child.getAssociatedUsername(), lastName);
        Person mother = CreateFemale(child.getAssociatedUsername(), lastName);
        child.setMotherID(mother.getPersonID());
        father.setSpouseID(mother.getPersonID());
        mother.setSpouseID(father.getPersonID());

        try {
            personDAO.addFather(child.getPersonID(), father.getPersonID());
            personDAO.addMother(child.getPersonID(), mother.getPersonID());
            personDAO.insertPerson(father);
            personDAO.insertPerson(mother);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        CreateBirth(father, child, eventDAO);
        CreateBirth(mother, child, eventDAO);
        CreateMarriage(father, mother, child, eventDAO);
        CreateDeath(father, child, eventDAO);
        CreateDeath(mother, child, eventDAO);
        numGenerations--;

        //recursion
        if (numGenerations > 0) {
            CreateGenerations(father, numGenerations);
            CreateGenerations(mother, numGenerations);
        }
    }

    public void SetPersonDAO(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    Person CreateMale(String associatedUsername, String lastName) {
        Person newPerson = new Person();
        newPerson.setPersonID(UUID.randomUUID().toString());
        newPerson.setAssociatedUsername(associatedUsername);
        newPerson.setFirstName(MakeFirstName("male"));
        newPerson.setLastName(lastName);
        newPerson.setGender("m");

        numPersons++;
        return newPerson;
    }

    Person CreateFemale(String associatedUsername, String lastName) {
        Person newPerson = new Person();
        newPerson.setPersonID(UUID.randomUUID().toString());
        newPerson.setAssociatedUsername(associatedUsername);
        newPerson.setFirstName(MakeFirstName("female"));
        newPerson.setLastName(lastName);
        newPerson.setGender("f");

        numPersons++;
        return newPerson;
    }

    String MakeFirstName(String gender) {
        Random random = new Random();
        String firstName = null;
        try {
            if (gender.equals("male")) {
                firstName = maleFirstNames[random.nextInt(maleFirstNames.length)];
            } else if (gender.equals("female")) {
                firstName = femaleFirstNames[random.nextInt(femaleFirstNames.length)];
            } else {
                throw new IOException("Gender for MakeFirstName was set incorrectly");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return firstName;
    }

    String MakeLastName() {
        Random random = new Random();
        String lastName = null;
        lastName = lastNames[random.nextInt(lastNames.length)];
        return lastName;
    }

    public void SetEventDAO(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    void CreateBirth(Person person, Person child, EventDAO eventDAO) {
        Event birth = new Event();

        try {
            birth.setEventID(UUID.randomUUID().toString());
            birth.setAssociatedUsername(person.getAssociatedUsername());
            birth.setPersonID(person.getPersonID());
            MakeLocation(birth);
            birth.setEventType("Birth");
            int childBirthYear = eventDAO.getEventYear(child.getPersonID(), "Birth");
            int upperBound = 18;
            int lowerBound = 35;
            birth.setYear(((int)(Math.random() * (/*max*/(childBirthYear - upperBound) - /*min*/(childBirthYear - lowerBound) + 1)) + /*min*/(childBirthYear - lowerBound)));
            eventDAO.insertEvent(birth);
            numEvents++;
        } catch (DatabaseException e) {
            e.printStackTrace();
            e.toString();
        }
    }

    void CreateMarriage(Person husband, Person wife, Person child, EventDAO eventDAO) {
        Event marriageH = new Event();
        Event marriageW = new Event();
        Random random = new Random();

        try {
            marriageH.setEventID(UUID.randomUUID().toString());
            marriageW.setEventID(UUID.randomUUID().toString());
            marriageH.setAssociatedUsername(husband.getAssociatedUsername());
            marriageW.setAssociatedUsername(wife.getAssociatedUsername());
            marriageH.setPersonID(husband.getPersonID());
            marriageW.setPersonID(wife.getPersonID());
            Location location = locationArray[random.nextInt(locationArray.length)];
            marriageH.setLatitude(location.getLatitude());
            marriageW.setLatitude(location.getLatitude());
            marriageH.setLongitude(location.getLongitude());
            marriageW.setLongitude(location.getLongitude());
            marriageH.setCountry(location.getCountry());
            marriageW.setCountry(location.getCountry());
            marriageH.setCity(location.getCity());
            marriageW.setCity(location.getCity());
            marriageH.setEventType("Marriage");
            marriageW.setEventType("Marriage");
            int upperBound = eventDAO.getEventYear(child.getPersonID(), "Birth") - 1;
            int lowerBound;
            int husbandBirthYear = eventDAO.getEventYear(husband.getPersonID(), "Birth");
            int wifeBirthYear = eventDAO.getEventYear(wife.getPersonID(), "Birth");
            if (husbandBirthYear >= wifeBirthYear) {
                lowerBound = husbandBirthYear + 18;
            }
            else {
                lowerBound = wifeBirthYear + 18;
            }
            int year = ((int) (Math.random() * (/*max*/(upperBound) - /*min*/(lowerBound) + 1)) + /*min*/(lowerBound));
            marriageH.setYear(year);
            marriageW.setYear(year);

            eventDAO.insertEvent(marriageH);
            numEvents++;
            eventDAO.insertEvent(marriageW);
            numEvents++;
        } catch (DatabaseException e) {
            e.printStackTrace();
            e.toString();
        }
    }

    void CreateDeath(Person person, Person child, EventDAO eventDAO) {
        Event death = new Event();

        try {
            death.setEventID(UUID.randomUUID().toString());
            death.setAssociatedUsername(person.getAssociatedUsername());
            death.setPersonID(person.getPersonID());
            MakeLocation(death);
            death.setEventType("Death");
            int upperBound = eventDAO.getEventYear(person.getPersonID(), "Birth") + 120;
            int lowerBound = eventDAO.getEventYear(child.getPersonID(), "Birth") + 1;
            //int personsMarriageYear = eventDAO.getEventYear(person.getPersonID(), "Marriage");
            death.setYear(((int) (Math.random() * (/*max*/(upperBound) - /*min*/(lowerBound) + 1)) + /*min*/(lowerBound)));
            eventDAO.insertEvent(death);
            numEvents++;
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public void MakeLocation(Event event) {
        Random random = new Random();
        Location location = locationArray[random.nextInt(locationArray.length)];
        event.setLatitude(location.getLatitude());
        event.setLongitude(location.getLongitude());
        event.setCountry(location.getCountry());
        event.setCity(location.getCity());
    }

    public int getNumPersons() {
        return numPersons;
    }

    public int getNumEvents() {
        return numEvents;
    }
}