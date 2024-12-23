package com.example.testappexchange.core.util

sealed class DataStatus <T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T): DataStatus<T>(data)
    class Error<T>(message: String?, data: T? = null): DataStatus<T>(data, message)
    class Loading<T>: DataStatus<T>()
}