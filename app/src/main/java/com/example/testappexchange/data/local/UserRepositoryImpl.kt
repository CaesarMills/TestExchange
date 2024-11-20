package com.example.testappexchange.data.local



import com.example.testappexchange.data.local.models.UserAccountEntity
import com.example.testappexchange.data.local.models.UserProfileEntity
import com.example.testappexchange.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class UserRepositoryImpl@Inject constructor(
    private val local: LocalDataSource
): UserRepository {
    override suspend fun saveNewUserProfile(userProfileEntity: UserProfileEntity){
        local.saveUserProfile(userProfileEntity = userProfileEntity)
    }
    override suspend fun saveNewUserAccount(userAccountEntity: UserAccountEntity) {
        local.updateUserAccount(userAccountEntity = userAccountEntity)
    }
    override suspend fun isLoginExist(login: String): Boolean {
        val newUser = local.isLoginExist(login) ?: return false
        return newUser.login == login
    }
    override fun getAccountInfoFlow(login: String): Flow<UserAccountEntity> {
        return local.getAccountInfoFlow(login = login)
    }
    override suspend fun loginIn(login: String, pin: String): Boolean {
        return local.login(login,pin)!=null
    }
}