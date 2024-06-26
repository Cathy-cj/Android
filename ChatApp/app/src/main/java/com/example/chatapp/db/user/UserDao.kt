package com.example.chatapp.db.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun register(user: User)

    @Query("SELECT * FROM USER WHERE phone = :phone AND password = :password")
    fun login(phone: String?, password: String?): List<User>
}
