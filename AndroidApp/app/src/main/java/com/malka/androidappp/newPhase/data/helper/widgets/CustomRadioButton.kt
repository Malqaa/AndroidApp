package com.malka.androidappp.newPhase.data.helper.widgets

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.widgets.edittext.TextFieldIdGenerator

class CustomRadioButton : LinearLayout {


    private var isChecked = false

    private var textText: CharSequence? = null


    private lateinit var radio_button: RadioButton
    private lateinit var radio_text: TextView
    private lateinit var radio_bg: LinearLayout


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

    private fun initializeViews(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.radio_item, this)
    }

    fun getStyles(context: Context, attrs: AttributeSet?): TypedArray {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomRadioButton,
            0, 0
        )

        a.run {

            textText = getText(R.styleable.CustomRadioButton_android_text)
            isChecked = getBoolean(R.styleable.CustomRadioButton_android_checked, false)

        }
        return a
    }

    override fun onFinishInflate() {
        super.onFinishInflate()



        radio_button = findViewById(R.id.radio_button)
        radio_button.id = TextFieldIdGenerator.viwId.getId(id)


        radio_text = findViewById(R.id.radio_text)
        radio_text.id = TextFieldIdGenerator.viwId.getlId(id)


        radio_bg = findViewById(R.id.radio_bg)
        radio_bg.id = TextFieldIdGenerator.viwId.getllId(id)
        _setText(textText.toString())
        _setCheck(isChecked)
    }
    public fun getText() :String{
       return  radio_text.text.toString()

    }
    public fun _setText(text: String) {
        radio_text.text = text

    }
    public fun getCheck():Boolean{
        return radio_button.isChecked

    }
    public fun _setCheck(isChecked: Boolean) {
        if (isChecked) {
            radio_bg.background=ContextCompat.getDrawable(context,R.drawable.radio_active_bg)
            radio_text.setTextColor(ContextCompat.getColor(context, R.color.bg))
        } else {
            radio_bg.background=ContextCompat.getDrawable(context,R.drawable.radio_inactive_bg)
            radio_text.setTextColor(ContextCompat.getColor(context, R.color.text_color))

        }
        radio_button.isChecked=isChecked
        radio_button.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

            }

        })
    }

    public fun _setOnClickListener(listener: OnClickListener) {
        radio_bg.setOnClickListener(listener)
    }

}