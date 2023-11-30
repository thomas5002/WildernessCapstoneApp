package com.example.yusefcapstione;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button HRBTN , BRBTN, GRAPHBTN, SETTINGSBTN, btn, btnRead;
    private EditText test;
    private TextView textView3;
    private DatabaseReference rootDatabaseref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test = findViewById(R.id.test);
        btn = findViewById(R.id.btn);
        btnRead = findViewById(R.id.btnRead);
        textView3 = findViewById(R.id.textView3);

        rootDatabaseref = FirebaseDatabase.getInstance().getReference().child("Users");

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootDatabaseref.child("user1").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String data = dataSnapshot.getValue().toString();
                            textView3.setText(data);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data=test.getText().toString();
                rootDatabaseref.child("user1").setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Your Data is successfully added", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Sorry! Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        HRBTN = findViewById(R.id.hrBTN);
        BRBTN = findViewById(R.id.brBTN);
        GRAPHBTN = findViewById(R.id.GraphBTN);
        SETTINGSBTN = findViewById(R.id.SettingBTN);

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
                Intent intent = new Intent(MainActivity.this, Graph_Activity.class);
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

            // Set toolbar name to user name

      //  TextView tvId = (TextView) findViewById(R.id.toolbar2);
       // tvId.setText("my name jeff");






    }
}