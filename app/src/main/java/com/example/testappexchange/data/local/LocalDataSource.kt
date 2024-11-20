package com.example.testappexchange.data.local



import com.example.testappexchange.data.local.database.UserAccountDao
import com.example.testappexchange.data.local.database.UserProfileDao
import com.example.testappexchange.data.local.models.UserAccountEntity
import com.example.testappexchange.data.local.models.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val daoProfile: UserProfileDao,
    private val daoAccount: UserAccountDao
) {
    suspend fun saveUserProfile(userProfileEntity: UserProfileEntity){
        daoProfile.saveUser(userProfileEntity)
    }
    suspend fun isLoginExist(name: String): UserProfileEntity?{
        return daoProfile.isLoginExist(name)
    }
    suspend fun login(login: String, pin: String): UserProfileEntity?{
        return daoProfile.login(login, pin)
    }
    fun getAccountInfoFlow(login: String): Flow<UserAccountEntity> {
        return daoAccount.getAccountInfoFlow(login)
    }
    suspend fun updateUserAccount(userAccountEntity: UserAccountEntity){
        daoAccount.updateUserAccount(userAccountEntity)
    }

    suspend fun deleteAll(){
        daoAccount.deleteAll()
        daoProfile.deleteAll()
    }
}