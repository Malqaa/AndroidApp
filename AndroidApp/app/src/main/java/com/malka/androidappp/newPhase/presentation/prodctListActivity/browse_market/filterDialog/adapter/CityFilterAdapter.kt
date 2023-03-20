package com.malka.androidappp.newPhase.presentation.prodctListActivity.browse_market.filterDialog.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView



import com.malka.androidappp.databinding.ItemSelectCityOrRegionBinding
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager

class CityFilterAdapter : RecyclerView.Adapter<CityFilterAdapter.CityFilterViewHolder>(){
    lateinit var context: Context

    class CityFilterViewHolder(var viewBinding: ItemSelectCityOrRegionBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityFilterViewHolder {
        context = parent.context
        return  CityFilterViewHolder(
            ItemSelectCityOrRegionBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = 2

    override fun onBindViewHolder(holder: CityFilterViewHolder, position: Int) {
        setupRegionAdapter(holder.viewBinding.rvRegions)
    }

    private fun setupRegionAdapter(rvRegions: RecyclerView) {
        val regionFilterAdapter= RegionFilterAdapter()
        rvRegions.apply {
            adapter=regionFilterAdapter
            layoutManager=linearLayoutManager(RecyclerView.VERTICAL)
        }
    }


}