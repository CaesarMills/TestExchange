package com.example.testappexchange.domain.repositories


import com.example.testappexchange.data.local.models.UserAccountEntity
import com.example.testappexchange.data.local.models.UserProfileEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveNewUserProfile(userProfileEntity: UserProfileEntity)
    suspend fun saveNewUserAccount(userAccountEntity: UserAccountEntity)
    suspend fun isLoginExist(login: String): Boolean
    fun getAccountInfoFlow(login: String): Flow<UserAccountEntity>
    suspend fun loginIn(login: String, pin: String):Boolean
}