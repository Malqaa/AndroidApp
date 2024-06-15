package com.malqaa.androidappp.newPhase.utils

import android.content.Context
import android.text.Html
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.malqaa.androidappp.R


class ReadMoreTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    AppCompatTextView(context, attrs) {
    var originalText: CharSequence? = null
        private set
    private var trimmedText: CharSequence? = null
    private var bufferType: BufferType? = null
    private var isTrimmed = true
    private var trimLength: Int

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ReadMoreTextView)
        trimLength = typedArray.getInt(R.styleable.ReadMoreTextView_trimLength, DEFAULT_TRIM_LENGTH)
        typedArray.recycle()
        setOnClickListener {
            isTrimmed = !isTrimmed
            setText()
            requestFocusFromTouch()
        }
    }

    private fun setText() {
        super.setText(displayableText, bufferType)
    }

    private val displayableText: CharSequence?
        private get() = if (isTrimmed) trimmedText else originalText

    override fun setText(text: CharSequence, type: BufferType) {
        originalText = text
        trimmedText = getTrimmedText(text)
        bufferType = type
        setText()
    }

    private fun getTrimmedText(text: CharSequence?): CharSequence? {

        val visitorStatusValue =
            Html.fromHtml("<font color=\"#EE6C4D\">" + context.resources.getString(R.string.Showmore) + "</font>")
        return if (originalText != null && originalText!!.length > trimLength) {
            SpannableStringBuilder(originalText, 0, trimLength + 1).append(visitorStatusValue)
        } else {
            originalText
        }
    }

    fun setTrimLength(trimLength: Int) {
        this.trimLength = trimLength
        trimmedText = getTrimmedText(originalText)
        setText()
    }

    fun getTrimLength(): Int {
        return trimLength
    }

    companion object {
        private const val TAG = "ReadMoreTextView"
        private const val DEFAULT_TRIM_LENGTH = 200
//        private const val ELLIPSIS = context.getString(R.string.Showmore)
    }
}
