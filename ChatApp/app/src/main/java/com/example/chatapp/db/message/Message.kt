package com.example.chatapp.db.message

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fromAccount: String,
    val toAccount: String,
    val content: String,
    val deleteUser: String,
    val revokeFlag: Boolean,
    val timestamp: Long,
)
