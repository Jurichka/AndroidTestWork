package com.example.jurgen.androidtestworkas;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class MyContentProvider extends android.content.ContentProvider {
    final String LOG_TAG = "TAG";
    static final String DB_NAME = "mydb";//ДБ название
    static final int DB_VERSION = 1;//Версия
    static final String TABLE_TEAM = "teams";// Таблица
    static final String TABLE_INFO = "info";// Таблица
    static final String KEY_ID = "_id";// Поля
    static final String KEY_LOGO = "_logo";// Поля
    static final String KEY_NAME = "_name";// Поля
    // Скрипт создания таблицы
    static final String DB_CREATE = "create table " + TABLE_TEAM + "("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_LOGO + " text, "
            + KEY_NAME + " text" + ");";
    static final String DB_INFO = "create table "+ TABLE_INFO +" ("
            +"_id integer primary key autoincrement,"
            +"name text, "
            +"arg00 text, "
            +"arg01 text, "
            +"arg02 text, "
            +"arg03 text, "
            +"arg04 text, "
            +"arg05 text"
            + ");";
    // Uri
    static final String AUTHORITY = "com.example.jurgen.androidworkas";
    // path
    static final String CONTACT_PATH = "teams";
    static final String CONTACT_PATH_INFO = "info";
    // Общий Uri
    public static final Uri TEAM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CONTACT_PATH);
    public static final Uri INFO_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CONTACT_PATH_INFO);
    // Типы данных
    // набор строк
    static final String TEAM_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."+ AUTHORITY + "." + CONTACT_PATH;
    static final String INFO_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."+ AUTHORITY + "." + CONTACT_PATH_INFO;
    // одна строка
    static final String CONTACT_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."+ AUTHORITY + "." + CONTACT_PATH;
    static final String INFO_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."+ AUTHORITY + "." + CONTACT_PATH_INFO;
    //// UriMatcher
    // общий Uri
    static final int URI_TEAM = 1;
    static final int URI_INFO = 3;
    // Uri с указанным ID
    static final int URI_TEAM_ID = 2;
    static final int URI_INFO_ID = 4;
    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, CONTACT_PATH, URI_TEAM);
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH + "/#", URI_TEAM_ID);
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH_INFO, URI_INFO);
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH_INFO + "/#", URI_INFO_ID);
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
    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_TEAM:
                return TEAM_CONTENT_TYPE;
            case URI_TEAM_ID:
                return CONTACT_CONTENT_ITEM_TYPE;
            case URI_INFO:
                return INFO_CONTENT_TYPE;
            case URI_INFO_ID:
                return INFO_CONTENT_ITEM_TYPE;
        }
        return null;

    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor=null;
        switch (uriMatcher.match(uri)){
            case URI_TEAM:
                Log.d(LOG_TAG, "query,ВьІборка " + uri.toString());
                db = dbHelper.getWritableDatabase();
                cursor = db.query(TABLE_TEAM,null , null, null, null, null, null);
                cursor.setNotificationUri(getContext().getContentResolver(),TEAM_CONTENT_URI);
                break;
            case URI_INFO:
                Log.d(LOG_TAG, "query,Виборка " + uri.toString());
                db = dbHelper.getWritableDatabase();
                cursor = db.query(TABLE_INFO,null , null, null, null, null, null);
                cursor.setNotificationUri(getContext().getContentResolver(),INFO_CONTENT_URI);
                break;
            case URI_INFO_ID:
                Log.d(LOG_TAG, "query,Виборка с условием" + uri.toString()+" SelectionArgs: "+selectionArgs);
                db = dbHelper.getWritableDatabase();
                cursor = db.query(TABLE_INFO,null ,selection, selectionArgs, null, null, null);
                cursor.setNotificationUri(getContext().getContentResolver(),INFO_CONTENT_URI);
                break;
        }
//        if (uriMatcher.match(uri) != URI_TEAM) throw new IllegalArgumentException("Wrong URI: " + uri);
        return cursor;
   }



    @Override
    public Uri insert (Uri uri, ContentValues values) {
        Uri resultUri=null;
        long rowID;
        switch (uriMatcher.match(uri)){
            case URI_TEAM:
                Log.d(LOG_TAG, "insert, " + uri.toString());
                db = dbHelper.getWritableDatabase();
                rowID = db.insert(TABLE_TEAM, null, values);
                resultUri = ContentUris.withAppendedId(TEAM_CONTENT_URI, rowID);
                getContext().getContentResolver().notifyChange(resultUri, null);
            break;
            case URI_INFO:
                db = dbHelper.getWritableDatabase();
                 rowID = db.insert("info", null, values);
                resultUri = ContentUris.withAppendedId(INFO_CONTENT_URI, rowID);
                // уведомляем ContentResolver, что данные по адресу resultUri изменились
                getContext().getContentResolver().notifyChange(resultUri, null);
                break;
        }

            return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_TEAM:
                Log.d(LOG_TAG, "URI_CONTACTS");
                break;
            case URI_TEAM_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id+" "+selection);
                if (TextUtils.isEmpty(selection)) {
                    selection = KEY_ID + " = " + id;
                } else {
                    selection = selection + " AND " + KEY_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(TABLE_TEAM, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
            switch (uriMatcher.match(uri)){
            case URI_INFO:
                int cnt = db.update(TABLE_INFO, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return cnt;

        }
        return 0;
    }
    class DBHelper extends SQLiteOpenHelper{
        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
            db.execSQL(DB_INFO);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
