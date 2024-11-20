package com.example.testappexchange.data.local.database


import androidx.room.TypeConverter
import com.example.testappexchange.domain.models.BalanceCurrency
import com.example.testappexchange.domain.models.Currency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CurrencyTypeConverter {
    private val gson = Gson()
    private val listType = object : TypeToken<List<BalanceCurrency>>() {}.type

    @TypeConverter
    fun currencyToString(list: List<BalanceCurrency>): String{
        return gson.toJson(list, listType)
    }

    @TypeConverter
    fun stringToCurrency(data: String): List<BalanceCurrency> {
        return gson.fromJson(data,listType)
    }
}