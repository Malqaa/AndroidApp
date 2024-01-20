package com.malqaa.androidappp.newPhase.presentation.dialogsShared.regionDialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.domain.models.regionsResp.Region


class RegionAdapter(
    var context: Context,
    private var regionsList: List<Region>,
    private var onRegionSelected: OnRegionSelected
) :
    RecyclerView.Adapter<RegionAdapter.CountriesViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
        return CountriesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_country,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return regionsList.size

    }

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        val regionName:String = regionsList[position].name
        holder.btnCountry.text = regionName
        holder.btnCountry.setOnClickListener {
            onRegionSelected.onRegionSelected(regionsList[position].id,regionName)
        }
    }


    class CountriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var btnCountry: TextView = view.findViewById(R.id.btnCity)
    }
    interface OnRegionSelected{
        fun onRegionSelected(id:Int, countryName:String)
    }

}