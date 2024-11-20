package com.example.testappexchange.data.remote

import android.util.Log
import com.example.testappexchange.domain.models.Currency
import com.example.testappexchange.domain.repositories.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.retryWhen
import okio.IOException
import javax.inject.Inject
import kotlin.jvm.Throws

class RepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource
) : Repository {
    override suspend fun getCurrenciesList(): Flow<List<Currency>> = flow {
        while (true) {
                val response = remote.getCurrencies()
                emit(response.ratesToCurrencyList())
                delay(5000)
        }
    }
}