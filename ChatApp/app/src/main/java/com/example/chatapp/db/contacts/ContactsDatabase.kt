package com.example.chatapp.db.contacts

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Contacts::class], version = 1)
abstract class ContactsDatabase : RoomDatabase() {
    abstract val contactsDao: ContactsDao
}