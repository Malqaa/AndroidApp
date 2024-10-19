package com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemFilterRegionBinding
import com.malqaa.androidappp.newPhase.domain.models.regionsResp.Region
import com.malqaa.androidappp.newPhase.utils.hide

class RegionFilterAdapter(
    var mainNeighborhoodList: List<Region>,
    var countryPositon: Int,
    var citiyPosition: Int,
    var setOnselectedListerner: SetOnselectedListerner
) :
    RecyclerView.Adapter<RegionFilterAdapter.RegionFilterViewHolder>() {
    lateinit var context: Context

    class RegionFilterViewHolder(var viewBinding: ItemFilterRegionBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionFilterViewHolder {
        context = parent.context
        return RegionFilterViewHolder(
            ItemFilterRegionBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = mainNeighborhoodList.size

    override fun onBindViewHolder(holder: RegionFilterViewHolder, position: Int) {
        holder.viewBinding.iVExpandIcon.hide()
        holder.viewBinding.tvRegionName.text = mainNeighborhoodList[position].name
        if (mainNeighborhoodList[position].isSelected) {
            holder.viewBinding.cbCity.setImageResource(R.drawable.checkbox_selected)
        } else {
            holder.viewBinding.cbCity.setImageResource(R.drawable.checkbox_un_selected)
        }

        holder.viewBinding.cbCity.setOnClickListener {
            if (position == 0) {
                setOnselectedListerner.setOnSaveCityToQuery(countryPositon, citiyPosition, position)
            } else {
                setOnselectedListerner.setOnSaveNeighborhoodToQuery(
                    countryPositon,
                    citiyPosition,
                    position
                )
            }

        }
    }

    interface SetOnselectedListerner {
        fun setOnSaveCityToQuery(
            countryPosition: Int,
            cityPostion: Int,
            mainNeighborhoodPosition: Int
        )

        fun setOnSaveNeighborhoodToQuery(
            countryPosition: Int,
            cityPostion: Int,
            mainNeighborhoodPosition: Int
        )
    }
}