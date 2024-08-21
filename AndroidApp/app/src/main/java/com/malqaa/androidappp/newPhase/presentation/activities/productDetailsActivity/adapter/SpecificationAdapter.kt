package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.AtrributeItemBinding
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationSentObject
import com.malqaa.androidappp.newPhase.utils.ConstantObjects

class SpecificationAdapter(private var originalSpecificationList: ArrayList<DynamicSpecificationSentObject>) :
    RecyclerView.Adapter<SpecificationAdapter.AttributeViewHolder>() {

    class AttributeViewHolder(var viewBinding: AtrributeItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttributeViewHolder {
        val viewBinding = AtrributeItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return AttributeViewHolder(viewBinding)
    }

    override fun getItemCount(): Int = filteredSpecificationList.size

    override fun onBindViewHolder(holder: AttributeViewHolder, position: Int) {
        val item = filteredSpecificationList[position]
        val context = holder.itemView.context
        val headerSpeAr = item.HeaderSpeAr
        val headerSpeEn = item.HeaderSpeEn
        val valueSpe = item.valueSpe
        val valueSpeAr = item.ValueSpeAr

        // Language Management
        if (ConstantObjects.currentLanguage == ConstantObjects.ARABIC) {
            holder.viewBinding.keyTv.text = headerSpeAr
            holder.viewBinding.valueTv.text = valueSpe
        } else {
            holder.viewBinding.keyTv.text = headerSpeEn
            holder.viewBinding.valueTv.text = if (valueSpe.isEmpty() && valueSpeAr.isNotEmpty()) {
                valueSpeAr
            } else {
                valueSpe
            }
        }

        // Alternating Backgrounds
        val backgroundDrawable = if (position % 2 == 0) {
            R.drawable.product_attribute_bg_gray
        } else {
            R.drawable.product_attribute_bg_white
        }
        holder.viewBinding.mainLayout.background =
            ContextCompat.getDrawable(context, backgroundDrawable)
    }

    private val filteredSpecificationList: List<DynamicSpecificationSentObject>
        get() = originalSpecificationList.filter {
            it.valueSpe.isNotEmpty() || it.ValueSpeAr.isNotEmpty()
        }
}
