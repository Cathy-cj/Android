package com.example.chatapp.db.message

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Message::class], version = 2)
abstract class MessageDatabase : RoomDatabase() {
    abstract val messageDao: MessageDao
}