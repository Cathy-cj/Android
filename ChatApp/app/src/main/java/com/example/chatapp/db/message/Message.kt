package com.example.chatapp.db.message

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message(
    @PrimaryKey
    val id: String,
    val fromAccount: String,
    val toAccount: String,
    val content: String,
    val deleteUser: String,
    val revokeFlag: Boolean,
    val timestamp: Long,
)
