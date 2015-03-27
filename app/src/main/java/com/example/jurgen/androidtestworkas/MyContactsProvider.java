package com.example.jurgen.androidtestworkas;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class MyContactsProvider extends ContentProvider{
    final String LOG_TAG = "TAG";
    static final String DB_NAME = "mydb";//ДБ название
    static final int DB_VERSION = 1;//Версия
    static final String TABLE_TEAM = "teams";// Таблица
    static final String KEY_ID = "_id";// Поля
    static final String KEY_LOGO = "_logo";// Поля
    static final String KEY_NAME = "_name";// Поля
    // Скрипт создания таблицы
    static final String DB_CREATE = "create table " + TABLE_TEAM + "("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_LOGO + " text, "
            + KEY_NAME + " text" + ");";
    // Uri
    static final String AUTHORITY = "com.example.jurgen.androidworkas";
    // path
    static final String CONTACT_PATH = "teams";
    // Общий Uri
    public static final Uri TEAM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CONTACT_PATH);

    // Типы данных
    // набор строк
    static final String TEAM_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."+ AUTHORITY + "." + CONTACT_PATH;
    // одна строка
    static final String CONTACT_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."+ AUTHORITY + "." + CONTACT_PATH;
    //// UriMatcher
    // общий Uri
    static final int URI_TEAM = 1;
    // Uri с указанным ID
//    static final int URI_CONTACTS_ID = 2;
    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH, URI_TEAM);
//        uriMatcher.addURI(AUTHORITY, CONTACT_PATH + "/#", URI_CONTACTS_ID);
    }
    DBHelper dbHelper;
    SQLiteDatabase db;
    @Override
    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query,ВьІборка " + uri.toString());
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_TEAM,null , null, null, null, null, null);
        // просим ContentResolver уведомлять этот курсор
        // об изменениях данных в CONTACT_CONTENT_URI
        cursor.setNotificationUri(getContext().getContentResolver(),TEAM_CONTENT_URI);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_TEAM:
                Log.d(LOG_TAG, "insert, " + TEAM_CONTENT_TYPE);
                return TEAM_CONTENT_TYPE;
//            case URI_CONTACTS_ID:
//                return CONTACT_CONTENT_ITEM_TYPE;
        }
        return null;

    }

    @Override
    public Uri insert (Uri uri, ContentValues values) {

        Log.d(LOG_TAG, "insert, " + uri.toString());
         if (uriMatcher.match(uri) != URI_TEAM)
            throw new IllegalArgumentException("Wrong URI: " + uri+"=/="+URI_TEAM);

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(TABLE_TEAM, null, values);
        Uri resultUri = ContentUris.withAppendedId(TEAM_CONTENT_URI, rowID);

        // уведомляем ContentResolver, что данные по адресу resultUri изменились
        getContext().getContentResolver().notifyChange(resultUri, null);
            return resultUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
    class DBHelper extends SQLiteOpenHelper{
        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
