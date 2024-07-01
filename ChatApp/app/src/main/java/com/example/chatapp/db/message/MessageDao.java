package com.example.chatapp.db.message;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MessageDao {
    // 插入消息
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Message message);

    // 查询我和好友的消息
    @Query("SELECT * FROM Message WHERE (fromAccount = :myPhone AND toAccount = :friendPhone) OR (fromAccount = :friendPhone AND toAccount = :myPhone) ORDER BY timestamp ASC")
    List<Message> queryMessages(String myPhone, String friendPhone);

    // 删除消息
    @Query("DELEtE FROM Message WHERE id = :id")
    void delete(long id);
}
