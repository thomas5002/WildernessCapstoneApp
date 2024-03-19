package com.example.yusefcapstione;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class CameraRoll_Activity extends AppCompatActivity {

    private Button backButton;
    private RecyclerView recyclerView;
    private PhotoPagerAdapter adapter;
    private List<String> photoPaths = new ArrayList<>();
    private CameraHelper cameraHelper; // Instance of CameraHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameraroll_settings);

        backButton = findViewById(R.id.backBTN);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Use LinearLayoutManager for single-column layout

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Navigate back to the previous activity
            }
        });

        cameraHelper = new CameraHelper(this); // Initialize CameraHelper

        // Create and set up the adapter
        adapter = new PhotoPagerAdapter(this, photoPaths);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh photo paths from the database
        retrievePhotoPathsFromDatabase();
    }

    private void retrievePhotoPathsFromDatabase() {
        photoPaths.clear(); // Clear existing list before refreshing
        List<String> allPhotoPaths = cameraHelper.getAllPhotoPaths(); // Get all photo paths from the database

        // Reverse the list to display the most recent photos first
        Collections.reverse(allPhotoPaths);

        photoPaths.addAll(allPhotoPaths); // Add photo paths to the list
        Log.d("PhotoPaths", "Retrieved from database: " + photoPaths.toString()); // Log retrieved photo paths
        adapter.notifyDataSetChanged(); // Notify adapter that the dataset has changed
    }

}
