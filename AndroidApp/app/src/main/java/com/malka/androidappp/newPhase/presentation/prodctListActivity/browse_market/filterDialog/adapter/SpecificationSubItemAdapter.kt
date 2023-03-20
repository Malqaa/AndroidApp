package com.malka.androidappp.newPhase.presentation.prodctListActivity.browse_market.filterDialog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.databinding.ItemFilterSpecificationSubItemBinding
import com.malka.androidappp.databinding.ItemQuestionAnswerDesignBinding
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter.QuestionAnswerAdapter

class SpecificationSubItemAdapter :
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

    override fun getItemCount(): Int = 4

    override fun onBindViewHolder(holder: SpecificationSubItemViewHolder, position: Int) {

    }
}