package com.example.testappexchange.data.local.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testappexchange.data.local.models.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun saveUser(userProfileEntity: UserProfileEntity)

    @Query("SELECT * FROM USER_TABLE WHERE login = :login")
    suspend fun isLoginExist(login: String): UserProfileEntity?

    @Query("SELECT * FROM USER_TABLE WHERE pin = :pin AND login = :login")
    suspend fun login(login: String, pin: String): UserProfileEntity?

    @Query("DELETE FROM USER_TABLE")
    suspend fun deleteAll()
}