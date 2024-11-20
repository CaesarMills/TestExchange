package com.example.testappexchange.data.remote

import com.example.testappexchange.data.remote.models.CurrencyDTO
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: ApiCurrency
) {
    suspend fun getCurrencies(): CurrencyDTO {
        return api.getCurrency()
    }
}