package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity5

import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationItem
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.SubSpecificationItem
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.dpToPx
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DynamicSpecificationsAdapter(
    private var dynamicSpecificationList: List<DynamicSpecificationItem>,
    private var onChangeValueListener: OnChangeValueListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val spinnerType = 1 // and 7
    private val textBoxType = 2 // and 3
    private val numberType = 4
    private val radioType = 5
    private val checkType = 6

    class TextBoxViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitleAr: TextView = view.findViewById(R.id.tvTitleAr)
        val tvTitleEn: TextView = view.findViewById(R.id.tvTitleEn)
        val txtRequired: TextView = view.findViewById(R.id.txtRequired)
        val txtRequiredEn: TextView = view.findViewById(R.id.txtRequiredEn)
        val etValueEn: EditText = view.findViewById(R.id.etValueEn)
        val etValueAr: EditText = view.findViewById(R.id.etValueAr)
    }

    class SpinnerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitleAr)
        val spinner: Spinner = view.findViewById(R.id.spinnerValues)
        val txtRequired: TextView = view.findViewById(R.id.txtRequired)
    }

    class CheckViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitleAr: TextView = view.findViewById(R.id.tvTitleAr)
    }

    lateinit var context: Context
    override fun getItemViewType(position: Int): Int {
        //type 1->dropdownlist  ,
        if (dynamicSpecificationList[position].type == 1 || dynamicSpecificationList[position].type == 7) {
            if (dynamicSpecificationList[position].subSpecifications!!.isNotEmpty()) {
                //dropDown
                return spinnerType
            } else {
                // text box
                return textBoxType
            }
        } else if (dynamicSpecificationList[position].type == 6) {
            return checkType
        } else if (dynamicSpecificationList[position].type == 5) {
            return radioType
        } else {
            // text box
            return textBoxType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        if (viewType == spinnerType) {
            return SpinnerViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_drop_down,
                    parent,
                    false
                )
            )
        } else if (viewType == checkType) {
            return CheckViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_check,
                    parent,
                    false
                )
            )
        } else if (viewType == radioType) {
            return CheckViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_check,
                    parent,
                    false
                )
            )
        } else {
            return TextBoxViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_text_boxs,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int = dynamicSpecificationList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == spinnerType) {
            setSpinnerView(holder as SpinnerViewHolder, position)
        } else if (getItemViewType(position) == checkType) {
            setCheckView(holder as CheckViewHolder, position)
        } else if (getItemViewType(position) == radioType) {
            setCheckView(holder as CheckViewHolder, position)
        } else {
            setTextBoxView(holder as TextBoxViewHolder, position)
        }
    }

    private fun setCheckView(
        checkViewHolder: CheckViewHolder,
        position: Int
    ) {
        val dynamicContainer =
            checkViewHolder.itemView.findViewById<LinearLayout>(R.id.dynamicContainer)
        dynamicContainer.removeAllViews() // Clear any previous dynamic views

        val dynamicSpecItem = dynamicSpecificationList[position]

        // Create a TextView for the title dynamically
        val titleTextView = TextView(context).apply {
            text = if (ConstantObjects.currentLanguage == "ar") {
                dynamicSpecItem.nameAr ?: "Default Title (AR)"
            } else {
                dynamicSpecItem.nameEn ?: "Default Title (EN)"
            }
            textSize = 14f // Adjust the text size as needed
            setTypeface(null, Typeface.BOLD) // Make it bold to distinguish the title
            setTextColor(ContextCompat.getColor(context, R.color.black)) // Adjust as necessary

            // Set layout parameters for titleTextView with marginStart
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginStart = 24.dpToPx(context) // Set marginStart to 24dp
            }
        }

        // Add the title TextView to the dynamic container
        dynamicContainer.addView(titleTextView)

        // Create a TextView for "Required" status dynamically if the item is required
        if (dynamicSpecItem.isRequired) {
            val requiredTextView = TextView(context).apply {
                text =
                    context.getString(R.string.required_label) // Assume you have a string resource for "Required"
                textSize = 12f
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.red
                    )
                ) // Adjust color to indicate importance
            }
            // Add the "Required" TextView right below the title or wherever appropriate
            dynamicContainer.addView(requiredTextView)
        }

        // Add sub-specifications (TextView and ImageView for each subSpec)
        dynamicSpecItem.subSpecifications?.forEachIndexed { index, subSpec ->
            // Create TextView for each sub-specification with layout_weight="1" and layout_width="0dp"
            val textView = TextView(context).apply {
                text =
                    if (ConstantObjects.currentLanguage == "ar") subSpec.nameAr else subSpec.nameEn
                textSize = 12f
                setTextColor(ContextCompat.getColor(context, R.color.black)) // Adjust as necessary
                layoutParams = LinearLayout.LayoutParams(
                    0, // layout_width = 0dp
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    weight = 1f // layout_weight = 1
                }
            }

            // Create ImageView for each sub-specification
            val imageView = ImageView(context).apply {
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setPadding(
                    10.dpToPx(context),
                    0.dpToPx(context),
                    10.dpToPx(context),
                    0.dpToPx(context)
                )
                layoutParams = params
                setImageResource(if (subSpec.isDataSelected) R.drawable.ic_radio_button_checked else R.drawable.ic_radio_button_unchecked)
                setOnClickListener {
                    // Handle selection logic
                    dynamicSpecItem.subSpecifications?.forEach { it.isDataSelected = false }
                    subSpec.isDataSelected = true

                    // Update the parent item based on the selection
                    dynamicSpecItem.valueBoolean = true // or set it based on the selected subSpec
                    notifyItemChanged(position)

                    onChangeValueListener.setCheckClicked(position)
                }
            }

            // Add the TextView and ImageView to the container
            val itemLayout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(
                    10.dpToPx(context),
                    10.dpToPx(context),
                    10.dpToPx(context),
                    10.dpToPx(context)
                ) // Padding 16dp

                // Set margins using LayoutParams
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(
                    10.dpToPx(context),
                    10.dpToPx(context),
                    10.dpToPx(context),
                    0.dpToPx(context)
                ) // Margin 10dp
                this.layoutParams = layoutParams

                // Add TextView (with weight) and ImageView inside this LinearLayout
                addView(textView)
                addView(imageView)
            }

            // Add the itemLayout to the dynamicContainer
            dynamicContainer.addView(itemLayout)
        }
    }

    private fun setTextBoxView(
        textBoxViewHolder: TextBoxViewHolder,
        position: Int
    ) {
        val dynamicSpecItem = dynamicSpecificationList[position]

        if (dynamicSpecItem.type == numberType) {
            textBoxViewHolder.tvTitleAr.text = dynamicSpecItem.name
            textBoxViewHolder.etValueAr.inputType = InputType.TYPE_CLASS_NUMBER
            textBoxViewHolder.etValueEn.inputType = InputType.TYPE_CLASS_NUMBER
            textBoxViewHolder.tvTitleEn.visibility = View.GONE
            textBoxViewHolder.etValueEn.visibility = View.GONE
            textBoxViewHolder.txtRequiredEn.visibility = View.GONE
        } else {
            textBoxViewHolder.tvTitleAr.text =
                "${dynamicSpecItem.name} ${context.getString(R.string.inArabic)}"
            textBoxViewHolder.tvTitleEn.text =
                "${dynamicSpecItem.name} ${context.getString(R.string.inEnglish)}"
            textBoxViewHolder.tvTitleEn.visibility = View.VISIBLE
            textBoxViewHolder.etValueEn.visibility = View.VISIBLE
        }

        // Set the hint for Arabic and English EditText fields
        textBoxViewHolder.etValueAr.hint =
            "${dynamicSpecItem.name} ${context.getString(R.string.inArabic)}"
        textBoxViewHolder.etValueEn.hint =
            "${dynamicSpecItem.name} ${context.getString(R.string.inEnglish)}"

        // Set text if the item is selected, otherwise set an empty string
        if (dynamicSpecItem.isSelected) {
            textBoxViewHolder.etValueAr.setText(dynamicSpecItem.valueArText ?: "")
            textBoxViewHolder.etValueEn.setText(dynamicSpecItem.valueEnText ?: "")
        } else {
            textBoxViewHolder.etValueAr.setText("")
            textBoxViewHolder.etValueEn.setText("")
        }

        // Handle visibility of required label
        if (dynamicSpecItem.isRequired) {
            textBoxViewHolder.txtRequired.visibility = View.VISIBLE
            if (dynamicSpecItem.type != numberType) {
                textBoxViewHolder.txtRequiredEn.visibility = View.VISIBLE
            } else {
                textBoxViewHolder.txtRequiredEn.visibility = View.GONE
            }
        } else {
            textBoxViewHolder.txtRequired.visibility = View.GONE
            textBoxViewHolder.txtRequiredEn.visibility = View.GONE
        }

        textBoxViewHolder.etValueAr.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                onChangeValueListener.setOnTextBoxTextChangeAR(
                    textBoxViewHolder.etValueAr.text.toString().trim(), position
                )
            }
        })

        textBoxViewHolder.etValueEn.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                onChangeValueListener.setOnTextBoxTextChangeEN(
                    textBoxViewHolder.etValueEn.text.toString().trim(), position
                )
            }
        })
    }

    private lateinit var itemDropDownArrayList: ArrayList<SubSpecificationItem>
    private lateinit var spinnerVisitAdapter: SpinnerVisitAdapter
    private fun setSpinnerView(
        spinnerViewHolder: SpinnerViewHolder,
        position: Int
    ) {
        spinnerViewHolder.tvTitle.text = dynamicSpecificationList[position].name
        itemDropDownArrayList = ArrayList<SubSpecificationItem>()
        dynamicSpecificationList[position].subSpecifications?.let { itemDropDownArrayList.addAll(it) }
        spinnerVisitAdapter = SpinnerVisitAdapter(context, itemDropDownArrayList)
        spinnerViewHolder.spinner.adapter = spinnerVisitAdapter
        if (dynamicSpecificationList[position].isRequired) {
            spinnerViewHolder.txtRequired.visibility = View.VISIBLE
        }

        if (dynamicSpecificationList[position].isSelected) {
            for (i in dynamicSpecificationList[position].subSpecifications!!.indices) {
                if (dynamicSpecificationList[position]?.subSpecifications?.get(i)?.isDataSelected == true) {
                    selectionType(spinnerViewHolder.spinner, i)

                }
            }
        }
        spinnerViewHolder.spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View,
                spinnerPosition: Int,
                l: Long
            ) {
                onChangeValueListener.setOnSpinnerListSelected(position, spinnerPosition)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun selectionType(sp: Spinner, pos: Int) {
        GlobalScope.launch {
            delay(250)
            sp.setSelection(pos, true)

        }
    }

    fun updateAdapter(dynamicSpecificationList: List<DynamicSpecificationItem>) {
        this.dynamicSpecificationList = dynamicSpecificationList
        notifyDataSetChanged()
    }

    interface OnChangeValueListener {
        fun setOnTextBoxTextChangeAR(value: String, position: Int)
        fun setOnTextBoxTextChangeEN(value: String, position: Int)
        fun setCheckClicked(position: Int)
        fun setOnSpinnerListSelected(mainAttributesPosition: Int, spinnerPosition: Int)
    }

}