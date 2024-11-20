package com.example.testappexchange.core.util

import android.util.Patterns
import com.example.testappexchange.domain.models.Fee
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


fun String.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

val df = DecimalFormatSymbols(Locale.UK)
private fun roundTwo() = DecimalFormat("##.##", df)
private fun roundFour() = DecimalFormat("##.####", df)

fun Double.toTwo() = roundTwo().format(this).toDouble()

fun Double.toFour() = roundFour().format(this).toDouble()

fun Double.calculateFee(transactionNum: Int): Double {
    if (this > 200 || transactionNum <= 5) {
        return (this * Fee.FREE.fee)
    }
    if (transactionNum % 10 == 0) {
        return (this * Fee.EVERYTENTH.fee)
    } else
        return (this * Fee.STANDARD.fee)
}
