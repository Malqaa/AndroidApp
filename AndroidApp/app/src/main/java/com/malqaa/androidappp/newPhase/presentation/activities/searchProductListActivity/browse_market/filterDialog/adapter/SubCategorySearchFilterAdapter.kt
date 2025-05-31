package com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemSubCategorySearchFilterBinding
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category


class SubCategorySearchFilterAdapter(
    var categoryList: List<Category>,
    var setOnselectedListerner: SetOnselectedListerner
) :
    RecyclerView.Adapter<SubCategorySearchFilterAdapter.CategorySearchFilterViewHolder>() {
    //var categoryList: List<Category>
    class CategorySearchFilterViewHolder(var viewBinding: ItemSubCategorySearchFilterBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategorySearchFilterViewHolder {
        return CategorySearchFilterViewHolder(
            ItemSubCategorySearchFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = categoryList.size
    override fun onBindViewHolder(holder: CategorySearchFilterViewHolder, position: Int) {
        holder.viewBinding.tvSubCategory.text = categoryList[position].name
        if (categoryList[position].isSelected) {
            holder.viewBinding.ivSelectCategory.setImageResource(R.drawable.checkbox_selected)
        } else {
            holder.viewBinding.ivSelectCategory.setImageResource(R.drawable.checkbox_un_selected)
        }
        holder.viewBinding.ivSelectCategory.setOnClickListener {
            setOnselectedListerner.setOnSelectSubCategories(position, categoryList[position].id)
        }

    }

    interface SetOnselectedListerner {
        fun setOnSelectSubCategories(position: Int, subCategoryId: Int)
    }
}