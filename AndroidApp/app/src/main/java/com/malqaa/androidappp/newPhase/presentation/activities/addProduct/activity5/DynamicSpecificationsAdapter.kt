package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity5

import android.content.Context
import android.opengl.Visibility
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationItem
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.SubSpecificationItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class DynamicSpecificationsAdapter(
    private var dynamicSpecificationList: List<DynamicSpecificationItem>,
    private var onChangeValueListener: OnChangeValueListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //====viewHolder
    private val spinnerType = 1 // and 7
    private val textBoxType = 2 // and 3
    private val numberType = 4
    private val radioType = 5
    private val checkType = 6


//    class TextBoxViewHolder(var viewBinding: ItemTextBoxsBinding) :
//        RecyclerView.ViewHolder(viewBinding.root)

    class TextBoxViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitleAr: TextView = view.findViewById(R.id.tvTitleAr)
        val tvTitleEn: TextView = view.findViewById(R.id.tvTitleEn)
        val txtRequired: TextView = view.findViewById(R.id.txtRequired)
        val txtRequiredEn: TextView = view.findViewById(R.id.txtRequiredEn)
        val etValueEn: EditText = view.findViewById(R.id.etValueEn)
        val etValueAr: EditText = view.findViewById(R.id.etValueAr)

    }

