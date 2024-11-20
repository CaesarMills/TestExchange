package com.example.testappexchange.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Currency(
    val name: String,
    val rate: Double
): Parcelable