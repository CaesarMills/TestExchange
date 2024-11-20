package com.example.testappexchange.data.local.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testappexchange.core.util.Constance.Companion.USER_ACCOUNT_TABLE
import com.example.testappexchange.domain.models.BalanceCurrency
import com.example.testappexchange.domain.models.Currency

@Entity(tableName = USER_ACCOUNT_TABLE)
data class UserAccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val login: String,
    val currencies : List<BalanceCurrency>,
    val transactionNum: Int
)