//    class SpinnerViewHolder(var viewBinding: ItemDropDownBinding) :
//        RecyclerView.ViewHolder(viewBinding.root)

    class SpinnerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitleAr)
        val spinner: Spinner = view.findViewById(R.id.spinnerValues)
        val txtRequired: TextView = view.findViewById(R.id.txtRequired)
    }

    class CheckViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitleAr: TextView = view.findViewById(R.id.tvTitleAr)
        val ivSelect: ImageView = view.findViewById(R.id.ivSelect)
        val ivSelect2: ImageView = view.findViewById(R.id.ivSelect2)
        val type1: TextView = view.findViewById(R.id.type1)
        val txtRequired: TextView = view.findViewById(R.id.txtRequired)
        val type2: TextView = view.findViewById(R.id.type2)


    }


    lateinit var context: Context
    override fun getItemViewType(position: Int): Int {
        //type 1->dropdownlist  ,
        if (dynamicSpecificationList[position].type == 1|| dynamicSpecificationList[position].type ==7) {
            if (dynamicSpecificationList[position].subSpecifications!!.isNotEmpty()) {
                //dropDown
                return spinnerType
            } else {
                // text box
                return textBoxType
            }
        } else if (dynamicSpecificationList[position].type == 6 ) {
            return checkType
        }
        else if ( dynamicSpecificationList[position].type ==5) {
            return radioType
        }

        else {
            // text box
            return textBoxType

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        if (viewType == spinnerType) {
//            return DynamicSpecificationsAdapter.SpinnerViewHolder(
//                ItemDropDownBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            )
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
        }
        else {
            return TextBoxViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_text_boxs,
                    parent,
                    false
                )
            )
//            return DynamicSpecificationsAdapter.TextBoxViewHolder(
//                ItemTextBoxsBinding
//                    .inflate(LayoutInflater.from(parent.context), parent, false)
        }
    }

    override fun getItemCount(): Int = dynamicSpecificationList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == spinnerType) {
            setSpinnerView(holder as SpinnerViewHolder, position)
        } else if (getItemViewType(position) == checkType) {
            setCheckView(holder as CheckViewHolder, position)
        }else if (getItemViewType(position) == radioType) {
            setCheckView(holder as CheckViewHolder, position)
        } else {
            setTextBoxView(holder as TextBoxViewHolder, position)
        }
    }

    private fun setCheckView(
        checkViewHolder: CheckViewHolder,
        position: Int
    ) {
        if (ConstantObjects.currentLanguage == "ar") {
            checkViewHolder.type1.text= dynamicSpecificationList[position].subSpecifications?.get(0)?.nameAr

            for(i in dynamicSpecificationList[position].subSpecifications!! ){
                checkViewHolder.type2.text=i.nameAr
            }
            checkViewHolder.tvTitleAr.text = "${dynamicSpecificationList[position].nameAr}"

        } else {
            checkViewHolder.type1.text= dynamicSpecificationList[position].subSpecifications?.get(0)?.nameEn

           for(i in dynamicSpecificationList[position].subSpecifications!! ){
               checkViewHolder.type2.text=i.nameEn
           }
            checkViewHolder.tvTitleAr.text = "${dynamicSpecificationList[position].nameEn}"

        }
//        if(dynamicSpecificationList[position].isSelected){
//            if (dynamicSpecificationList[position].valueBoolean) {
//                checkViewHolder.ivSelect.setImageResource(R.drawable.ic_radio_button_checked)
//            } else {
//                checkViewHolder.ivSelect.setImageResource(R.drawable.ic_radio_button_unchecked)
//            }
//        }

        if (dynamicSpecificationList[position].valueBoolean) {
            checkViewHolder.ivSelect.setImageResource(R.drawable.ic_radio_button_checked)
        } else {
            checkViewHolder.ivSelect.setImageResource(R.drawable.ic_radio_button_unchecked)
        }
        checkViewHolder.ivSelect.setOnClickListener {
            if (dynamicSpecificationList[position].valueBoolean) {
                dynamicSpecificationList[position].valueBoolean = false
                checkViewHolder.ivSelect.setImageResource(R.drawable.ic_radio_button_unchecked)
                checkViewHolder.ivSelect2.setImageResource(R.drawable.ic_radio_button_checked)
            } else {
                dynamicSpecificationList[position].valueBoolean = true
                checkViewHolder.ivSelect.setImageResource(R.drawable.ic_radio_button_checked)
                checkViewHolder.ivSelect2.setImageResource(R.drawable.ic_radio_button_unchecked)
            }
            onChangeValueListener.setCheckClicked(position)
        }

        if(dynamicSpecificationList[position].isRequired){
            checkViewHolder.txtRequired.visibility=View.VISIBLE
        }

        checkViewHolder.ivSelect2.setOnClickListener {
            if (dynamicSpecificationList[position].valueBoolean) {
                checkViewHolder.ivSelect.setImageResource(R.drawable.ic_radio_button_checked)
                dynamicSpecificationList[position].valueBoolean = false
                checkViewHolder.ivSelect2.setImageResource(R.drawable.ic_radio_button_unchecked)
            } else {
                dynamicSpecificationList[position].valueBoolean = true
                checkViewHolder.ivSelect.setImageResource(R.drawable.ic_radio_button_unchecked)
                checkViewHolder.ivSelect2.setImageResource(R.drawable.ic_radio_button_checked)
            }
            onChangeValueListener.setCheckClicked(position)
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
            textBoxViewHolder.tvTitleAr.text = "${dynamicSpecItem.name} ${context.getString(R.string.inArabic)}"
            textBoxViewHolder.tvTitleEn.text = "${dynamicSpecItem.name} ${context.getString(R.string.inEnglish)}"
        }

        // Set the hint for Arabic and English EditText fields
        textBoxViewHolder.etValueAr.hint = "${dynamicSpecItem.name} ${context.getString(R.string.inArabic)}"
        textBoxViewHolder.etValueEn.hint = "${dynamicSpecItem.name} ${context.getString(R.string.inEnglish)}"

        // Set text if the item is selected, otherwise set an empty string
        if (dynamicSpecItem.isSelected) {
            textBoxViewHolder.etValueAr.setText(dynamicSpecItem.valueArText ?: "")
            textBoxViewHolder.etValueEn.setText(dynamicSpecItem.valueEnText ?: "")
        } else {
            textBoxViewHolder.etValueAr.setText("")
            textBoxViewHolder.etValueEn.setText("")
        }

        if (dynamicSpecItem.isRequired) {
            textBoxViewHolder.txtRequired.visibility = View.VISIBLE
            if (dynamicSpecItem.type != numberType) {
                textBoxViewHolder.txtRequiredEn.visibility = View.GONE
            }
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
//        val itemDropdownDetails = ItemDropdownDetails(
//            0, 0, attributesList.get(position).getTitle()
//        )
//        itemDropDownArrayList.add(itemDropdownDetails)
        dynamicSpecificationList[position].subSpecifications?.let { itemDropDownArrayList.addAll(it) }
        spinnerVisitAdapter = SpinnerVisitAdapter(context, itemDropDownArrayList)
        spinnerViewHolder.spinner.adapter = spinnerVisitAdapter
        if(dynamicSpecificationList[position].isRequired){
            spinnerViewHolder.txtRequired.visibility=View.VISIBLE
        }

        if( dynamicSpecificationList[position].isSelected){
            for( i in dynamicSpecificationList[position].subSpecifications!!.indices){
                if(dynamicSpecificationList[position]?.subSpecifications?.get(i)?.isDataSelected == true){
                    selectionType(spinnerViewHolder.spinner,i)

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

    private  fun selectionType(sp: Spinner,  pos: Int) {
        GlobalScope.launch {
            delay(250)
            sp.setSelection(pos, true)

        }
    }

    fun updateAdapter(dynamicSpecificationList : List<DynamicSpecificationItem>){
        this.dynamicSpecificationList=dynamicSpecificationList
        notifyDataSetChanged()
    }
    interface OnChangeValueListener {
        fun setOnTextBoxTextChangeAR(value: String, position: Int)
        fun setOnTextBoxTextChangeEN(value: String, position: Int)
        fun setCheckClicked(position: Int)

        //        fun setData(position: Int)
//        fun setOnCheckBoxSelected(value: String?, position: Int)
        fun setOnSpinnerListSelected(mainAttributesPosition: Int, spinnerPosition: Int)
    }

}