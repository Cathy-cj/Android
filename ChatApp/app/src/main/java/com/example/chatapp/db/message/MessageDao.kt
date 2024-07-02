package com.example.chatapp.db.message

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MessageDao {
    // 插入消息
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(message: Message)

    // 查询我和好友的消息
    @Query(
        """
    SELECT * FROM Message 
    WHERE ((fromAccount = :myPhone AND toAccount = :friendPhone) 
        OR (fromAccount = :friendPhone AND toAccount = :myPhone)) 
        AND deleteUser != :myPhone 
        AND revokeFlag = 0
    ORDER BY timestamp ASC
"""
    )
    fun queryMessages(myPhone: String?, friendPhone: String?): List<Message>

    // 删除消息
    @Query("UPDATE Message SET deleteUser =:deleteUser WHERE id = :id")
    fun delete(id: String, deleteUser: String)

    // 撤回消息
    @Query("UPDATE Message SET revokeFlag = 1 WHERE id = :id")
    fun revoke(id: String)
}
