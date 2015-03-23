package com.example.jurgen.androidtestworkas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "FootballTeamDB";

    // Contacts table name
    private static final String TABLE_CONTACTS = "Teams";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LOGO = "img";
    private static final String KEY_NAME = "name";

    public DBhelper(Context context) {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
