package com.example.testappexchange.domain.models

enum class Fee(val fee: Double){
    FREE(0.0),
    STANDARD(0.07),
    EVERYTENTH(0.01)
}