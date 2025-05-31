package com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.databinding.ItemFilterSpecificationBinding
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationItem
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.SubSpecificationItem
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show

class SpecificationFilterAdapter(
    var dynamicSpecificationsArrayList: ArrayList<DynamicSpecificationItem>,
    var onChangeValueListener: OnChangeValueListener
) :
    RecyclerView.Adapter<SpecificationFilterAdapter.SpecificationFilterViewHolder>() {
    lateinit var context: Context

    class SpecificationFilterViewHolder(var viewBinding: ItemFilterSpecificationBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SpecificationFilterViewHolder {
        context = parent.context
        return SpecificationFilterViewHolder(
            ItemFilterSpecificationBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = dynamicSpecificationsArrayList.size

    override fun onBindViewHolder(
        holder: SpecificationFilterViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.viewBinding.headerTitle.text = dynamicSpecificationsArrayList[position].name
        if (dynamicSpecificationsArrayList[position].subSpecifications != null
            && dynamicSpecificationsArrayList[position].subSpecifications!!.isNotEmpty()
        ) {
            holder.viewBinding.subItemRcv.show()
            holder.viewBinding.mainContainerText.hide()
        } else {
            holder.viewBinding.subItemRcv.hide()
            holder.viewBinding.mainContainerText.show()
            if (dynamicSpecificationsArrayList[position].filterValue != "") {
                holder.viewBinding.etValue.setText(dynamicSpecificationsArrayList[position].filterValue)
            } else {
                holder.viewBinding.etValue.text = null
            }
        }
        holder.viewBinding.etValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                onChangeValueListener.setOnTextBoxTextChange(
                    holder.viewBinding.etValue.text.toString().trim(), position
                )
            }
        })

        if (dynamicSpecificationsArrayList[position].subSpecifications != null) {
            setAdapterForSpecificationSubItem(
                holder.viewBinding.subItemRcv,
                dynamicSpecificationsArrayList[position].subSpecifications!!,
                position
            )
        } else {
            setAdapterForSpecificationSubItem(
                holder.viewBinding.subItemRcv,
                ArrayList(),
                position
            )
        }
    }

    private fun setAdapterForSpecificationSubItem(
        subItemRcv: RecyclerView,
        subSpecifications: List<SubSpecificationItem>,
        parentPosition: Int
    ) {
        val specificationSubItemAdapter = SpecificationSubItemAdapter(
            subSpecifications,
            parentPosition,
            object : SpecificationSubItemAdapter.SetOnClickListeners {
                override fun setonClickListeners(parentPosition: Int, childPosition: Int) {
                    onChangeValueListener.setOnSelectedSpecificationItemFromList(
                        parentPosition,
                        childPosition
                    )
                }

            })
        subItemRcv.apply {
            adapter = specificationSubItemAdapter
            layoutManager = linearLayoutManager(RecyclerView.HORIZONTAL)
            isNestedScrollingEnabled = true
        }
    }

    interface OnChangeValueListener {
        fun setOnTextBoxTextChange(value: String, position: Int)

        //        fun setData(position: Int)
        fun setOnSelectedSpecificationItemFromList(parentPosition: Int, childPosition: Int)
    }
}