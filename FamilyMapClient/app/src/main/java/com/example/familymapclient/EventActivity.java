package com.example.familymapclient;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.familymapclient.fragments.MapsFragment;

public class EventActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        DataCash myCash = DataCash.getInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.eventActivity);

        String eventID = getIntent().getExtras().getString("eventID");
        if (eventID != null) {
            myCash.setCurrentEvent(myCash.getEvent(eventID));
        }

        if (fragment == null) {
            fragment = new MapsFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.eventActivity, fragment)
                    .commit();
        }
    }
}
