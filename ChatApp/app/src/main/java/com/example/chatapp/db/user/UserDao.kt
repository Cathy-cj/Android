package com.example.chatapp.db.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    /**
     * 注册用户
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun register(user: User)

    /**
     * 用户登录
     */
    @Query("SELECT * FROM User WHERE phone = :phone AND password = :password")
    fun login(phone: String, password: String): List<User>

    /**
     * 查询用户
     */
    @Query("SELECT * FROM User WHERE phone = :phone")
    fun queryUser(phone: String): List<User>
}
