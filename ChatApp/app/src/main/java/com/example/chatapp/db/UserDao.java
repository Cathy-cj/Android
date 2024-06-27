package com.example.chatapp.db;

// 创建 UserDao.java 文件

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.chatapp.db.User;

public class UserDao {

    private DatabaseHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long addUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NICKNAME, user.getNickname());
        values.put(DatabaseHelper.COLUMN_PHONE, user.getPhone());
        values.put(DatabaseHelper.COLUMN_PASSWORD, user.getPassword());

        return db.insert(DatabaseHelper.TABLE_USERS, null, values);
    }

    public User getUser(String phone) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {
                DatabaseHelper.COLUMN_NICKNAME,
                DatabaseHelper.COLUMN_PHONE,
                DatabaseHelper.COLUMN_PASSWORD
        };

        String selection = DatabaseHelper.COLUMN_PHONE + " = ?";
        String[] selectionArgs = { phone };

        Cursor cursor = db.query(
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
            return new User(nickname, phone, password);
        }

        return null;
    }
}

