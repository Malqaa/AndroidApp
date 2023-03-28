package com.malka.androidappp.newPhase.presentation.addProduct.activity5

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationItem
import com.malka.androidappp.newPhase.domain.models.dynamicSpecification.SubSpecificationItem


class DynamicSpecificationsAdapter(
    var dynamicSpecificationList: List<DynamicSpecificationItem>,
    var onChangeValueListener:OnChangeValueListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //====viewHolder
    val textBoxType = 2
    val spinnerType = 1

//    class TextBoxViewHolder(var viewBinding: ItemTextBoxsBinding) :
//        RecyclerView.ViewHolder(viewBinding.root)

    class TextBoxViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitleAr)
        val etValue: EditText = view.findViewById(R.id.etValue)
    }

//    class SpinnerViewHolder(var viewBinding: ItemDropDownBinding) :
//        RecyclerView.ViewHolder(viewBinding.root)

    class SpinnerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitleAr)
        val spinner: Spinner = view.findViewById(R.id.spinnerValues)
    }


    lateinit var context: Context
    override fun getItemViewType(position: Int): Int {
        if (dynamicSpecificationList[position].subSpecifications != null) {
            if(dynamicSpecificationList[position].subSpecifications!!.isNotEmpty()){
                //dropDown
                return spinnerType
            }else{
                // text box
                return textBoxType
            }
        } else {
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
        } else {
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
        } else {
            setTextBoxView(holder as TextBoxViewHolder, position)
        }
    }

    private fun setTextBoxView(
        textBoxViewHolder: TextBoxViewHolder,
        position: Int
    ) {
        textBoxViewHolder.tvTitle.text = dynamicSpecificationList[position].name
        //textBoxHolder.etValue.setHint(attributesList.get(position).getTitle());
        //if (attributesList.get(position).getTextboxDatatype() != 4)
        textBoxViewHolder.etValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                onChangeValueListener.setOnTextBoxTextChange(
                    textBoxViewHolder.etValue.text.toString().trim(), position
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

    interface OnChangeValueListener {
        fun setOnTextBoxTextChange(value: String, position: Int)
//        fun setData(position: Int)
//        fun setOnCheckBoxSelected(value: String?, position: Int)
        fun setOnSpinnerListSelected(mainAttributesPosition: Int, spinnerPosition: Int)
    }

}