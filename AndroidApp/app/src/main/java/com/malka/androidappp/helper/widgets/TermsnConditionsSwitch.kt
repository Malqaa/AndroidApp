package com.malka.androidappp.helper.widgets

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.malka.androidappp.R
import com.malka.androidappp.helper.widgets.edittext.TextFieldIdGenerator


class TermsnConditionsSwitch : LinearLayout {

    private lateinit var tv_Switch: SwitchCompat

    private lateinit var ctv_Link: TextView

    private var textSwitch: CharSequence? = null
    private var textLink: CharSequence? = null


    private var isChecked = false

    constructor(context: Context) : super(context) {
        initializeViews(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        getStyles(context, attrs)
        initializeViews(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
        getStyles(context, attrs)
        initializeViews(context)
    }

    /**
     * Inflates the views in the layout.
     *
     * @param context
     * the current context for the view.
     */
    private fun initializeViews(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.compound_terms_n_conditions_switch, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        // Sets the images for the previous and next buttons. Uses
        // built-in images so you don't need to add images, but in
        // a real application your images should be in the
        // application package so they are always available.

        tv_Switch = findViewById(R.id.tv_switch)
        tv_Switch.id = TextFieldIdGenerator.viwId.getllllllId(id)

//        ctv_Text = findViewById(R.id.ctv_text)
//        ctv_Text.id = DetailedTextFieldIdGenerator.viwId.getStartIconID(id)

        ctv_Link = findViewById(R.id.ctv_link)
        ctv_Link.id = TextFieldIdGenerator.viwId.getStartIconID(id)

        _setChecked(isChecked)
        text = textSwitch
//        texturl = textLink

        ctv_Link.setOnClickListener {
            val defaultLink="https://www.google.com/"
            defaultLink.viewTextIntent(context!! as Activity) {
                print(it)
            }
        }
    }
    // This extension function is used to open link  externally
    fun String.viewTextIntent(activity: Activity, onFailed: (error: String) -> Unit) {
        try {
            val uri = Uri.parse(this)
            activity.startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (error: Exception) {
            onFailed.invoke(error.message!!)
        }

    }

    public fun _setChecked(checked: Boolean) {
        this.isChecked = checked
        tv_Switch.isChecked = checked
    }

    public fun _getChecked() = tv_Switch.isChecked

    public fun _setOnClickListener(listener: OnClickListener) {
        tv_Switch.setOnClickListener(listener)
    }

    public fun _setOnLinkClickListener(listener: OnClickListener) {
        ctv_Link.setOnClickListener(listener)
    }
    fun String?.isNotNullOrEmpty(): String? {
        if (this.isNullOrBlank())
            return null

        return this
    }
    var text: CharSequence?
        get() = textSwitch
        set(value) {
            textSwitch = value

            textLink.toString().isNotNullOrEmpty()?.let {
                var text2 = if(textSwitch.isNullOrEmpty()) it else "$textSwitch $it"

                val spannable: Spannable = SpannableString(text2)
                spannable.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, R.color.bg)),
                    textSwitch?.length ?: 0,
                    text2.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                ctv_Link.setText(spannable, TextView.BufferType.SPANNABLE)
            } ?: kotlin.run {
                ctv_Link.text = textSwitch
            }
        }

    /*var texturl: CharSequence?
        get() = textLink
        set(value) {
            textLink = value
            var s = textLink
            textSwitch.toString().isNotNullOrEmpty()?.let {
                s = "$s $it"
            }
            ctv_Link.text = s
        }*/


    public fun _view(): SwitchCompat {
        return tv_Switch
    }

    public fun _setOnCheckedChangeListener(listener: CompoundButton.OnCheckedChangeListener) {
        tv_Switch.setOnCheckedChangeListener(listener)
    }

    fun getStyles(context: Context, attrs: AttributeSet?): TypedArray {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.TermsnConditionsSwitch,
            0, 0
        )

        a.run {
            textSwitch = getText(R.styleable.TermsnConditionsSwitch_textSwitch)
            textLink = getText(R.styleable.TermsnConditionsSwitch_textLink)
            isChecked = getBoolean(R.styleable.TermsnConditionsSwitch_android_checked, false)
        }

        return a
    }


}