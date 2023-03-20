package com.malka.androidappp.newPhase.presentation.prodctListActivity.browse_market.filterDialog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.databinding.ItemFilterRegionBinding

class RegionFilterAdapter  :
    RecyclerView.Adapter<RegionFilterAdapter.RegionFilterViewHolder>(){
    lateinit var context: Context
    class  RegionFilterViewHolder(var viewBinding: ItemFilterRegionBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionFilterViewHolder {
        context = parent.context
        return  RegionFilterViewHolder(
            ItemFilterRegionBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: RegionFilterViewHolder, position: Int) {

    }
}