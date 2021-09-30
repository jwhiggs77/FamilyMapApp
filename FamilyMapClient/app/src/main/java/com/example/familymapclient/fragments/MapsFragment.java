package com.example.familymapclient.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.familymapclient.DataCash;
import com.example.familymapclient.MainActivity;
import com.example.familymapclient.PersonActivity;
import com.example.familymapclient.R;
import com.example.familymapclient.SearchActivity;
import com.example.familymapclient.SettingsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import Model.Event;
import Model.Person;

public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private DataCash myCash = DataCash.getInstance();;
    private Event selectedEventGlobal;
    private static View passView;
    ArrayList<Polyline> polylines;
    ArrayList<String> eventTypes = new ArrayList<>();
    HashMap<String, String> eventColors = new HashMap<>();

    public MapsFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        View view = layoutInflater.inflate(R.layout.fragment_maps, container, false);
        passView = view;
        polylines = new ArrayList<>();

        if (myCash.getCurrentEvent() != null) {
            selectedEventGlobal = myCash.getCurrentEvent();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::onMapReady);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);

        ArrayList<Event> events = loadEvents();
        makeMarkers(events);
        loadSelectedMarker(events);

        LinearLayout bottomOfScreen = (LinearLayout) passView.findViewById(R.id.bottomOfScreen);
        bottomOfScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start person activity
                if (selectedEventGlobal != null) {
                    Intent intent = new Intent(getActivity(), PersonActivity.class);
                    Bundle bundle  = new Bundle();
                    bundle.putString("personID", selectedEventGlobal.getPersonID());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onMapLoaded() {
        // You probably don't need this callback. It occurs after onMapReady and I have seen
        // cases where you get an error when adding markers or otherwise interacting with the map in
        // onMapReady(...) because the map isn't really all the way ready. If you see that, just
        // move all code where you interact with the map (everything after
        // map.setOnMapLoadedCallback(...) above) to here.
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map != null) {
            map.clear();
            ArrayList<Event> events = loadEvents();
            makeMarkers(events);
            loadSelectedMarker(events);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.map_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.settingsIcon) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (item.getItemId() == R.id.searchIcon) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Event> loadEvents() {
        ArrayList<Event> events;
        if (myCash.getMaleChecked() && myCash.getFemaleChecked() && myCash.getFatherSideChecked() && myCash.getMotherSideChecked()) {
            events = myCash.getEvents();
        }
        else {
            events = myCash.filterEvents();
        }
        return events;
    }

    private void makeMarkers(ArrayList<Event> events) {
        eventTypes.add("birth");
        eventColors.put("birth", "#1E90FF");
        eventTypes.add("marriage");
        eventColors.put("marriage", "#2eb82e");
        eventTypes.add("death");
        eventColors.put("death", "#333333");

        for (int i = 0; i < events.size(); i++) {
            getLatLng(events.get(i));
            LatLng eventPosition = getLatLng(events.get(i));;

            if (eventTypes.contains(events.get(i).getEventType().toLowerCase())) {
                String color = eventColors.get(events.get(i).getEventType().toLowerCase());
                map.addMarker(new MarkerOptions().position(eventPosition).title(events.get(i).getEventType()).icon(getMarkerIcon(color))).setTag(events.get(i));
            }
            else {
                eventTypes.add(events.get(i).getEventType().toLowerCase());
                Random rnd = new Random();
                int rand_num = rnd.nextInt(0xffffff + 1);
                String colorCode = String.format("#%06x", rand_num);
                eventColors.put(events.get(i).getEventType().toLowerCase(), colorCode);
                map.addMarker(new MarkerOptions().position(eventPosition).title(events.get(i).getEventType()).icon(getMarkerIcon(colorCode))).setTag(events.get(i));
            }
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Event selectedEvent = (Event) marker.getTag();
                selectedEventGlobal = selectedEvent;
                clickMarker(passView);
                return false;
            }
        });
    }

    public void clickMarker(View v){
        ArrayList<Event> events = loadEvents();
        DataCash myCash = DataCash.getInstance();
        Person selectedPerson = myCash.getPerson(selectedEventGlobal);
        ImageView img = (ImageView) v.findViewById(R.id.genderImage);
        int width = 10;
        for (Polyline line : polylines) {
            line.remove();
        }

        //create spouse lines
        Event spouseEvent = myCash.getEarliestEvent(selectedPerson.getSpouseID());
        if (spouseEvent != null && myCash.getSpouseLinesChecked() && events.contains(spouseEvent)) {
            Polyline line = map.addPolyline(new PolylineOptions().add(getLatLng(selectedEventGlobal), getLatLng(spouseEvent))
                    .width(width)
                    .color(Color.GREEN));
            polylines.add(line);
        }

        //create life story lines
        ArrayList<Event> lifeEvents = myCash.getLifeEvents(selectedPerson.getPersonID());
        if (lifeEvents.size() > 1 && myCash.getLifeLinesChecked()) {
            for (int i = 1; i < lifeEvents.size(); i++) {
                Polyline line = map.addPolyline(new PolylineOptions().add(getLatLng(lifeEvents.get(i - 1)), getLatLng(lifeEvents.get(i)))
                        .width(width)
                        .color(Color.BLUE));
                polylines.add(line);
            }
        }

        //create generational lines
        if (myCash.getFamilyLinesChecked()) {
            makeGenLines(selectedPerson, selectedEventGlobal, width);
        }

        //set bottom screen
        if (selectedPerson.getGender().equals("m")) {
            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.male).sizeDp(45);
            img.setImageDrawable(genderIcon);
        } else {
            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.femaleColor).sizeDp(45);
            img.setImageDrawable(genderIcon);
        }

        TextView textTop = (TextView) v.findViewById(R.id.textTop);
        TextView textBottom = (TextView) v.findViewById(R.id.textBottom);

        String topText = selectedPerson.getFirstName() + " " + selectedPerson.getLastName();
        String bottomText = selectedEventGlobal.getEventType() + ": " + selectedEventGlobal.getCity() + ", " + selectedEventGlobal.getCountry() + " (" + selectedEventGlobal.getYear() + ")";
        textTop.setText(topText);
        textBottom.setText(bottomText);

        if (getActivity() instanceof MainActivity) {
            myCash.setCurrentEvent(selectedEventGlobal);
        }
    }

    private LatLng getLatLng(Event event) {
        double lat = event.getLatitude();
        double lng = event.getLongitude();
        return new LatLng(lat, lng);
    }

    private void makeGenLines(Person person, Event personEvent, int width) {
        ArrayList<Event> events = loadEvents();
        Event fatherEvent = myCash.getEarliestEvent(person.getFatherID());
        Event motherEvent = myCash.getEarliestEvent(person.getMotherID());

        if (width <= 0){
            width = 1;
        }
        if (person.getFatherID() != null && events.contains(fatherEvent)) {
            Polyline line = map.addPolyline(new PolylineOptions().add(getLatLng(personEvent), getLatLng(fatherEvent))
                    .width(width)
                    .color(Color.RED));
            polylines.add(line);
        }
        if (person.getMotherID() != null && events.contains(motherEvent)) {
            Polyline line = map.addPolyline(new PolylineOptions().add(getLatLng(personEvent), getLatLng(motherEvent))
                    .width(width)
                    .color(Color.RED));
            polylines.add(line);
        }
        if (person.getFatherID() != null) {
            makeGenLines(myCash.getPerson(person.getFatherID()), fatherEvent, width - 3);
        }
        if (person.getMotherID() != null) {
            makeGenLines(myCash.getPerson(person.getMotherID()), motherEvent, width - 3);
        }
    }

    private void loadSelectedMarker(ArrayList<Event> events) {
        if (!events.contains(selectedEventGlobal)) {
            selectedEventGlobal = null;
        }
        if (selectedEventGlobal != null) {
            clickMarker(passView);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(getLatLng(selectedEventGlobal),1));
        }
    }

    public BitmapDescriptor getMarkerIcon(String color) {
        float[] cBit = new float[3];
        Color.colorToHSV(Color.parseColor(color), cBit);
        return BitmapDescriptorFactory.defaultMarker(cBit[0]);
    }
}