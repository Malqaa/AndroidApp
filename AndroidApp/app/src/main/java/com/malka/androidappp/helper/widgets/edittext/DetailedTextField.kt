package com.malka.androidappp.helper.widgets.edittext

import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.Selection
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import com.malka.androidappp.R
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.show
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem

class DetailedTextField : LinearLayout {

    val selectedItem: Any
        get() {
            TODO()
        }
    private lateinit var etl_Field: TextInputLayout
    private lateinit var textView: TextView
    private lateinit var iv_info_icon: ImageView
    private lateinit var iv_start_icon: ImageView
    private lateinit var iv_end_icon: ImageView
    private lateinit var line: View
    private lateinit var cppfield: CountryCodePicker
    private lateinit var et_Field: TextInputEditText


    private var enableTopLine = true
    private var enableBottomLine = true


    private var enablePasswordToggle = false
    private var startIcon =0
    private var endIcon =0

    private var isLastField = false
    var textHint: CharSequence? = null
    private var inputType = EditorInfo.TYPE_CLASS_TEXT
    private var maxLength: Int? = null
    private var textText: CharSequence? = null
    private var viewType = 0


    private var focusImpList = ArrayList<(View, Boolean) -> Unit>()
    val filterArray: ArrayList<InputFilter> = ArrayList()

    private lateinit var tv_Error: TextView

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
        inflater.inflate(R.layout.item_detailed_text_field_description, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        // Sets the images for the previous and next buttons. Uses
        // built-in images so you don't need to add images, but in
        // a real application your images should be in the
        // application package so they are always available.
        etl_Field = findViewById(R.id.tvl_field)
        etl_Field.id = DetailedTextFieldIdGenerator.viwId.getId(id)

        iv_start_icon = findViewById(R.id.iv_start_icon)
        iv_start_icon.id = DetailedTextFieldIdGenerator.viwId.getlId(id)


        iv_end_icon = findViewById(R.id.iv_end_icon)
        iv_end_icon.id = DetailedTextFieldIdGenerator.viwId.getllId(id)


        line = findViewById(R.id.line)
        line.id = DetailedTextFieldIdGenerator.viwId.getlllId(id)


        cppfield = findViewById(R.id.cppfield)
        cppfield.id = DetailedTextFieldIdGenerator.viwId.getllllId(id)


        et_Field = findViewById(R.id.tv_field)
        et_Field.id = DetailedTextFieldIdGenerator.viwId.getlllllId(id)

        tv_Error = findViewById(R.id.tv_error)
        tv_Error.id = DetailedTextFieldIdGenerator.viwId.getllllllId(id)


        textView = findViewById(R.id.textView)
        textView.id = DetailedTextFieldIdGenerator.viwId.getInfoIconID(id)

        tv_Error.hide()


        _setViewType(viewType, true)

        _setLastField(isLastField)
//        _setDescriptionEnabled(enableDescription, arrayListOf(textDescription))
        _setStartIcon(startIcon)
        _setEndIcon(endIcon)
        _setHint(textHint, true)
        _setInputType(inputType)
        _setMaxLength(maxLength)
        text = textText
        _setPasswordToggle(enablePasswordToggle)

        addFocusImplementations { view, b ->
//            rv_Description.visibility = if (b && enableDescription) View.VISIBLE else View.GONE
        }

        setupOnFocusChangeListener()
        if (filterArray.size > 0) {
            et_Field.filters = filterArray.toTypedArray()
        }
    }

    var error: String?
        get() = tv_Error.text.toString()
        set(value) {
            if(value==null){
                tv_Error.hide()
            }else{
                tv_Error.show()
            }
            tv_Error.text = value
        }



    public fun hideError() {
        tv_Error.hide()
    }



