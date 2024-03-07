package com.example.yusefcapstione;
import android.annotation.SuppressLint;
import android.widget.ImageView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;


public class CameraRoll_Activity extends AppCompatActivity {

    private Button backButton;
    private ImageView imageView;
    private List<String> photoPaths = new ArrayList<>();
    private CameraHelper cameraHelper; // Instance of CameraHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameraroll_settings);

        backButton = findViewById(R.id.backBTN);
        imageView = findViewById(R.id.imageView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CameraRoll_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        cameraHelper = new CameraHelper(this); // Initialize CameraHelper

        // Retrieve photo paths from the database
        retrievePhotoPathsFromDatabase();

        // Display the first photo
        displayNextPhoto();
    }

    private void retrievePhotoPathsFromDatabase() {
        SQLiteDatabase db = cameraHelper.getReadableDatabase();
        String[] projection = {CameraHelper.COLUMN_FILE_PATH};
        Cursor cursor = db.query(CameraHelper.TABLE_NAME, projection, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String photoPath = cursor.getString(cursor.getColumnIndex(CameraHelper.COLUMN_FILE_PATH));
                photoPaths.add(photoPath);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
    }

    private void displayNextPhoto() {
        if (!photoPaths.isEmpty()) {
            String photoPath = photoPaths.remove(0); // Remove the first photo path from the list
            File photoFile = new File(photoPath);
            Glide.with(this).load(photoFile).into(imageView); // Load the photo into the imageView
        } else {
            // Handle case where there are no more photos to display
            // For example, you can show a message or hide the imageView
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
