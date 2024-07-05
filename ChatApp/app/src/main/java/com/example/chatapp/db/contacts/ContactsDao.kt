package com.example.chatapp.db.contacts

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactsDao {
    /**
     * 插入联系人
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addContact(contact: Contacts)

    /**
     * 查询联系人
     */
    @Query("SELECT * FROM Contacts WHERE myPhone = :phone OR friendPhone = :phone")
    fun queryContacts(phone: String): List<Contacts>

    /**
     * 搜索联系人
     */
    @Query(
        """
        SELECT * FROM Contacts 
        WHERE myPhone = :myPhone AND friendPhone LIKE '%' || :keyword || '%' 
        OR friendPhone = :myPhone AND myPhone LIKE '%' || :keyword || '%'
    """
    )
    fun searchContacts(myPhone: String, keyword: String): List<Contacts>
    /**
     * 检查是否是好友
     */
    @Query("SELECT EXISTS(SELECT 1 FROM Contacts WHERE (myPhone = :myPhone AND friendPhone = :friendPhone) OR (myPhone = :friendPhone AND friendPhone = :myPhone))")
    fun isFriend(myPhone: String, friendPhone: String): Boolean

    /**
     * 删除联系人
     */
    @Query("DELETE FROM Contacts WHERE myPhone = :phone AND friendPhone = :friendPhone")
    fun deleteContacts(phone: String, friendPhone: String)

    /**
     * 更新昵称
     */
    @Query("UPDATE Contacts SET myNickName = :nickName, friendNickName = :friendNickName WHERE myPhone = :phone AND friendPhone = :friendPhone")
    fun updateNickName(phone: String, friendPhone: String, nickName: String, friendNickName: String)
}
