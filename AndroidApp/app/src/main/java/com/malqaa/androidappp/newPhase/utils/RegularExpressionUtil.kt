package com.malqaa.androidappp.newPhase.utils

fun isTextInArabic(text: String): Boolean {
    val arabicPattern = Regex("[\\u0600-\\u06FF\\u0750-\\u077F\\u08A0-\\u08FF]")
    return arabicPattern.containsMatchIn(text)
}

fun isTextInEnglish(text: String): Boolean {
    val englishPattern = Regex("[A-Za-z]")
    return englishPattern.containsMatchIn(text)
}