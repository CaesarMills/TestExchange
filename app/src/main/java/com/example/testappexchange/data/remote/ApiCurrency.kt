package com.example.testappexchange.data.remote

import com.example.testappexchange.core.util.Constance.Companion.CURRENCY_END_POINT_URL
import com.example.testappexchange.data.remote.models.CurrencyDTO
import retrofit2.Response
import retrofit2.http.GET

interface ApiCurrency {
    @GET(CURRENCY_END_POINT_URL)
    suspend fun getCurrency(): CurrencyDTO
}