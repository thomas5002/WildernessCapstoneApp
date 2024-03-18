package com.example.yusefcapstione;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyHelper extends SQLiteOpenHelper {

    private Context con;

    // Constructor
    public MyHelper(Context context) {
        super(context, "MYDatabase", null, 1);
        con = context;
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL query to create the table
        String createTable = "CREATE TABLE MyTable (xValues INTEGER, yValues INTEGER);";
        // Execute the SQL query
        db.execSQL(createTable);
        // Show a toast message indicating table creation
        Toast.makeText(con, "Table Created", Toast.LENGTH_LONG).show();
    }

    // Called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No implementation for now
    }

    // Method to insert data into the database
    public void insertData(int x, int y) {
        // Get a writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a ContentValues object to store the data
        ContentValues contentValues = new ContentValues();
        // Put data into ContentValues object
        contentValues.put("xValues", x);
        contentValues.put("yValues", y);
        // Insert data into the table
        db.insert("MyTable", null, contentValues);
        // Show a toast message indicating data insertion
        Toast.makeText(con, "Data Logged", Toast.LENGTH_LONG).show();
    }
}