package com.example.yusefcapstione;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


public class MyHelper2 extends SQLiteOpenHelper {

    private Context con2;

    public MyHelper2 (Context context){
        super(context, "MYDatabase2", null, 1);
        con2=context;
    }


    @Override
    public void onCreate(SQLiteDatabase db2) {
        String createTable2 = "create table MyTable2 (xValues2 INTEGER, yValues2 INTEGER);";
        db2.execSQL(createTable2);
        Toast.makeText(con2, "Table Created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db2, int oldVersion, int newVersion) {

    }

    public void insertData(int x2, int y2)  {

        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("xValues2",x2);
        contentValues2.put("yValues2",y2);
        db2.insert("MyTable2", null, contentValues2);
        Toast.makeText(con2, "Data Logged", Toast.LENGTH_LONG).show();

    }
}
