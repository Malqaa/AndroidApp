package com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.databinding.AtrributeItemBinding

class AttributeAdapter  :
    RecyclerView.Adapter<AttributeAdapter.AttributeViewHolder>(){
  lateinit var context: Context
    class AttributeViewHolder(var viewBinding: AtrributeItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttributeViewHolder {
        context = parent.context
        return  AttributeViewHolder(AtrributeItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: AttributeViewHolder, position: Int) {
        holder.viewBinding.keyTv.text="brand"
        holder.viewBinding.valueTv.text="value"
    }
}