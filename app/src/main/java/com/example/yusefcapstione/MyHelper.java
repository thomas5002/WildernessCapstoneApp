package com.example.yusefcapstione;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyHelper extends SQLiteOpenHelper {

    private Context con;

    public MyHelper (Context context){
        super(context, "MYDatabase", null, 1);
        con=context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "create table MyTable (xValues INTEGER, yValues INTEGER);";
        db.execSQL(createTable);
        Toast.makeText(con, "Table Created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertData(int x, int y)  {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("xValues",x);
        contentValues.put("yValues",y);
        db.insert("MyTable", null, contentValues);
        Toast.makeText(con, "Data Logged", Toast.LENGTH_LONG).show();

}
}
