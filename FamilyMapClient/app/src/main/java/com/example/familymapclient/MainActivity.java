package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;


import com.example.familymapclient.fragments.LoginRegisterFragment;
import com.example.familymapclient.fragments.MapsFragment;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Iconify.with(new FontAwesomeModule());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        LoginRegisterFragment loginFrag = new LoginRegisterFragment();
        MapsFragment mapFrag = new MapsFragment();
        String token = DataCash.getInstance().getCurrAuthToken();

        if (token == null) {
            fragmentManager.beginTransaction().add(R.id.activityMain, loginFrag).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.activityMain, mapFrag).commit();
        }
    }

    public void sendMessage(View view) {

    }
}