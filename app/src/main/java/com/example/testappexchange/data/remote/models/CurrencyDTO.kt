package com.example.testappexchange.data.remote.models

import com.example.testappexchange.domain.models.Currency
import com.google.gson.annotations.SerializedName

data class  CurrencyDTO(
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val rates: Map<String, Double>
){
    fun ratesToCurrencyList(): List<Currency>{
        return rates.map {
            Currency(
                it.key,
                it.value
            )
        }
    }
}
