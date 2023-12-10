package com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.databinding.AtrributeItemBinding
import com.malka.androidappp.newPhase.data.helper.getDrawableCompat
import com.malka.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationSentObject
import com.malka.androidappp.newPhase.domain.models.productResp.ProductSpecialityItemDetails

class SpecificationAdapter(var specificationList: ArrayList<DynamicSpecificationSentObject>) :
    RecyclerView.Adapter<SpecificationAdapter.AttributeViewHolder>(){
  lateinit var context: Context
    class AttributeViewHolder(var viewBinding: AtrributeItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttributeViewHolder {
        context = parent.context
        return  AttributeViewHolder(AtrributeItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = specificationList.size

    override fun onBindViewHolder(holder: AttributeViewHolder, position: Int) {
        holder.viewBinding.keyTv.text=specificationList[position].headerSpe?:""
        holder.viewBinding.valueTv.text=specificationList[position].valueSpe?:""
        var _position=position+1
        if(_position%2==0){
            holder.viewBinding.mainLayout.background=context.getDrawableCompat(R.drawable.product_attribute_bg_gray)
        }else{
            holder.viewBinding.mainLayout.background=context.getDrawableCompat(R.drawable.product_attribute_bg_white)
        }
    }
}