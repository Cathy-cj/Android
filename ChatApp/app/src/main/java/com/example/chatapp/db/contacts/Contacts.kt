package com.example.chatapp.db.contacts

import androidx.room.Entity

@Entity(primaryKeys = ["myPhone", "friendPhone"])
data class Contacts(
    val myPhone: String,
    val myName: String,
    val myNickName: String,
    val friendPhone: String,
    val friendName: String,
    val friendNickName: String,
    val deleteFlag: Boolean
)