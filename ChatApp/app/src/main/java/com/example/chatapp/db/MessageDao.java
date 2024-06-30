package com.example.chatapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MessageDao {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public MessageDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    public long addMessage(Message message) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SENDER, message.getSender());
        values.put(DatabaseHelper.COLUMN_RECEIVER, message.getReceiver());
        values.put(DatabaseHelper.COLUMN_CONTENT, message.getContent());
        values.put(DatabaseHelper.COLUMN_TIMESTAMP, message.getTimestamp());

        return database.insert(DatabaseHelper.TABLE_MESSAGES, null, values);
    }

    public List<Message> getMessages(String user1, String user2) {
        List<Message> messages = new ArrayList<>();

        String selection = "(" + DatabaseHelper.COLUMN_SENDER + " = ? AND " + DatabaseHelper.COLUMN_RECEIVER + " = ?)" +
                " OR (" + DatabaseHelper.COLUMN_SENDER + " = ? AND " + DatabaseHelper.COLUMN_RECEIVER + " = ?)";
        String[] selectionArgs = {user1, user2, user2, user1};

        Cursor cursor = database.query(DatabaseHelper.TABLE_MESSAGES, null, selection, selectionArgs, null, null, DatabaseHelper.COLUMN_TIMESTAMP + " ASC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MESSAGE_ID));
                String sender = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SENDER));
                String receiver = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RECEIVER));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTENT));
                long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIMESTAMP));

                messages.add(new Message(id, sender, receiver, content, timestamp));
            }
            cursor.close();
        }

        return messages;
    }
}
