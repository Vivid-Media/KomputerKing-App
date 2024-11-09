package com.maya.kliksoftapp1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "UserDatabase";

    private static final int DATABASE_VERSION = 2;   // 1 more to update


    // Table and Column Names
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Admin credentials and status
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "123";
    private boolean isAdmin = false;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Insert default users
        insertDefaultUsers(db);
    }

    // Method to insert base users like 'admin' and 'user'
    private void insertDefaultUsers(SQLiteDatabase db) {
        ContentValues adminValues = new ContentValues();
        adminValues.put(COLUMN_USERNAME, ADMIN_USERNAME);
        adminValues.put(COLUMN_PASSWORD, ADMIN_PASSWORD);
        db.insert(TABLE_USERS, null, adminValues);

        ContentValues userValues = new ContentValues();
        userValues.put(COLUMN_USERNAME, "s");
        userValues.put(COLUMN_PASSWORD, "s");
        db.insert(TABLE_USERS, null, userValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Method to add a new user
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // Method to check user credentials and set admin status if applicable
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean exists = cursor.getCount() > 0;

        // Set isAdmin to true if credentials match the hardcoded admin values
        if (exists && ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            isAdmin = true;
        } else {
            isAdmin = false;
        }

        cursor.close();
        db.close();
        return exists;
    }

    // Getter for isAdmin
    public boolean isAdmin() {
        return isAdmin;
    }
}
