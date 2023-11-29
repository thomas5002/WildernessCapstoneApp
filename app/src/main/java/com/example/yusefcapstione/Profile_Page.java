package com.example.yusefcapstione;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Profile_Page extends AppCompatActivity {

    private Button LOADUSERBTN, INC1BTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        LOADUSERBTN = findViewById(R.id.loadUserBTN);
        INC1BTN = findViewById(R.id.inc1BTN);

        LOADUSERBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Page.this, MainActivity.class);
                startActivity(intent);
            }
        });

        INC1BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Page.this, MainActivity.class);
                startActivity(intent);
            }
        });





    }
}
