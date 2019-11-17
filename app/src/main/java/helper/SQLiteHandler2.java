package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler2 extends SQLiteOpenHelper{
    private static final String TAG = SQLiteHandler2.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_REVIEW = "reviews";


    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_CAR_NAME = "car_name";
   // private static final String KEY_EMAIL = "email";
   // private static final String KEY_UID = "u_id";
    private static final String KEY_USER_REVIEW = "user_review";
    private static final String KEY_USER_id = "user_id";
    private static final String KEY_car_id = "car_id";

    public SQLiteHandler2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables

    public void onCreate(SQLiteDatabase db) {
        String CREATE_REVIEW_TABLE = "CREATE TABLE " + TABLE_REVIEW + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CAR_NAME + " TEXT,"
                + KEY_USER_REVIEW + " TEXT,"
                + KEY_car_id + " INTEGER" + KEY_USER_id + "INTEGER" +")";
        db.execSQL(CREATE_REVIEW_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEW);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String car_name, String user_review) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CAR_NAME, car_name); // Name
        values.put(KEY_USER_REVIEW, user_review); // Email
      //  values.put(KEY_UID, u_id); // Email
//        values.put(KEY_car_id, car_id);
//        values.put(KEY_USER_id, user_id);// Created At

        // Inserting Row
        long id = db.insert(TABLE_REVIEW, null, values);
        db.close(); // Closing database connection


        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> review = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_REVIEW;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            review.put("car_name", cursor.getString(1));
            review.put("user_review", cursor.getString(2));
           // review.put("u_id", cursor.getString(3));
            review.put("car_id", cursor.getString(4));
            review.put("user_id", cursor.getString(5));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + review.toString());

        return review;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_REVIEW, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }


}
