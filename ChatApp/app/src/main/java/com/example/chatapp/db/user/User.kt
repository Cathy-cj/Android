package com.example.chatapp.db.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val phone: String,
    val nikeName: String,
    val password: String,
    val headPic: String?,
)