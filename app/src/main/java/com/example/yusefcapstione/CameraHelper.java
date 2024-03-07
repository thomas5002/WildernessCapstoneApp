package com.example.yusefcapstione;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CameraHelper extends SQLiteOpenHelper {
    public static final String COLUMN_FILE_PATH = "file_path";
    public static final String TABLE_NAME = "photos";
    // Define your database name and version
    private static final String DATABASE_NAME = "your_database_name.db";
    private static final int DATABASE_VERSION = 1;

    // Define your table name
    //private static final String TABLE_NAME = "photos";

    // Define the column names
    private static final String COLUMN_ID = "id";
    //private static final String COLUMN_FILE_PATH = "file_path";

    // Define the SQL statement to create the table
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
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
}
