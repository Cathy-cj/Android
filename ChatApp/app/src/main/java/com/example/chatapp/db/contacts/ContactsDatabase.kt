package com.example.chatapp.db.contacts

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Contacts::class], version = 3)
abstract class ContactsDatabase : RoomDatabase() {
    abstract val contactsDao: ContactsDao
}