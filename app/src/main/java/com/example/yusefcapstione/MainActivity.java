package com.example.yusefcapstione;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button HRBTN, BRBTN, GRAPHBTN, SETTINGSBTN, CAMERABTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HRBTN = findViewById(R.id.hrBTN);
        BRBTN = findViewById(R.id.brBTN);
        GRAPHBTN = findViewById(R.id.GraphBTN);
        SETTINGSBTN = findViewById(R.id.SettingBTN);
        CAMERABTN = findViewById(R.id.CameraBTN);

        HRBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HeartRate_Activity.class);
                startActivity(intent);
            }
        });

        BRBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BreathRate_Activity.class);
                startActivity(intent);
            }
        });

        GRAPHBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecordSymptoms.class);
                startActivity(intent);
            }
        });

        SETTINGSBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings_Activity.class);
                startActivity(intent);
            }
        });

        CAMERABTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Camera_Activity.class);
                startActivity(intent);
            }
        });
    }
}
