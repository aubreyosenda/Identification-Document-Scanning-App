package com.android.example.cameraxapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user_data.db";
    private static final int DATABASE_VERSION = 3; // Increment version

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE UserSignup (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + // Auto-incremental ID
                "name TEXT, " +
                "id_number TEXT, " +
                "document TEXT, " +
                "country TEXT, " +
                "phone TEXT, " +
                "purpose_of_visit TEXT, " + // Added purpose_of_visit
                "signup_time TEXT, " + // Changed to TEXT to store date-time string
                "signout_time TEXT)"; // Changed to TEXT to store date-time string
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS UserSignup");
        onCreate(db);
    }
}
