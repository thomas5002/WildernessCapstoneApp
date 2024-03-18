package com.example.yusefcapstione;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    // Declare UI elements
    private Button HRBTN, BRBTN, GRAPHBTN, SETTINGSBTN, CAMERABTN, backbutton, GRAPH2BTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        HRBTN = findViewById(R.id.hrBTN);
        BRBTN = findViewById(R.id.brBTN);
        GRAPHBTN = findViewById(R.id.GraphBTN);
        GRAPH2BTN = findViewById( R.id.Graph2BTN);
        SETTINGSBTN = findViewById(R.id.SettingBTN);
        CAMERABTN = findViewById(R.id.CameraBTN);
        backbutton = findViewById(R.id.backBTN);

        // Set click listeners for buttons

        // Heart Rate button
        HRBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HeartRate_Activity.class);
                startActivity(intent);
            }
        });

        // Breath Rate button
        BRBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BreathRate_Activity.class);
                startActivity(intent);
            }
        });

        // Graph button
        GRAPHBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GraphMain.class);
                startActivity(intent);
            }
        });

        // Second Graph button
        GRAPH2BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GraphMain2.class);
                startActivity(intent);
            }
        });

        // Settings button
        SETTINGSBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraRoll_Activity.class);
                startActivity(intent);
            }
        });

        // Camera button
        CAMERABTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Camera_Activity.class);
                startActivity(intent);
            }
        });

        // Back button
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Profile_Page.class);
                startActivity(intent);
            }
        });
    }
}