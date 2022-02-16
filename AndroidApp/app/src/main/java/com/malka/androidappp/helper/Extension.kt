package com.malka.androidappp.helper

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object Extension {


    fun Double.decimalNumberFormat(): String {

        val limit =  3

        var aNumber = ""
        try {
            val format = "%.${limit}f"
            val decimalNumber = String.format(Locale.ENGLISH, format, this)
            var pattern = "###,###,###,###,##0"

            if (limit > 0) {
                //pattern += "."

                for (i in 1..limit) {
                   // pattern += "0"
                }
            }

            val formatter = DecimalFormat(pattern, DecimalFormatSymbols(Locale.ENGLISH))
            aNumber = formatter.format(decimalNumber.toDouble())
        } catch (error: Exception) {
            error.printStackTrace()
            aNumber = this.toString()
        }
        return aNumber
    }
}