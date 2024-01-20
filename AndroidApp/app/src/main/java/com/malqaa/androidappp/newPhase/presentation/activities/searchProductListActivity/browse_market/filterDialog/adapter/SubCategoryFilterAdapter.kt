package com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemFiltterSubCategoryDesignBinding
import com.malqaa.androidappp.newPhase.domain.models.productResp.CategoriesSearchItem

class SubCategoryFilterAdapter(var categoryList: ArrayList<CategoriesSearchItem>, var setOnSubCategorySelectListents:SetOnSubCategorySelectListents) :
    RecyclerView.Adapter<SubCategoryFilterAdapter.SubCategoryFilterViewHolder>() {
    lateinit var context: Context

    class SubCategoryFilterViewHolder(var viewBinding: ItemFiltterSubCategoryDesignBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryFilterViewHolder {
        context = parent.context
        return SubCategoryFilterViewHolder(
            ItemFiltterSubCategoryDesignBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = categoryList.size

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(
        holder: SubCategoryFilterViewHolder,
        position: Int
    ) {
        holder.viewBinding.categoryTv.text = categoryList[position].name
        holder.viewBinding.cbCity.isSelected = categoryList[position].isSelected

        if (categoryList[position].isSelected) {
            holder.viewBinding.cbCity.setImageResource(R.drawable.checkbox_selected)
        } else {
            holder.viewBinding.cbCity.setImageResource(R.drawable.checkbox_un_selected)
        }

        holder.viewBinding.cbCity.setOnClickListener {
            setOnSubCategorySelectListents.onSubCategorySelected( position)
        }
    }

    interface SetOnSubCategorySelectListents{
        fun onSubCategorySelected(position: Int)
    }
}