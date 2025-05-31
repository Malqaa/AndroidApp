package com.malqaa.androidappp.newPhase.utils

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan

fun String.toStyledSpannable(
    highlightPart: String,
    sizeInSp: Int = 20,
    isBold: Boolean = true
): SpannableStringBuilder {
    val spannable = SpannableStringBuilder(this)
    val start = this.indexOf(highlightPart)
    if (start == -1) return spannable
    val end = start + highlightPart.length

    if (isBold) {
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    spannable.setSpan(
        AbsoluteSizeSpan(sizeInSp, true), // use SP
        start,
        end,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return spannable
}
