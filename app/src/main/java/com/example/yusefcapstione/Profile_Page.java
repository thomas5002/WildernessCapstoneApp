package com.example.yusefcapstione;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;  //stuff below is added so we can find the edit text so name can be manipulated
import android.text.Editable;    //added for name change
import android.text.TextWatcher; //added for name change


import androidx.appcompat.app.AppCompatActivity;

public class Profile_Page extends AppCompatActivity {

    private Button LOADUSERBTN, INC1BTN;
    private EditText editText; // Added line for name change
    public int visC = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        LOADUSERBTN = findViewById(R.id.loadUserBTN);
        INC1BTN = findViewById(R.id.inc1BTN);
        editText = findViewById(R.id.test); // Added line for name change

        INC1BTN.setVisibility(View.INVISIBLE);           // starts the incidents button as hidden

        LOADUSERBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visC++;
                Intent intent = new Intent(Profile_Page.this, MainActivity.class);
                startActivity(intent);

                if (visC == 1) {
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

        // Added block for name change - Start
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before the text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called when the text is being changed
                String enteredText = s.toString();
                // Modify the button's text based on the entered text
                INC1BTN.setText(enteredText);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called after the text has changed
            }
        });
        // Added block - End

    }

}