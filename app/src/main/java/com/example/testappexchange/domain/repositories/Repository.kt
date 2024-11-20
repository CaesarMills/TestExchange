package com.example.testappexchange.domain.repositories

import com.example.testappexchange.domain.models.Currency
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getCurrenciesList(): Flow<List<Currency>>
}