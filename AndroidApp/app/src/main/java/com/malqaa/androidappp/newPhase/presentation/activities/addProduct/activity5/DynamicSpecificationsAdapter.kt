package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity5

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationItem
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.capitalizeFirstLetter

enum class SpecificationType(val type: Int) {
    DropdownList(type = 1),
    ShortText(type = 2),
    LongText(type = 3),
    Number(type = 4),
    Radio(type = 5),
    Checkbox(type = 6),
    MultiSelect(type = 7);

    companion object {
        fun fromType(type: Int): SpecificationType? {
            return values().find { it.type == type }
        }
    }
}

class DynamicSpecificationsAdapter(
    private var dynamicSpecificationList: List<DynamicSpecificationItem>,
    private var onChangeValueListener: OnChangeValueListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Use SpecificationType enum for clarity
    private val spinnerType = SpecificationType.DropdownList.type
    private val shortTextType = SpecificationType.ShortText.type
    private val radioType = SpecificationType.Radio.type
    private val checkboxType = SpecificationType.Checkbox.type

    // View Holders
    inner class TextBoxViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val etArValue: EditText = view.findViewById(R.id.etArValue)
        val etEnValue: EditText = view.findViewById(R.id.etEnValue)
    }

    inner class SpinnerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val spinner: Spinner = view.findViewById(R.id.spinnerValues)
    }

    inner class RadioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val radioGroup: RadioGroup = view.findViewById(R.id.radioGroup)
    }

    inner class CheckboxViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val checkboxGroup: LinearLayout = view.findViewById(R.id.checkboxGroup)
    }

    override fun getItemViewType(position: Int): Int {
        val specType = SpecificationType.values()
            .firstOrNull { it.type == dynamicSpecificationList[position].type }
        return when (specType) {
            SpecificationType.DropdownList -> spinnerType
            SpecificationType.ShortText, SpecificationType.LongText, SpecificationType.Number -> shortTextType
            SpecificationType.Radio -> radioType
            SpecificationType.Checkbox -> checkboxType
            else -> shortTextType // Default to text box
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            spinnerType -> SpinnerViewHolder(
                inflater.inflate(
                    R.layout.item_drop_down,
                    parent,
                    false
                )
            )

            radioType -> RadioViewHolder(inflater.inflate(R.layout.item_radio, parent, false))
            checkboxType -> CheckboxViewHolder(
                inflater.inflate(
                    R.layout.item_checkbox,
                    parent,
                    false
                )
            )

            else -> TextBoxViewHolder(inflater.inflate(R.layout.item_text_box, parent, false))
        }
    }

    override fun getItemCount(): Int = dynamicSpecificationList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val specItem = dynamicSpecificationList[position]

        // Handle isActive: Hide the item if not active
        if (!specItem.isActive) {
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
            return
        } else {
            holder.itemView.visibility = View.VISIBLE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        when (holder) {
            is SpinnerViewHolder -> bindSpinnerView(holder, specItem, position)
            is RadioViewHolder -> bindRadioView(holder, specItem, position)
            is CheckboxViewHolder -> bindCheckboxView(holder, specItem, position)
            is TextBoxViewHolder -> bindTextBoxView(holder, specItem, position)
        }
    }

    private fun bindTextBoxView(
        holder: TextBoxViewHolder,
        specItem: DynamicSpecificationItem,
        position: Int
    ) {
        val context = holder.itemView.context

        val formattedTitle = formatTitleWithAsterisk(specItem, context)
        holder.tvTitle.text = formattedTitle

        val inputType = when (SpecificationType.values().firstOrNull { it.type == specItem.type }) {
            SpecificationType.Number -> InputType.TYPE_CLASS_NUMBER
            SpecificationType.LongText -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            else -> InputType.TYPE_CLASS_TEXT
        }

        if (inputType == InputType.TYPE_CLASS_NUMBER && ConstantObjects.currentLanguage == "ar") {
            holder.etEnValue.visibility = View.GONE
        } else if (inputType == InputType.TYPE_CLASS_NUMBER && ConstantObjects.currentLanguage == "en") {
            holder.etArValue.visibility = View.GONE
        } else {
            holder.etArValue.visibility = View.VISIBLE
            holder.etEnValue.visibility = View.VISIBLE
        }

        // Set input type based on specification type
        holder.etArValue.inputType = inputType
        holder.etEnValue.inputType = inputType

        // Set placeholder
        holder.etArValue.hint =
            "${specItem.name} ${context.getString(R.string.inArabic)}".capitalizeFirstLetter()
        holder.etEnValue.hint =
            "${specItem.name} ${context.getString(R.string.inEnglish)}".capitalizeFirstLetter()

        // Set existing value based on current language
        holder.etArValue.setText(specItem.valueArText)
        holder.etEnValue.setText(specItem.valueEnText)

        // Handle text changes
        holder.etArValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                onChangeValueListener.setOnTextBoxTextChangeAR(text, position)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        holder.etEnValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                onChangeValueListener.setOnTextBoxTextChangeEN(text, position)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun bindSpinnerView(
        holder: SpinnerViewHolder,
        specItem: DynamicSpecificationItem,
        position: Int
    ) {
        val context = holder.itemView.context

        val formattedTitle = formatTitleWithAsterisk(specItem, context)
        holder.tvTitle.text = formattedTitle

        // Prepare spinner data
        val subSpecs = specItem.subSpecifications ?: emptyList()
        val spinnerAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            subSpecs.map {
                if (ConstantObjects.currentLanguage == "ar") it.nameAr ?: "" else it.nameEn ?: ""
            }
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        holder.spinner.adapter = spinnerAdapter

        // Set selection if already selected
        val selectedIndex = subSpecs.indexOfFirst { it.isDataSelected }
        if (selectedIndex >= 0) {
            holder.spinner.setSelection(selectedIndex)
        }

        // Handle selection changes
        holder.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, spinnerPosition: Int, id: Long
            ) {
                onChangeValueListener.setOnSpinnerListSelected(position, spinnerPosition)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun bindRadioView(
        holder: RadioViewHolder,
        specItem: DynamicSpecificationItem,
        position: Int
    ) {
        val context = holder.itemView.context

        val formattedTitle = formatTitleWithAsterisk(specItem, context)
        holder.tvTitle.text = formattedTitle

        holder.radioGroup.removeAllViews()

        specItem.subSpecifications?.forEach { subSpec ->
            if (subSpec.isActive) {
                val radioButton = RadioButton(context).apply {
                    text =
                        if (ConstantObjects.currentLanguage == "ar") subSpec.nameAr else subSpec.nameEn
                    isChecked = subSpec.isDataSelected
                    id = subSpec.id
                    // Set width to match_parent and align text to the left
                    layoutParams = RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.MATCH_PARENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT
                    )
                    // Align the text to the left and the RadioButton to the right
                    layoutDirection = View.LAYOUT_DIRECTION_RTL
                }
                holder.radioGroup.addView(radioButton)
            }
        }

        holder.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            specItem.subSpecifications?.forEach { subSpec ->
                subSpec.isDataSelected = (subSpec.id == checkedId)
            }
            onChangeValueListener.onRadioItemSelected(position, checkedId)
        }
    }

    private fun bindCheckboxView(
        holder: CheckboxViewHolder,
        specItem: DynamicSpecificationItem,
        position: Int
    ) {
        val context = holder.itemView.context

        val formattedTitle = formatTitleWithAsterisk(specItem, context)
        holder.tvTitle.text = formattedTitle

        holder.checkboxGroup.removeAllViews()

        specItem.subSpecifications?.forEach { subSpec ->
            if (subSpec.isActive) {
                val checkBox = CheckBox(context).apply {
                    text =
                        if (ConstantObjects.currentLanguage == "ar") subSpec.nameAr else subSpec.nameEn
                    isChecked = subSpec.isDataSelected
                    id = subSpec.id
                    layoutParams = RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.MATCH_PARENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT
                    )
                    // Align the text to the left and the RadioButton to the right
                    layoutDirection = View.LAYOUT_DIRECTION_RTL
                }
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    subSpec.isDataSelected = isChecked
                    onChangeValueListener.onCheckboxItemChecked(position, checkBox.id, isChecked)
                }
                holder.checkboxGroup.addView(checkBox)
            }
        }
    }

    interface OnChangeValueListener {
        fun setOnTextBoxTextChangeAR(value: String, position: Int)
        fun setOnTextBoxTextChangeEN(value: String, position: Int)
        fun setOnSpinnerListSelected(mainAttributesPosition: Int, spinnerPosition: Int)
        fun onRadioItemSelected(position: Int, selectedId: Int)
        fun onCheckboxItemChecked(position: Int, checkboxId: Int, isChecked: Boolean)
    }

    fun updateAdapter(newList: List<DynamicSpecificationItem>) {
        this.dynamicSpecificationList = newList
        notifyDataSetChanged()
    }
}

fun formatTitleWithAsterisk(specItem: DynamicSpecificationItem, context: Context): SpannableString {
    val orangeColor = ContextCompat.getColor(context, R.color.orange)

    // Set title with asterisk if required and based on current language
    val titleText = if (specItem.isRequired) {
        if (ConstantObjects.currentLanguage == "ar") "${specItem.nameAr} *" else "${specItem.nameEn} *"
    } else {
        if (ConstantObjects.currentLanguage == "ar") specItem.nameAr else specItem.nameEn
    }

    // Format title text with only the first letter capitalized and the rest in lowercase
    val formattedTitleText = titleText?.capitalizeFirstLetter()

    // Create a SpannableString from the formatted text
    val spannableString = SpannableString(formattedTitleText)

    // Apply orange color to asterisk if required
    if (specItem.isRequired) {
        val asteriskIndex = formattedTitleText?.indexOf("*")
        if (asteriskIndex != null) {
            if (asteriskIndex >= 0) {
                spannableString.setSpan(
                    ForegroundColorSpan(orangeColor),
                    asteriskIndex,
                    asteriskIndex + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }

    return spannableString
}