    public fun _setPasswordToggle(enabled: Boolean) {
        enablePasswordToggle = enabled
        if (enabled) {
            etl_Field.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE)
            etl_Field.setPasswordVisibilityToggleTintList(AppCompatResources.getColorStateList(context, R.color.line));

        }

    }

    public fun _setStartIcon(drawable: Int) {
        if(drawable!=0){
            iv_start_icon.show()
            line.show()

            startIcon = drawable

            iv_start_icon.setImageDrawable(ContextCompat.getDrawable(context, drawable))

        }else{
            iv_start_icon.hide()
            line.hide()
        }

    }

    public fun _setEndIcon(drawable: Int) {
        if(drawable!=0){
            iv_end_icon.show()

            endIcon = drawable

            iv_end_icon.setImageDrawable(ContextCompat.getDrawable(context, drawable))

        }else{
            iv_end_icon.hide()
        }

    }


    public fun _setMaxLength(maxLength: Int?) {
        val maxLen = if (maxLength != null && maxLength > 0) maxLength else null

        maxLen?.let {
            filterArray.add(LengthFilter(it))
        }
    }

    public fun _setHint(textHint: CharSequence?, initialisation: Boolean = false) {
        this.textHint = textHint

        if (etl_Field.isHintEnabled)
            etl_Field.hint = textHint
        else
            et_Field.hint = textHint

        if (!initialisation)
            checkHint()
    }

    private fun toggleHintText() {
        if (etl_Field.isHintEnabled) {
            val h = etl_Field.hint
            etl_Field.hint = null
            etl_Field.isHintEnabled = false
            et_Field.hint = h
        } else {
            val h = et_Field.hint
            et_Field.hint = null
            etl_Field.isHintEnabled = true
            etl_Field.hint = h
        }
    }

    public fun _setInputType(inputType: Int) {
        this.inputType = inputType
        et_Field.inputType = inputType
        // 8194 fixed ID for Decimal Input Type
        if (et_Field.inputType == 8194) {
         //   filterArray.add(DecimalDigitsInputFilter(3))
        }
    }

    public fun _setLastField(isLastField: Boolean) {
        this.isLastField = isLastField
        et_Field.imeOptions = if (isLastField) EditorInfo.IME_ACTION_DONE else EditorInfo.IME_ACTION_NEXT
    }

    fun checkHint() {

        if (_getisFocusable()) {
            if (_getisFocused()) {

                if (!etl_Field.isHintEnabled)
                    toggleHintText()

            } else {

                if (et_Field.text.isNullOrEmpty() && etl_Field.isHintEnabled) {
                    toggleHintText()
                } else if (!et_Field.text.isNullOrEmpty() && !etl_Field.isHintEnabled) {
                    toggleHintText()
                }

            }
        } else {

            if (text.isNullOrEmpty() && etl_Field.isHintEnabled) {
                toggleHintText()
            } else if (!text.isNullOrEmpty() && !etl_Field.isHintEnabled) {
                toggleHintText()
            }

        }

        /*if(!_getisFocusable()) {
            if (text.isNullOrEmpty()) {
                if (etl_Field.isHintEnabled) {
                    toggleHintText()
                }
            } else {
                if (!etl_Field.isHintEnabled)
                    toggleHintText()
            }
        }*/
    }


    public fun _attachInfoClickListener(listener: OnClickListener?) {
        iv_info_icon.setOnClickListener(listener)
    }

    public fun view_parentInputLayout(): TextInputLayout {
        return etl_Field
    }

    public fun _setOnClickListener(listener: OnClickListener) {
        et_Field.setOnClickListener(listener)
    }

    public fun showSpinner(items: List<SearchableItem>,title:String,selection: (SearchableItem) -> Unit) {

        SearchableDialog(context,
            items,
            title,
            {item, _ ->
                selection.invoke(item)
            },
            cancelButtonColor = ContextCompat.getColor(context, R.color.bg),
            onlyLightTheme = true).show()
    }
    public fun _setOnLongClickListener(listener: OnLongClickListener) {
        et_Field.setOnLongClickListener(listener)
    }

    public fun _addTextChangedListener(listener: TextWatcher) {
        et_Field.addTextChangedListener(listener)
    }

    public fun _onChange(postOnTextChanged: (String) -> Unit) {
        et_Field.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                postOnTextChanged(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    public fun _setSelection(index: Int) {
        et_Field.setSelection(index)
    }

    var text: CharSequence?
        get() = et_Field.text
        set(value) {
            textText = value
            et_Field.setText(value)
            textView.setText(value)
        }

    fun selectedCountryCode():String{
        return cppfield.selectedCountryCode
    }


    fun _getisFocusable() = et_Field.isFocusable

    fun _getisFocused() = et_Field.isFocused

    private fun setupOnFocusChangeListener() {
        et_Field.setOnFocusChangeListener { view, b ->

            if (focusImpList.isNotEmpty()) {
                focusImpList.forEach {
                    it.invoke(view, b)
                }
            }
        }
    }

    public fun addFocusImplementations(listener: (View, Boolean) -> Unit) {
        focusImpList.add(listener)
    }

    public fun removeFocusImplementations(index: Int) {
        focusImpList.removeAt(index)
    }

    public fun _view(): TextInputEditText {
        return et_Field
    }


    public fun getText(): String {
        return et_Field.text.toString()
    }
    public fun setText(text:String?) {
        et_Field.setText(text?:"")
    }
/*    public fun showError(text: String) {
        tv_Error.text = text
        tv_Error.visible()
    }

    public fun hideError() {
        tv_Error.gone()
    }*/

    fun getStyles(context: Context, attrs: AttributeSet?): TypedArray {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.DetailedTextField,
            0, 0
        )

        a.run {
//            textDescription = DetailedTextDescription(DetailedTextDescriptionType.Normal, getString(R.styleable.DetailedTextField_textDescription) ?: "Lorem Ipsum")
//            enableDescription = getBoolean(R.styleable.DetailedTextField_enableDescription, false)
            enablePasswordToggle = getBoolean(R.styleable.DetailedTextField_enablePasswordToggle, false)
            textHint = getText(R.styleable.DetailedTextField_android_hint)
            inputType = getInt(R.styleable.DetailedTextField_android_inputType, EditorInfo.TYPE_CLASS_TEXT)
            maxLength = getInt(R.styleable.DetailedTextField_android_maxLength, -1)
            textText = getText(R.styleable.DetailedTextField_android_text)
            viewType = getInt(R.styleable.DetailedTextField_viewType, 0)

            startIcon = getResourceId(R.styleable.DetailedTextField_startIcon, 0)
            endIcon = getResourceId(R.styleable.DetailedTextField_endIcon, 0)
            isLastField = getBoolean(R.styleable.DetailedTextField_lastfield, false)
            enableTopLine = getBoolean(R.styleable.DetailedTextField_enableTopLine, true)
            enableBottomLine = getBoolean(R.styleable.DetailedTextField_enableBottomLine, true)

        }


//        a.recycle()
        return a
    }
    public fun _setViewType(viewType: Int, initialisation: Boolean = false) {

        if (this.viewType == viewType) {
            if (!initialisation)
                return
        }

        this.viewType = viewType
        when (viewType) {
            0 -> {
                cppfield.hide()
                et_Field.isFocusable = true
                et_Field.isFocusableInTouchMode = true
                et_Field.isClickable = false
            }
            1 -> {
                cppfield.hide()
                et_Field.isFocusable = false
                et_Field.isFocusableInTouchMode = false
                et_Field.isClickable = true
            }
            2 -> {
                cppfield.show()
                line.show()
                et_Field.isFocusable = true
                et_Field.isFocusableInTouchMode = true
                et_Field.isClickable = false
            }
            3 -> {
                etl_Field.hide()
                textView.show()
            }
            else -> {


            }
        }

    }


}