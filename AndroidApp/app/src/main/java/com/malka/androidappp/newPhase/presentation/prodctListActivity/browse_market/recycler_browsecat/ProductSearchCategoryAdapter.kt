package com.malka.androidappp.newPhase.presentation.prodctListActivity.browse_market.recycler_browsecat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.databinding.ItemQuestionAnswerDesignBinding
import com.malka.androidappp.databinding.ProductItemBinding
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter.QuestionAnswerAdapter

class ProductSearchCategoryAdapter :
    RecyclerView.Adapter<ProductSearchCategoryAdapter.ProductSearchCategoryViewHolder>(){
    lateinit var context: Context
    class ProductSearchCategoryViewHolder(var viewBinding: ProductItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSearchCategoryViewHolder {
        context = parent.context
        return  ProductSearchCategoryViewHolder(
            ProductItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: ProductSearchCategoryViewHolder, position: Int) {

    }
}