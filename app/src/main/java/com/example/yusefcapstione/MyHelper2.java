package com.example.yusefcapstione;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class MyHelper2 extends SQLiteOpenHelper {

    private Context con2;

    // Constructor
    public MyHelper2(Context context) {
        super(context, "MYDatabase2", null, 1);
        con2 = context;
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db2) {
        // SQL query to create the table
        String createTable2 = "CREATE TABLE MyTable2 (xValues2 INTEGER, yValues2 INTEGER);";
        // Execute the SQL query
        db2.execSQL(createTable2);
        // Show a toast message indicating table creation
        Toast.makeText(con2, "Table Created", Toast.LENGTH_LONG).show();
    }

    // Called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db2, int oldVersion, int newVersion) {
        // No implementation for now
    }

    // Method to insert data into the database
    public void insertData(double x2, int y2) {
        // Get a writable database
        SQLiteDatabase db2 = this.getWritableDatabase();

        Cursor cursor = db2.rawQuery("SELECT MAX(xValues2) FROM MyTable2", null);
        int nextXValue = 0;
        if (cursor.moveToFirst()) {
            nextXValue = cursor.getInt(0) + 1;
        }
        cursor.close();

        nextXValue = nextXValue % 100;

        // Create a ContentValues object to store the data
        ContentValues contentValues2 = new ContentValues();
        // Put data into ContentValues object
        contentValues2.put("xValues2", nextXValue);
        contentValues2.put("yValues2", y2);
        // Insert data into the table
        db2.insert("MyTable2", null, contentValues2);
        // Show a toast message indicating data insertion
        Toast.makeText(con2, "Data Logged", Toast.LENGTH_LONG).show();
    }
}