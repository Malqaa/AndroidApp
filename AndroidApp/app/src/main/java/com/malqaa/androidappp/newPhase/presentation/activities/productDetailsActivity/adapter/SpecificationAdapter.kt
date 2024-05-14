package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.AtrributeItemBinding
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.getDrawableCompat
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationSentObject

class SpecificationAdapter(var specificationList: ArrayList<DynamicSpecificationSentObject>) :
    RecyclerView.Adapter<SpecificationAdapter.AttributeViewHolder>() {
    lateinit var context: Context

    class AttributeViewHolder(var viewBinding: AtrributeItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttributeViewHolder {
        context = parent.context
        return AttributeViewHolder(
            AtrributeItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = specificationList.size

    override fun onBindViewHolder(holder: AttributeViewHolder, position: Int) {
        if (ConstantObjects.currentLanguage == ConstantObjects.ARABIC) {
            holder.viewBinding.keyTv.text = specificationList[position].HeaderSpeAr
            holder.viewBinding.valueTv.text = specificationList[position].valueSpe
        }else{
            holder.viewBinding.keyTv.text = specificationList[position].HeaderSpeEn
            holder.viewBinding.valueTv.text = specificationList[position].valueSpe
        }
        val _position = position + 1
        if (_position % 2 == 0) {
            holder.viewBinding.mainLayout.background =
                context.getDrawableCompat(R.drawable.product_attribute_bg_gray)
        } else {
            holder.viewBinding.mainLayout.background =
                context.getDrawableCompat(R.drawable.product_attribute_bg_white)
        }
    }
}