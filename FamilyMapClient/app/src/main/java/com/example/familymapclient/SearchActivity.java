package com.example.familymapclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;

import Model.Event;
import Model.Person;

public class SearchActivity extends AppCompatActivity {
    private Context context = this;
    String userSearch = new String();
    private static final int PERSON_ITEM_VIEW_TYPE = 0;
    private static final int EVENT_ITEM_VIEW_TYPE = 1;
    DataCash myCash = DataCash.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);
        List<Person> searchPeopleResults = new ArrayList<>();
        List<Event> searchEventResults = new ArrayList<>();

        RecyclerView searchResults = findViewById(R.id.searchRecycler);
        searchResults.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        EditText searchBox = findViewById(R.id.editTextSearchBox);
        TextWatcher watchSearchBoxEditText = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userSearch = s.toString().toLowerCase();
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchPeopleResults.clear();
                searchEventResults.clear();

                myCash.searchData(userSearch, searchPeopleResults, searchEventResults);

                SearchAdapter adapter = new SearchAdapter(searchPeopleResults, searchEventResults);
                searchResults.setAdapter(adapter);
            }
        };
        searchBox.addTextChangedListener(watchSearchBoxEditText);
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
        private List<Person> familyList;
        private List<Event> eventList;

        SearchAdapter(List<Person> familyList, List<Event> eventList) {
            this.familyList = familyList;
            this.eventList = eventList;
        }

        @Override
        public int getItemViewType(int position) {
            return position < familyList.size() ? PERSON_ITEM_VIEW_TYPE : EVENT_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.person_item, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.event_item, parent, false);
            }

            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if (position < familyList.size()) {
                holder.bind(familyList.get(position));
            }
            else {
                holder.bind(eventList.get(position - familyList.size()));
            }
        }

        @Override
        public int getItemCount() {
            return familyList.size() + eventList.size();
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final int viewType;
        TextView personName;
        String gender = new String();
        String personID = new String();
        TextView eventInfo;
        String eventID = new String();
        ImageView img;

        public SearchViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            itemView.setOnClickListener(this);
            img = (ImageView) itemView.findViewById(R.id.personItemMarker);

            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                personName = itemView.findViewById(R.id.nameOfFamilymember);
            }
            else {
                Drawable eventIcon = new IconDrawable(context, FontAwesomeIcons.fa_map_marker).colorRes(R.color.green).sizeDp(35);
                img.setImageDrawable(eventIcon);
                eventInfo = itemView.findViewById(R.id.eventInfo);
                personName = itemView.findViewById(R.id.nameOfPersonConnectedToEvent);
            }
        }

        private void bind(Person person) {
            this.gender = person.getGender();
            this.personID = person.getPersonID();
            personName.setText(person.getFirstName() + " " + person.getLastName());
            if (gender.equals("m")) {
                Drawable genderIcon = new IconDrawable(context, FontAwesomeIcons.fa_male).colorRes(R.color.male).sizeDp(45);
                img.setImageDrawable(genderIcon);
            } else {
                Drawable genderIcon = new IconDrawable(context, FontAwesomeIcons.fa_female).colorRes(R.color.femaleColor).sizeDp(45);
                img.setImageDrawable(genderIcon);
            }
        }

        private void bind(Event event) {
            this.eventID = event.getEventID();
            eventInfo.setText(event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")");
        }

        @Override
        public void onClick(View v) {
            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("personID", personID);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                myCash.setCurrentEvent(myCash.getEvent(eventID));
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                startActivity(intent);
            }
        }
    }
}
