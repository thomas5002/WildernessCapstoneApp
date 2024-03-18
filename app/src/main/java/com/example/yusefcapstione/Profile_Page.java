package com.example.yusefcapstione;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;




import androidx.appcompat.app.AppCompatActivity;

public class Profile_Page extends AppCompatActivity {

    // Declare UI elements
    private Button LOADUSERBTN, INC1BTN, INC2BTN, INC3BTN, INC4BTN, DEL1BTN, DEL2BTN, DEL3BTN, DEL4BTN;
    public int visC;// Variable to keep track of visibility change

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        int i=0;
        i++;
        // Initialize visibility counter
        if (i == 0){
            visC = 0;
        }
        // Initialize UI elements
        LOADUSERBTN = findViewById(R.id.loadUserBTN);
        INC1BTN = findViewById(R.id.inc1BTN);
        INC2BTN = findViewById(R.id.inc2BTN);
        INC3BTN = findViewById(R.id.inc3BTN);
        INC4BTN = findViewById(R.id.inc4BTN);
        DEL1BTN = findViewById(R.id.del1BTN);
        DEL2BTN = findViewById(R.id.del2BTN);
        DEL3BTN = findViewById(R.id.del3BTN);
        DEL4BTN = findViewById(R.id.del4BTN);


        // Set incidents buttons initially invisible
        INC1BTN.setVisibility(View.INVISIBLE);
        INC2BTN.setVisibility(View.INVISIBLE);
        INC3BTN.setVisibility(View.INVISIBLE);
        INC4BTN.setVisibility(View.INVISIBLE);
        DEL1BTN.setVisibility(View.INVISIBLE);
        DEL2BTN.setVisibility(View.INVISIBLE);
        DEL3BTN.setVisibility(View.INVISIBLE);
        DEL4BTN.setVisibility(View.INVISIBLE);

        // Load user button click listener
        LOADUSERBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment visibility counter
                visC++;
                // Start MainActivity
                Intent intent = new Intent(Profile_Page.this, MainActivity.class);
                startActivity(intent);
                // Show the next available incident button
                if (!INC1BTN.isShown()) {
                    INC1BTN.setVisibility(View.VISIBLE);
                    DEL1BTN.setVisibility(View.VISIBLE);
                } else if (!INC2BTN.isShown()) {
                    INC2BTN.setVisibility(View.VISIBLE);
                    DEL2BTN.setVisibility(View.VISIBLE);
                } else if (!INC3BTN.isShown()) {
                    INC3BTN.setVisibility(View.VISIBLE);
                    DEL3BTN.setVisibility(View.VISIBLE);
                } else if (!INC4BTN.isShown()) {
                    INC4BTN.setVisibility(View.VISIBLE);
                    DEL4BTN.setVisibility(View.VISIBLE);
                }

            }
        });

        // Incident 1 button click listener
        INC1BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Page.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Delete 1 button click listener
        DEL1BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visC--;
                INC1BTN.setVisibility(View.INVISIBLE);
                DEL1BTN.setVisibility(View.INVISIBLE);
            }
        });

        // Incident 2 button click listener
        INC2BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Page.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Delete 2 button click listener
        DEL2BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visC--;
                INC2BTN.setVisibility(View.INVISIBLE);
                DEL2BTN.setVisibility(View.INVISIBLE);
            }
        });

        // Incident 3 button click listener
        INC3BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Page.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Delete 3 button click listener
        DEL3BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visC--;
                INC3BTN.setVisibility(View.INVISIBLE);
                DEL3BTN.setVisibility(View.INVISIBLE);
            }
        });

        // Incident 4 button click listener
        INC4BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Page.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Delete 4 button click listener
        DEL4BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visC--;
                INC4BTN.setVisibility(View.INVISIBLE);
                DEL4BTN.setVisibility(View.INVISIBLE);
            }
        });



    }

}