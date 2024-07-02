package com.example.chatapp.db.contacts

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactsDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addContact(contact: Contacts)

    @Query("SELECT * FROM Contacts WHERE myPhone = :phone OR friendPhone = :phone")
    fun queryContacts(phone: String): List<Contacts>

    @Query("SELECT EXISTS(SELECT 1 FROM Contacts WHERE (myPhone = :myPhone AND friendPhone = :friendPhone) OR (myPhone = :friendPhone AND friendPhone = :myPhone))")
    fun isFriend(myPhone: String, friendPhone: String): Boolean

    @Query("DELETE FROM Contacts WHERE myPhone = :phone AND friendPhone = :friendPhone")
    fun deleteContacts(phone: String, friendPhone: String)
}
