package com.fitbit.sampleandroidoauth2.Home;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by pratik on 7/30/2017.
 */

public class HMDataProvider extends ContentProvider {

    public static final String CONTENT_ITEM_TYPE = "foodlist";
    private static final String AUTHORITY = "com.fitbit.sampleandroidoauth2.Home.HMDataProvider";
    private static final String BASE_PATH = "foodlists";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    // private static final int FOODS_ID = 2;
    // Constant to identify the requested operation
    private static final int FOODS = 1;
    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, FOODS);
        //  uriMatcher.addURI(AUTHORITY, BASE_PATH +  "/#", FOODS_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        HMDatabaseHandler helper = new HMDatabaseHandler(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {

        return database.query(HMDatabaseHandler.FOOD_TABLE_NAME, HMDatabaseHandler.FOOD_ALL_COLUMNS,
                s, null, null, null,
                HMDatabaseHandler.FOOD_COLUMN_CALORIES + " DESC");

    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id = database.insert(HMDatabaseHandler.FOOD_TABLE_NAME,
                null, contentValues);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return database.delete(HMDatabaseHandler.FOOD_TABLE_NAME, s, strings);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return database.update(HMDatabaseHandler.FOOD_TABLE_NAME,
                contentValues, s, strings);
    }
}
