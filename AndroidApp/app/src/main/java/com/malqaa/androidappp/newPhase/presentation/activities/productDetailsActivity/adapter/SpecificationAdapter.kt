package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.AtrributeItemBinding
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationSentObject
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.getDrawableCompat

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
        val headerSpeAr = specificationList[position].HeaderSpeAr
        val headerSpeEn = specificationList[position].HeaderSpeEn
        val valueSpe = specificationList[position].valueSpe
        var _position = position + 1

        // Visibility Management
        if (valueSpe.isEmpty() || valueSpe.isBlank()) {
            holder.viewBinding.keyTv.visibility = View.GONE
            holder.viewBinding.valueTv.visibility = View.GONE
            _position -= 1
        } else {
            holder.viewBinding.keyTv.visibility = View.VISIBLE
            holder.viewBinding.valueTv.visibility = View.VISIBLE
        }

        // Language Management
        if (ConstantObjects.currentLanguage == ConstantObjects.ARABIC) {
            holder.viewBinding.keyTv.text = headerSpeAr
            holder.viewBinding.valueTv.text = valueSpe
        } else {
            holder.viewBinding.keyTv.text = headerSpeEn
            holder.viewBinding.valueTv.text = valueSpe
        }

        // Alternating Backgrounds
        if (_position % 2 == 0) {
            holder.viewBinding.mainLayout.background =
                context.getDrawableCompat(R.drawable.product_attribute_bg_gray)
        } else {
            holder.viewBinding.mainLayout.background =
                context.getDrawableCompat(R.drawable.product_attribute_bg_white)
        }
    }

}