package com.example.yusefcapstione;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Profile_Page extends AppCompatActivity {

    private Button LOADUSERBTN, INC1BTN;

    public int visC = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        LOADUSERBTN = findViewById(R.id.loadUserBTN);
        INC1BTN = findViewById(R.id.inc1BTN);

        INC1BTN.setVisibility(View.INVISIBLE);           // starts the incidents button as hidden

        LOADUSERBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visC ++;
                Intent intent = new Intent(Profile_Page.this, MainActivity.class);
                startActivity(intent);

                if(visC == 1){
                    INC1BTN.setVisibility(View.VISIBLE);
                }

            //    v.setVisibility(View.VISIBLE);
            //    v.setVisibility(View.INVISIBLE);


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
