package com.example.chatapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserDao {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public UserDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
        Log.d("UserDao", "Database opened");
    }

    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
            Log.d("UserDao", "Database closed");
        }
    }

    public long addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NICKNAME, user.getNickname());
        values.put(DatabaseHelper.COLUMN_PHONE, user.getPhone());
        values.put(DatabaseHelper.COLUMN_PASSWORD, user.getPassword());

        long result = database.insert(DatabaseHelper.TABLE_USERS, null, values);
        Log.d("UserDao", "User added with result: " + result);
        return result;
    }

    public User getUser(String phone) {
        String[] columns = {
                DatabaseHelper.COLUMN_NICKNAME,
                DatabaseHelper.COLUMN_PHONE,
                DatabaseHelper.COLUMN_PASSWORD
        };

        String selection = DatabaseHelper.COLUMN_PHONE + " = ?";
        String[] selectionArgs = { phone };

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            String nickname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NICKNAME));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD));
            cursor.close();
            Log.d("UserDao", "User found: " + phone);
            return new User(nickname, phone, password);
        }

        Log.d("UserDao", "User not found: " + phone);
        return null;
    }

    public boolean checkUserExists(String phone) {
        String[] columns = { DatabaseHelper.COLUMN_PHONE };
        String selection = DatabaseHelper.COLUMN_PHONE + " = ?";
        String[] selectionArgs = { phone };

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int count = cursor.getCount();
        cursor.close();
        Log.d("UserDao", "User exists: " + (count > 0));
        return count > 0;
    }

    public int updatePassword(String phone, String newPassword) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PASSWORD, newPassword);

        String selection = DatabaseHelper.COLUMN_PHONE + " = ?";
        String[] selectionArgs = { phone };

        int result = database.update(DatabaseHelper.TABLE_USERS, values, selection, selectionArgs);
        Log.d("UserDao", "Password updated for user: " + phone);
        return result;
    }
}
