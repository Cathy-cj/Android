package com.example.chatapp.db.contacts

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactsDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addContact(contact: Contacts)

    @Query("SELECT * FROM Contacts WHERE myPhone = :phone")
    fun queryContacts(phone: String): List<Contacts>

    @Query("DELETE FROM Contacts WHERE myPhone = :phone AND friendPhone = :friendPhone")
    fun deleteContacts(phone: String, friendPhone: String)
}
