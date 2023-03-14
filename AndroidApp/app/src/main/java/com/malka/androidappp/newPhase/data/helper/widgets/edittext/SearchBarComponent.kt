package com.malka.androidappp.newPhase.data.helper.widgets.edittext

import android.content.Context
import android.content.res.TypedArray
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.domain.models.servicemodels.Product
import kotlinx.android.synthetic.main.item_search.view.*


class SearchBarComponent : LinearLayout {

    private lateinit var suggestion_rcv: RecyclerView
    private lateinit var edittext: EditText
    private lateinit var itemLayout: LinearLayout
    var adapter:GenericListAdapter<Product>?=null

    var suggestionList: ArrayList<Product> = ArrayList()

    var textHint: CharSequence? = null
    private var maxLength: Int? = null
    private var textText: CharSequence? = null


    private var focusImpList = ArrayList<(View, Boolean) -> Unit>()
    val filterArray: ArrayList<InputFilter> = ArrayList()
    private lateinit var iv_end_icon: ImageView
    var onClick: ((product:Product) -> Unit)? = null


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
        inflater.inflate(R.layout.item_searchbar, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        suggestion_rcv = findViewById(R.id.suggestion_rcv)
        suggestion_rcv.id = TextFieldIdGenerator.viwId.getlllId(id)
        iv_end_icon = findViewById(R.id.iv_end_icon)
        iv_end_icon.id = TextFieldIdGenerator.viwId.getllId(id)
        edittext = findViewById(R.id.edittext)
        edittext.id = TextFieldIdGenerator.viwId.getStartIconID(id)
        itemLayout = findViewById(R.id.itemLayout)
        itemLayout.id = TextFieldIdGenerator.viwId.getStartIconIDD(id)




        _setHint(textHint, true)
        text = textText


        setupOnFocusChangeListener()
        if (filterArray.size > 0) {
            edittext.filters = filterArray.toTypedArray()
        }
        adapter = object : GenericListAdapter<Product>(
            R.layout.item_search,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        val result = "$name ${context.getString(R.string.in_)} $category"
                        val spannable: Spannable = SpannableString(result)
                        spannable.setSpan(
                            ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimary)),
                            result
                                .indexOf(category),
                            result.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        query_tv.setText(spannable, TextView.BufferType.SPANNABLE)
                        setOnClickListener {
                            onClick?.invoke(this)
                        }
                    }

                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(
                suggestionList
            )
        }

        suggestion_rcv.adapter=adapter

    }

    fun onClickListener(onClick_: ((product:Product) -> Unit)? = null) {
        onClick=onClick_
    }

    fun updateList(newList: ArrayList<Product>) {
        suggestionList = newList
        adapter?.submitList(suggestionList)
    }


    public fun _setMaxLength(maxLength: Int?) {
        val maxLen = if (maxLength != null && maxLength > 0) maxLength else null

        maxLen?.let {
            filterArray.add(InputFilter.LengthFilter(it))
        }
    }


    public fun _setHint(textHint: CharSequence?, initialisation: Boolean = false) {
        this.textHint = textHint



        edittext.hint = textHint
    }


    public fun _onChange(postOnTextChanged: (String) -> Unit) {
        edittext.addTextChangedListener(object : TextWatcher {
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
        edittext.setSelection(index)
    }

    var text: CharSequence?
        get() = edittext.text
        set(value) {
            textText = value
            edittext.setText(value)
        }


    private fun setupOnFocusChangeListener() {
        edittext.setOnFocusChangeListener { view, b ->

            if (focusImpList.isNotEmpty()) {
                focusImpList.forEach {
                    it.invoke(view, b)
                }
            }
        }
    }


    public fun _view2(): EditText {
        return edittext
    }


    public fun getText(): String {

        return edittext.text.toString()

    }

    public fun setText(text: String?) {

        edittext.setText(text ?: "")

    }

    public fun _attachInfoClickListener(listener: OnClickListener) {
        iv_end_icon.setOnClickListener(listener)
    }
    fun getStyles(context: Context, attrs: AttributeSet?): TypedArray {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.SearchBar,
            0, 0
        )

        a.run {
            textHint = getText(R.styleable.SearchBar_hint)
            print(textHint)
        }
//        a.recycle()
        return a
    }


}