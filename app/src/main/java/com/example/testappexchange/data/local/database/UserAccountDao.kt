package com.example.testappexchange.data.local.database


import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.testappexchange.data.local.models.UserAccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAccountDao {
    @Query("SELECT * FROM user_account_table WHERE login = :login")
    fun getAccountInfoFlow(login: String): Flow<UserAccountEntity>

    @Upsert
    suspend fun updateUserAccount(userAccountEntity: UserAccountEntity)

    @Query("DELETE FROM user_account_table")
    suspend fun deleteAll()
}