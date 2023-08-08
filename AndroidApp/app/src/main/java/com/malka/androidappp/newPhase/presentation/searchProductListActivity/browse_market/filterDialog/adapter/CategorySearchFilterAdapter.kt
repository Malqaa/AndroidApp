package com.malka.androidappp.newPhase.presentation.searchProductListActivity.browse_market.filterDialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.malka.androidappp.databinding.ItemCategorySearchFilterBinding
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.productResp.CategoriesSearchItem
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category

class CategorySearchFilterAdapter(var categories:List<CategoriesSearchItem>,var setonClickListeners:SetonClickListeners) : RecyclerView.Adapter<CategorySearchFilterAdapter.CategorySearchFilterViewHolder>() {
    class CategorySearchFilterViewHolder(var viewBinding:ItemCategorySearchFilterBinding):ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategorySearchFilterViewHolder {
       return CategorySearchFilterViewHolder(ItemCategorySearchFilterBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int =categories.size
    override fun onBindViewHolder(holder: CategorySearchFilterViewHolder, position: Int) {
        holder.viewBinding.tvCategory.text=categories[position].name
        holder.viewBinding.containerRegion.setOnClickListener {
            if (categories[position].categoryList == null)
                setonClickListeners.onSelectedCategory(position, categories[position].id)
            else {
                if (holder.viewBinding.rvSubCategories.isVisible) {
                    holder.viewBinding.rvSubCategories.hide()
                } else {
                    holder.viewBinding.rvSubCategories.show()
                }
            }
        }
        if (categories[position].categoryList != null) {
            setAdapter(
                holder.viewBinding.rvSubCategories,
                categories[position].categoryList!!,
                position
            )
        }

    }

    private fun setAdapter(
        rvSubCategories: RecyclerView,
        categoryList: List<CategoriesSearchItem>,
        position: Int
    ) {
//       var myAdapter= SubCategorySearchFilterAdapter(categoryList)
//        rvSubCategories.apply {
//            adapter=myAdapter
//            layoutManager=linearLayoutManager(RecyclerView.VERTICAL)
//        }
    }

    interface SetonClickListeners{
        fun onSelectedCategory(position:Int,categoryId:Int)
    }
}