package com.example.testappexchange.presentation.loginScreen

import androidx.lifecycle.ViewModel
import com.example.testappexchange.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    suspend fun loginIn(login: String, pin: String): Boolean =
        userRepository.loginIn(login = login, pin = pin)
}