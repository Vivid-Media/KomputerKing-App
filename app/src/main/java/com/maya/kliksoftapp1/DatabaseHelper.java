package com.maya.kliksoftapp1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Information
    private static final String DATABASE_NAME = "UserDatabase";
    private static final int DATABASE_VERSION = 6;   // 1 more to update

    // Admin Credentials
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "123";
    private boolean isAdmin = false;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the users table
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)");
        db.execSQL("CREATE VIRTUAL TABLE products USING fts4(name, description, price)");

        insertDefaultUsers(db);
        insertDefaultProducts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS products");
        onCreate(db);
    }

    // Insert Default Admin User
    private void insertDefaultUsers(SQLiteDatabase db) {
        addUser(db, ADMIN_USERNAME, ADMIN_PASSWORD);
        addUser(db, "s", "s");
    }

    private void addUser(SQLiteDatabase db, String username, String password) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        db.insert("users", null, values);
    }

    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        long result = db.insert("users", null, values);
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE username=? AND password=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean exists = cursor.getCount() > 0;
        isAdmin = exists && ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);

        cursor.close();
        db.close();
        return exists;
    }

    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE username=?", new String[]{username});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        return userId;
    }

    public String getUserName(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT username FROM users WHERE id=?", new String[]{String.valueOf(userId)});
        String username = null;
        if (cursor.moveToFirst()) {
            username = cursor.getString(0);
        }
        cursor.close();
        return username;
    }

    public boolean addProduct(SQLiteDatabase db, String productName, String productDesc, int productPrice) {
        if (productPrice <= 0) {
            Log.e("Database", "Invalid price: " + productPrice);
            return false;
        }
        ContentValues values = new ContentValues();
        values.put("name", productName);
        values.put("description", productDesc);
        values.put("price", productPrice);
        long result = db.insert("products", null, values);
        return result != -1;
    }

    public void insertDefaultProducts(SQLiteDatabase db) {
        addProduct(db, "Komputer 4k rtx 4024", "Dobry komputer do gier uwu", 500);
        addProduct(db, "laptop 2k rtx 404", "Dobry laptop uwu", 300);
        addProduct(db, "telefon 44k intelcore 2", "Dobry telefon uwu", 500);
        addProduct(db, "tablet 3k gtx 1090px", "Dobry tablet", 500);
        addProduct(db, "telewizor 12k LG", "Tv 12k firmy lg", 5000);
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
