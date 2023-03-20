package com.malka.androidappp.newPhase.presentation.prodctListActivity.browse_market.filterDialog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.databinding.ItemFilterSpecificationBinding
import com.malka.androidappp.databinding.ItemQuestionAnswerDesignBinding
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter.QuestionAnswerAdapter

class SpecificationFilterAdapter :
    RecyclerView.Adapter<SpecificationFilterAdapter.SpecificationFilterViewHolder>(){
    lateinit var context: Context
    class  SpecificationFilterViewHolder(var viewBinding: ItemFilterSpecificationBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecificationFilterViewHolder {
        context = parent.context
        return  SpecificationFilterViewHolder(
            ItemFilterSpecificationBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: SpecificationFilterViewHolder, position: Int) {
    setAdapterForSpecificationSubItem(holder.viewBinding.subItemRcv)
    }

    private fun setAdapterForSpecificationSubItem(subItemRcv: RecyclerView) {
  val specificationSubItemAdapter=SpecificationSubItemAdapter()
        subItemRcv.apply {
            adapter=specificationSubItemAdapter
            layoutManager=linearLayoutManager(RecyclerView.HORIZONTAL)
        }
    }
}