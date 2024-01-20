package com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R


import com.malqaa.androidappp.databinding.ItemSelectCityOrRegionBinding
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.domain.models.regionsResp.Region

class CityFilterAdapter(
    var regions: List<Region>,
    var countryPosition: Int,
    var setOnselectedListerner: SetOnselectedListerner
) :
    RecyclerView.Adapter<CityFilterAdapter.CityFilterViewHolder>() {
    lateinit var context: Context

    class CityFilterViewHolder(var viewBinding: ItemSelectCityOrRegionBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityFilterViewHolder {
        context = parent.context
        return CityFilterViewHolder(
            ItemSelectCityOrRegionBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = regions.size

    override fun onBindViewHolder(holder: CityFilterViewHolder, position: Int) {
        holder.viewBinding.tvCityName.text = regions[position].name
        if (position == 0) {
            holder.viewBinding.cbCity.show()
            holder.viewBinding.iVExpandIcon.hide()
        } else {
            holder.viewBinding.cbCity.hide()
            holder.viewBinding.iVExpandIcon.show()
        }
        if (regions[position].isSelected) {
            holder.viewBinding.cbCity.setImageResource(R.drawable.checkbox_selected)
        } else {
            holder.viewBinding.cbCity.setImageResource(R.drawable.checkbox_un_selected)
        }

        holder.viewBinding.cbCity.setOnClickListener {
            setOnselectedListerner.setOnSaveCountryToQuery(countryPosition, position)
        }
        holder.viewBinding.ContainerCity.setOnClickListener {
            if (position != 0) {
                if (regions[position].mainNeighborhoodList != null) {
                    if (holder.viewBinding.rvRegions.isVisible) {
                        holder.viewBinding.rvRegions.hide()
                    } else {
                        holder.viewBinding.rvRegions.show()
                    }
                } else {
                    setOnselectedListerner.setOnSelectCitiy(position, regions[position].id)
                }
            }
        }
        if (regions[position].mainNeighborhoodList != null) {
            setupRegionAdapter(
                holder.viewBinding.rvRegions,
                regions[position].mainNeighborhoodList!!,
                position
            )
        }
        //
    }

    private fun setupRegionAdapter(
        rvRegions: RecyclerView,
        mainNeighborhoodList: List<Region>,
        cityPositon: Int
    ) {
        val regionFilterAdapter = RegionFilterAdapter(
            mainNeighborhoodList,
            countryPosition,
            cityPositon,
            object : RegionFilterAdapter.SetOnselectedListerner {
                override fun setOnSaveCityToQuery(
                    countryPosition: Int,
                    cityPostion: Int,
                    mainNeighborhoodPosition: Int
                ) {
                    setOnselectedListerner.setOnSaveCityToQuery(
                        countryPosition,
                        cityPostion,
                        mainNeighborhoodPosition
                    )
                }

                override fun setOnSaveNeighborhoodToQuery(
                    countryPosition: Int,
                    cityPostion: Int,
                    mainNeighborhoodPosition: Int
                ) {
                    setOnselectedListerner.setOnSaveNeighborhoodToQuery(
                        countryPosition,
                        cityPostion,
                        mainNeighborhoodPosition
                    )
                }

            })
        rvRegions.apply {
            adapter = regionFilterAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    interface SetOnselectedListerner {
        fun setOnSelectCitiy(positionCity: Int, cityId: Int)
        fun setOnSaveCountryToQuery(countryPosition: Int, cityPostion: Int)
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