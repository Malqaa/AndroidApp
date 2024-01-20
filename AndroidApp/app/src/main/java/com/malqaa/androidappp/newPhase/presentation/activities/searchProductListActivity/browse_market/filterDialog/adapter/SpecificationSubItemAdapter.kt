package com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.databinding.ItemFilterSpecificationSubItemBinding
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.SubSpecificationItem
import com.malqaa.androidappp.newPhase.utils.ConstantObjects

class SpecificationSubItemAdapter(
    var subSpecifications: List<SubSpecificationItem>,
    var parentPosition: Int,
    var setOnClickListeners:SetOnClickListeners
) :
    RecyclerView.Adapter<SpecificationSubItemAdapter.SpecificationSubItemViewHolder>(){
    lateinit var context: Context
    class SpecificationSubItemViewHolder(var viewBinding: ItemFilterSpecificationSubItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecificationSubItemViewHolder {
        context = parent.context
        return  SpecificationSubItemViewHolder(
            ItemFilterSpecificationSubItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = subSpecifications.size

    override fun onBindViewHolder(holder: SpecificationSubItemViewHolder, position: Int) {
        if(ConstantObjects.currentLanguage== ConstantObjects.ARABIC) {
            holder.viewBinding.specificationTv.text = subSpecifications[position].nameEn
        }else{
            holder.viewBinding.specificationTv.text = subSpecifications[position].nameAr
        }
        holder.viewBinding.specificationTv.isSelected = subSpecifications[position].isSelected
        holder.viewBinding.specificationTv.setOnClickListener {
            setOnClickListeners.setonClickListeners(parentPosition, childPosition = position)
        }
    }

    interface SetOnClickListeners{
        fun setonClickListeners(parentPosition:Int,childPosition:Int)
    }
}