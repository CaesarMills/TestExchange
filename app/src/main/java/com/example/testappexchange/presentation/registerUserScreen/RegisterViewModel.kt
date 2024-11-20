package com.example.testappexchange.presentation.registerUserScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testappexchange.data.local.UserRepositoryImpl
import com.example.testappexchange.data.local.models.UserAccountEntity
import com.example.testappexchange.data.local.models.UserProfileEntity
import com.example.testappexchange.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel(){
    fun saveUserProfile(userProfileEntity: UserProfileEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.saveNewUserProfile(userProfileEntity = userProfileEntity)
        }
    }
    fun saveUserAccount(userAccountEntity: UserAccountEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.saveNewUserAccount(userAccountEntity = userAccountEntity)
        }
    }
    suspend fun isLoginExist(name: String): Boolean =
        userRepository.isLoginExist(name)
}