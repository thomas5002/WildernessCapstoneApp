package com.example.yusefcapstione;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CameraHelper extends SQLiteOpenHelper {
    // Define your database name and version
    private static final String DATABASE_NAME = "your_database_name.db";
    private static final int DATABASE_VERSION = 1;

    // Define your table name and column name
    public static final String TABLE_NAME = "photos";
    public static final String COLUMN_FILE_PATH = "file_path";

    // Define the SQL statement to create the table
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_FILE_PATH + " TEXT)";

    public CameraHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the database table
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle upgrades if your schema changes in the future
    }

    // Method to insert image data into the database
    public void insertImageData(String filePath) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FILE_PATH, filePath);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    // Method to retrieve all photo paths from the database
    public List<String> getAllPhotoPaths() {
        List<String> photoPaths = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_FILE_PATH}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String photoPath = cursor.getString(cursor.getColumnIndex(COLUMN_FILE_PATH));
                photoPaths.add(photoPath);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return photoPaths;
    }
}

