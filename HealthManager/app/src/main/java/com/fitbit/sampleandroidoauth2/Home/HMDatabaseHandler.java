package com.fitbit.sampleandroidoauth2.Home;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by pratik on 7/30/2017.
 */

public class HMDatabaseHandler extends SQLiteOpenHelper {

    //Food List Table Details
    protected static final String FOOD_TABLE_NAME = "FoodList";
    protected static final String FOOD_COLUMN_ID = "_id";
    protected static final String FOOD_COLUMN_USERID = "userID";
    protected static final String FOOD_COLUMN_NAME = "foodName";
    protected static final String FOOD_COLUMN_BRAND = "foodBrandName";
    protected static final String FOOD_COLUMN_CALORIES = "foodCalories";
    protected static final String FOOD_COLUMN_SERVING_SIZE = "foodServingSize";
    protected static final String FOOD_COLUMN_SERVING_SIZE_UNIT = "foodServingSizeUnit";
    protected static final String FOOD_COLUMN_DATE = "date";
    protected static final String[] FOOD_ALL_COLUMNS =
            {FOOD_COLUMN_ID, FOOD_COLUMN_USERID, FOOD_COLUMN_NAME, FOOD_COLUMN_BRAND, FOOD_COLUMN_CALORIES,
                    FOOD_COLUMN_SERVING_SIZE, FOOD_COLUMN_SERVING_SIZE_UNIT, FOOD_COLUMN_DATE};
    protected static final String FOOD_TABLE_CREATE = "CREATE TABLE " + FOOD_TABLE_NAME + " ( " +
            FOOD_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FOOD_COLUMN_USERID + " INTEGER, " +
            FOOD_COLUMN_NAME + " TEXT, " +
            FOOD_COLUMN_BRAND + " TEXT, " +
            FOOD_COLUMN_CALORIES + " TEXT, " +
            FOOD_COLUMN_SERVING_SIZE + " TEXT, " +
            FOOD_COLUMN_SERVING_SIZE_UNIT + " TEXT, " +
            FOOD_COLUMN_DATE + " TEXT " +
            ")";

    //User Table Details
    protected static final String USER_TABLE_NAME = "UserList";
    protected static final String USER_COLUMN_ID = "userID";
    protected static final String USER_COLUMN_PASSWORD = "userPassword";
    protected static final String USER_COLUMN_EMAIL = "userEmail";
    protected static final String USER_COLUMN_WEIGHT = "userWeight";
    protected static final String USER_COLUMN_HEIGHT = "userHeight";
    protected static final String USER_COLUMN_TARGET = "target";
    protected static final String USER_COLUMN_AGE = "userAge";
    protected static final String USER_COLUMN_GENDER = "userGender";
    protected static final String USER_COLUMN_DESIREDWEIGHT = "userDesiredWeight";
    protected static final String USER_COLUMN_NOOFWEEKS = "noOfWeeks";
    protected static final String USER_COLUMN_GOAL = "Goal";
    protected static final String[] USER_ALL_COLUMNS =
            {USER_COLUMN_ID, USER_COLUMN_EMAIL, USER_COLUMN_PASSWORD, USER_COLUMN_WEIGHT, USER_COLUMN_HEIGHT,
                    USER_COLUMN_TARGET, USER_COLUMN_AGE, USER_COLUMN_GENDER, USER_COLUMN_DESIREDWEIGHT, USER_COLUMN_NOOFWEEKS, USER_COLUMN_GOAL};
    protected static final String USER_TABLE_CREATE = "CREATE TABLE " + USER_TABLE_NAME + " ( " +
            USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USER_COLUMN_EMAIL + " TEXT, " +
            USER_COLUMN_PASSWORD + " TEXT, " +
            USER_COLUMN_WEIGHT + " INTEGER, " +
            USER_COLUMN_HEIGHT + " INTEGER, " +
            USER_COLUMN_TARGET + " TEXT, " +
            USER_COLUMN_AGE + " INTEGER, " +
            USER_COLUMN_GENDER + " TEXT, " +
            USER_COLUMN_DESIREDWEIGHT + " INTEGER, " +
            USER_COLUMN_NOOFWEEKS + " INTEGER, " +
            USER_COLUMN_GOAL + " DOUBLE " +
            ")";
    //Constants for db name and version
    private static final String DATABASE_NAME = "HealthManager.db";
    private static final int DATABASE_VERSION = 10;
    SQLiteDatabase db;

