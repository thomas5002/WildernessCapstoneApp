package com.example.yusefcapstione;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class CameraHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "photos_database.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "photos";
    public static final String COLUMN_FILE_PATH = "file_path";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_FILE_PATH + " TEXT)";

    public CameraHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle upgrades if your schema changes in the future
    }

    public void insertImageData(String filePath) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FILE_PATH, filePath);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<String> getAllPhotoPaths() {
        List<String> photoPaths = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COLUMN_FILE_PATH};
        Cursor cursor = db.query(true, TABLE_NAME, projection, null, null, COLUMN_FILE_PATH, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String photoPath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FILE_PATH));
                photoPaths.add(photoPath);
            }
            cursor.close();
        } else {
            Log.e("CameraHelper", "Cursor is null");
        }
        db.close();
        return photoPaths;
    }
}
