package com.example.testappexchange.domain.models

data class BalanceCurrency(
    val name: String,
    //In real app we need use BigDecimal, but in test app it be too complicated
    var balance: Double
)