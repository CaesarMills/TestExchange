package com.example.testappexchange.data.local.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testappexchange.core.util.Constance.Companion.USER_PROFILE_TABLE

@Entity(tableName = USER_PROFILE_TABLE)
data class UserProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val mail: String,
    val login: String,
    val pin: String,
)