    public HMDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(FOOD_TABLE_CREATE);
        sqLiteDatabase.execSQL(USER_TABLE_CREATE);
        this.db = sqLiteDatabase;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FOOD_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);

        this.onCreate(sqLiteDatabase);
        this.db = sqLiteDatabase;
    }

    public void insertUser(String userEmail, String userPassword, String userGender, int userWeight,
                           int userHeight, String userTarget, int userAge, int desiredWeight, int noOfWeeks, double goal) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_COLUMN_EMAIL, userEmail);
        values.put(USER_COLUMN_PASSWORD, userPassword);
        values.put(USER_COLUMN_WEIGHT, userWeight);
        values.put(USER_COLUMN_HEIGHT, userHeight);
        values.put(USER_COLUMN_GENDER, userGender);
        values.put(USER_COLUMN_TARGET, userTarget);
        values.put(USER_COLUMN_AGE, userAge);
        values.put(USER_COLUMN_DESIREDWEIGHT, desiredWeight);
        values.put(USER_COLUMN_NOOFWEEKS, noOfWeeks);
        values.put(USER_COLUMN_GOAL, goal);
        long insert = db.insert(USER_TABLE_NAME, null, values);
        Log.e("TAG", "insert: " + insert);
    }

    public void UpdateUser(int Id, String userEmail, String userPassword, String userGender, int userWeight, int userHeight,
                           String userTarget, int userAge, int desiredWeight, int noOfWeeks, double goal) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_COLUMN_EMAIL, userEmail);
        values.put(USER_COLUMN_PASSWORD, userPassword);
        values.put(USER_COLUMN_WEIGHT, userWeight);
        values.put(USER_COLUMN_HEIGHT, userHeight);
        values.put(USER_COLUMN_GENDER, userGender);
        values.put(USER_COLUMN_TARGET, userTarget);
        values.put(USER_COLUMN_AGE, userAge);
        values.put(USER_COLUMN_DESIREDWEIGHT, desiredWeight);
        values.put(USER_COLUMN_NOOFWEEKS, noOfWeeks);
        values.put(USER_COLUMN_GOAL, goal);
        db.update(USER_TABLE_NAME, values, HMDatabaseHandler.USER_COLUMN_ID + "=" + Id, null);
    }

    public String searchPass(String st_Uname) {
        db = this.getReadableDatabase();
        String query = "SELECT " + USER_COLUMN_EMAIL + "," + USER_COLUMN_PASSWORD + " FROM " + USER_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String a, b;
        b = "not found";
        if (cursor.moveToFirst()) {
            do {
                a = cursor.getString(0);
                if (a.equals(st_Uname)) {
                    b = cursor.getString(1);
                }
            } while (cursor.moveToNext());
        }
        return b;
    }


    public HMUserClass getUserFromID(int id) {

        Cursor cursor = getReadableDatabase().query(USER_TABLE_NAME, USER_ALL_COLUMNS, USER_COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        HMUserClass User = new HMUserClass(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)),
                cursor.getString(5),
                Integer.parseInt(cursor.getString(6)),
                cursor.getString(7),
                Integer.parseInt(cursor.getString(8)),
                Integer.parseInt(cursor.getString(9)),
                Double.parseDouble(cursor.getString(10)));
        return User;
    }

    public HMUserClass getUserFromEmail(String email) {

        HMUserClass User = null;

        Cursor cursor = getReadableDatabase().query(USER_TABLE_NAME, USER_ALL_COLUMNS, USER_COLUMN_EMAIL + "=?",
                new String[]{String.valueOf(email)}, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            User = new HMUserClass(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    Integer.parseInt(cursor.getString(3)),
                    Integer.parseInt(cursor.getString(4)),
                    cursor.getString(5),
                    Integer.parseInt(cursor.getString(6)),
                    cursor.getString(7),
                    Integer.parseInt(cursor.getString(8)),
                    Integer.parseInt(cursor.getString(9)),
                    Double.parseDouble(cursor.getString(10)));
        }
        return User;
    }
}
