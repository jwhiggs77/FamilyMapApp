package com.example.familymapclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;

import Model.Event;
import Model.Person;

public class PersonActivity extends AppCompatActivity {
    DataCash myCash = DataCash.getInstance();
    String personID;
    private Context context = this;
    private static final int GROUP_COUNT = 2;
    private static final int EVENT_POSITION = 0;
    private static final int PERSON_POSITION = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_screen);
        DataCash myCash = DataCash.getInstance();
        personID = getIntent().getExtras().getString("personID");
        Person person = myCash.getPerson(personID);
        List<Event> eventList = myCash.getLifeEvents(personID);
        List<Person> familyList = myCash.getFamily(personID);
        TextView firstNameTextView = findViewById(R.id.firstName);
        TextView lastNameTextView = findViewById(R.id.lastName);
        TextView genderTextView = findViewById(R.id.gender);
        ExpandableListView expandableListView = findViewById(R.id.expandableListView);

        firstNameTextView.setText(person.getFirstName());
        lastNameTextView.setText(person.getLastName());
        if (person.getGender().equals("m")) {
            genderTextView.setText("Male");
        }
        else {
            genderTextView.setText("Female");
        }

        expandableListView.setAdapter(new ExpandableListAdapter(eventList, familyList));

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (groupPosition == EVENT_POSITION) {
                    Intent intent = new Intent(context, EventActivity.class);
                    Bundle bundle  = new Bundle();
                    bundle.putString("eventID", eventList.get(childPosition).getEventID());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if (groupPosition == PERSON_POSITION) {
                    Intent intent = new Intent(context, PersonActivity.class);
                    Bundle bundle  = new Bundle();
                    bundle.putString("personID", familyList.get(childPosition).getPersonID());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else {
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
                }

                return false;
            }
        });
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {
        private List<Event> eventList;
        private List<Person> familyList;

        ExpandableListAdapter(List<Event> eventList, List<Person> familyList) {
            this.eventList = eventList;
            this.familyList = familyList;
        }

        @Override
        public int getGroupCount() {
            return GROUP_COUNT;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (groupPosition == EVENT_POSITION) {
                return eventList.size();
            }
            else if (groupPosition == PERSON_POSITION) {
                return familyList.size();
            }
            else {
                throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            if (groupPosition == EVENT_POSITION) {
                return getString(R.string.person_activity_event_title);
            }
            else if (groupPosition == PERSON_POSITION) {
                return getString(R.string.person_activity_person_title);
            }
            else {
                throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            if (groupPosition == EVENT_POSITION) {
                return eventList.get(childPosition);
            }
            else if (groupPosition == PERSON_POSITION) {
                return familyList.get(childPosition);
            }
            else {
                throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }
            TextView titleView = convertView.findViewById(R.id.listTitle);

            if (groupPosition == EVENT_POSITION) {
                titleView.setText(R.string.person_activity_event_title);
            }
            else if (groupPosition == PERSON_POSITION) {
                titleView.setText(R.string.person_activity_person_title);
            }
            else {
                throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            if (groupPosition == EVENT_POSITION) {
                itemView = getLayoutInflater().inflate(R.layout.event_item, parent, false);
                initializeEventListView(itemView, childPosition);
            }
            else if (groupPosition == PERSON_POSITION) {
                itemView = getLayoutInflater().inflate(R.layout.person_item, parent, false);
                initializePersonListView(itemView, childPosition);
            }
            else {
                throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeEventListView(View eventItemView, final int childPosition) {
            Event event = eventList.get(childPosition);
            Person person = myCash.getPerson(eventList.get(childPosition).getPersonID());
            String eventInfo = event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")";
            String personName = person.getFirstName() + " " + person.getFirstName();

            TextView eventText = eventItemView.findViewById(R.id.eventInfo);
            eventText.setText(eventInfo);
            TextView personNameText = eventItemView.findViewById(R.id.nameOfPersonConnectedToEvent);
            personNameText.setText(personName);
            ImageView img = (ImageView) eventItemView.findViewById(R.id.personItemMarker);
            Drawable eventIcon = new IconDrawable(context, FontAwesomeIcons.fa_map_marker).colorRes(R.color.green).sizeDp(35);
            img.setImageDrawable(eventIcon);
        }

        private void initializePersonListView(View personItemView, final int childPosition) {
            Person relative = myCash.getPerson(familyList.get(childPosition).getPersonID());
            String personName = relative.getFirstName() + " " + relative.getLastName();
            ImageView img = (ImageView) personItemView.findViewById(R.id.personItemMarker);

            if (relative != null) {
                TextView personNameText = personItemView.findViewById(R.id.nameOfFamilymember);
                personNameText.setText(personName);

                if (relative.getGender().equals("m")) {
                    Drawable genderIcon = new IconDrawable(context, FontAwesomeIcons.fa_male).colorRes(R.color.male).sizeDp(35);
                    img.setImageDrawable(genderIcon);
                } else {
                    Drawable genderIcon = new IconDrawable(context, FontAwesomeIcons.fa_female).colorRes(R.color.femaleColor).sizeDp(35);
                    img.setImageDrawable(genderIcon);
                }

                TextView familyTitleText = personItemView.findViewById(R.id.relationshipOfFamilyMember);
                Person person = myCash.getPerson(personID);

                if (person.getFatherID() != null && person.getFatherID().equals(relative.getPersonID())) {
                    familyTitleText.setText("Father");
                } else if (person.getMotherID() != null && person.getMotherID().equals(relative.getPersonID())) {
                    familyTitleText.setText("Mother");
                } else if (person.getSpouseID() != null && person.getSpouseID().equals(relative.getPersonID())) {
                    familyTitleText.setText("Spouse");
                } else {
                    familyTitleText.setText("Child");
                }
            }
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
