package com.example.familymapclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    DataCash myCash = DataCash.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView logout = findViewById(R.id.logout);
        Switch lifeStoryLines = findViewById(R.id.lifeStoryLines);
        Switch familyTreeLines = findViewById(R.id.familyTreeLines);
        Switch spouseLines = findViewById(R.id.spouseLines);
        Switch fatherSide = findViewById(R.id.fatherSide);
        Switch motherSide = findViewById(R.id.motherSide);
        Switch maleEvents = findViewById(R.id.maleEvents);
        Switch femaleEvents = findViewById(R.id.femaleEvents);
        if (myCash.getLifeLinesChecked()) {
            lifeStoryLines.setChecked(true);
        }
        if (myCash.getFamilyLinesChecked()) {
            familyTreeLines.setChecked(true);
        }
        if (myCash.getSpouseLinesChecked()) {
            spouseLines.setChecked(true);
        }
        if (myCash.getFatherSideChecked()) {
            fatherSide.setChecked(true);
        }
        if (myCash.getMotherSideChecked()) {
            motherSide.setChecked(true);
        }
        if (myCash.getMaleChecked()) {
            maleEvents.setChecked(true);
        }
        if (myCash.getFemaleChecked()) {
            femaleEvents.setChecked(true);
        }

        lifeStoryLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myCash.setLifeLinesChecked(true);
                }
                else {
                    myCash.setLifeLinesChecked(false);
                }
            }
        });

        familyTreeLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myCash.setFamilyLinesChecked(true);
                }
                else {
                    myCash.setFamilyLinesChecked(false);
                }
            }
        });

        spouseLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myCash.setSpouseLinesChecked(true);
                }
                else {
                    myCash.setSpouseLinesChecked(false);
                }
            }
        });

        fatherSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myCash.setFatherSideChecked(true);
                }
                else {
                    myCash.setFatherSideChecked(false);
                }
            }
        });

        motherSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myCash.setMotherSideChecked(true);
                }
                else {
                    myCash.setMotherSideChecked(false);
                }
            }
        });

        maleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myCash.setMaleChecked(true);
                }
                else {
                    myCash.setMaleChecked(false);
                }
            }
        });

        femaleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myCash.setFemaleChecked(true);
                }
                else {
                    myCash.setFemaleChecked(false);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCash.clearData();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }
}
