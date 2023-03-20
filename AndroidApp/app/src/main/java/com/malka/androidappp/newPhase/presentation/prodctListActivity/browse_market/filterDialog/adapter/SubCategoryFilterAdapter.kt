package com.malka.androidappp.newPhase.presentation.prodctListActivity.browse_market.filterDialog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.databinding.ItemFiltterSubCategoryDesignBinding
import com.malka.androidappp.databinding.ItemQuestionAnswerDesignBinding
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter.QuestionAnswerAdapter

class SubCategoryFilterAdapter  :
    RecyclerView.Adapter<SubCategoryFilterAdapter.SubCategoryFilterViewHolder>(){
    lateinit var context: Context
    class SubCategoryFilterViewHolder(var viewBinding: ItemFiltterSubCategoryDesignBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryFilterViewHolder {
        context = parent.context
        return  SubCategoryFilterViewHolder(
            ItemFiltterSubCategoryDesignBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: SubCategoryFilterViewHolder, position: Int) {

    }
}