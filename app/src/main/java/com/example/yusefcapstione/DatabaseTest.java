package com.example.yusefcapstione;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import java.util.ArrayList;
//import androidx.annotation.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class DatabaseTest {

    //get reference of the database helper class
    static DatabaseHelper dbHelper;

    //database adapter class constructor
    public DatabaseTest(Context context) {

        dbHelper = new DatabaseHelper(context);
    }


    public Cursor getAllBreathRates() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(DatabaseHelper.BREATH_RATE_TABLE_NAME,
                new String[]{DatabaseHelper.COL_UID, DatabaseHelper.BREATH_RATE},
                null, null, null, null, DatabaseHelper.COL_UID + " ASC");
    }

    public Cursor getAllHeartRates() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(DatabaseHelper.HEART_RATE_TABLE_NAME,
                new String[]{DatabaseHelper.COL_UID, DatabaseHelper.HEART_RATE},
                null, null, null, null, DatabaseHelper.COL_UID + " ASC");
    }

    public long saveOrUpdateBreathRate(String contactId, int breathRate) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.BREATH_RATE, breathRate);

        // Trying to update first
        int rowsAffected = db.update(DatabaseHelper.BREATH_RATE_TABLE_NAME, contentValues,
                DatabaseHelper.CONTACT_ID + " = ?", new String[]{contactId});

        if (rowsAffected == 0) { // No row was updated, hence insert a new one
            contentValues.put(DatabaseHelper.CONTACT_ID, contactId);
            long id = db.insert(DatabaseHelper.BREATH_RATE_TABLE_NAME, null, contentValues);
            db.close();
            return id;
        } else {
            db.close();
            return rowsAffected; // Number of rows updated
        }
    }

    public long saveOrUpdateHeartRate(String contactId, double heartRate) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.HEART_RATE, heartRate);

        // Trying to update first
        int rowsAffected = db.update(DatabaseHelper.HEART_RATE_TABLE_NAME, contentValues,
                DatabaseHelper.CONTACT_ID + " = ?", new String[]{contactId});

        if (rowsAffected == 0) { // No row was updated, hence insert a new one
            contentValues.put(DatabaseHelper.CONTACT_ID, contactId);
            long id = db.insert(DatabaseHelper.HEART_RATE_TABLE_NAME, null, contentValues);
            db.close();
            return id;
        } else {
            db.close();
            return rowsAffected; // Number of rows updated
        }
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {


     //   public static final String DATABASE_NAME = "SYMPTOMSDB.db";
        public static final String TABLE_NAME = "SymptomsTable";
        public static long LAST_ID = 1;
        public static String FOLDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MC/";

        // new addition
        private static final int DATABASE_VERSION=1;
        private static final String DATABASE_NAME="HealthContactsDatabase";
        private static final String HEART_RATE_TABLE_NAME = "HeartRate";
        private static final String BREATH_RATE_TABLE_NAME = "BreathRate";
        private static final String CONTACT_ID = "contact_id";
        private static final String COL_UID = "_id";
        public static final String HEART_RATE = "heart_rate";

        public static final String COLUMN_ID = "_id"; // Make sure "_id" matches the column name in your table
        public static final String BREATH_RATE = "breath_rate"; // Make sure "breath_rate" matches the column name

        // Column names
       /* public static final String COL_0 = "LAST_NAME";
        public static final String COL_1 = "HEART_RATE";
        public static final String COL_2 = "RESPIRATORY_RATE";
        public static final String COL_3 = "NAUSEA";
        public static final String COL_4 = "HEADACHE";
        public static final String COL_5 = "DIARRHOEA";
        public static final String COL_6 = "SOAR_THROAT";
        public static final String COL_7 = "FEVER";
        public static final String COL_8 = "MUSCLE_ACHE";
        public static final String COL_9 = "LOSS_OF_SMELL";
        public static final String COL_10 = "COUGH";
        public static final String COL_11 = "SHORTNESS_OF_BREATH";
        public static final String COL_12 = "FEELING_TIRED";*/


      /*  public DatabaseHelper(@Nullable Context context) {
            super(context, FOLDER_PATH + DATABASE_NAME, null, 1);
        } */

        //HeartRate Table
        private static final String HEART_RATE_TABLE_CREATE = "CREATE TABLE "
                + HEART_RATE_TABLE_NAME + "(" + CONTACT_ID + " INTEGER PRIMARY KEY, "
                + HEART_RATE + " INTEGER);";

        // BreathRate table create statement
        private static final String BREATH_RATE_TABLE_CREATE = "CREATE TABLE "
                + BREATH_RATE_TABLE_NAME + "(" + COL_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CONTACT_ID + " TEXT, "
                + BREATH_RATE + " INTEGER);";

     /*   @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + TABLE_NAME + "( ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "LAST_NAME TEXT," +
                    "HEART_RATE TEXT," +
                    "RESPIRATORY_RATE TEXT," +
                    "NAUSEA TEXT," +
                    "HEADACHE TEXT," +
                    "DIARRHOEA TEXT," +
                    "SOAR_THROAT TEXT," +
                    "FEVER TEXT," +
                    "MUSCLE_ACHE TEXT," +
                    "LOSS_OF_SMELL TEXT," +
                    "COUGH TEXT," +
                    "SHORTNESS_OF_BREATH TEXT," +
                    "FEELING_TIRED TEXT)");
        }*/
        //you can change the reference name to db
     public DatabaseHelper(Context context) {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
     }
        public void onCreate(SQLiteDatabase db) {
            //pass the string value of creation statement
         //   db.execSQL(CREATE_PHONE_CONTACTS_TABLE);
            db.execSQL(HEART_RATE_TABLE_CREATE);
            db.execSQL(BREATH_RATE_TABLE_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            //we can check if table exists > drop it and create it again
            db.execSQL("DROP TABLE IF EXISTS " + BREATH_RATE_TABLE_NAME + HEART_RATE_TABLE_NAME);
            onCreate(db);
        }

   /*     public Boolean insertHeartRate(String heartRate, String respiratoryRate) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_0, "PANDIT");
            contentValues.put(COL_1, heartRate);
            contentValues.put(COL_2, respiratoryRate);
            contentValues.put(COL_3, "0");
            contentValues.put(COL_4, "0");
            contentValues.put(COL_5, "0");
            contentValues.put(COL_6, "0");
            contentValues.put(COL_7, "0");
            contentValues.put(COL_8, "0");
            contentValues.put(COL_9, "0");
            contentValues.put(COL_10, "0");
            contentValues.put(COL_11, "0");
            contentValues.put(COL_12, "0");

            long result = db.insert(TABLE_NAME, null, contentValues);
            if (result == -1) {
                return false;
            }
            LAST_ID = result;
            return true;
        }

        public Boolean updateDbWithSymptoms(HashMap<String, String> data) {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_3, data.get("Nausea"));
            contentValues.put(COL_4, data.get("Headache"));
            contentValues.put(COL_5, data.get("Diarrhoea"));
            contentValues.put(COL_6, data.get("Soar Throat"));
            contentValues.put(COL_7, data.get("Fever"));
            contentValues.put(COL_8, data.get("Muscle Ache"));
            contentValues.put(COL_9, data.get("Loss of smell or taste"));
            contentValues.put(COL_10, data.get("Cough"));
            contentValues.put(COL_11, data.get("Shortness of breath"));
            contentValues.put(COL_12, data.get("Feeling Tired"));
            long result = db.update(TABLE_NAME, contentValues, "ID" + " = ?", new String[]{String.valueOf(LAST_ID)});

            if (result == -1) {
                return false;
            }
            return true;
        }*/
    }
}