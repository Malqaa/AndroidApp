package com.malqaa.androidappp.newPhase.presentation.dialogsShared.countryDialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.domain.models.countryResp.Country


class CountriesAdapter(
    var countries: List<Country>,
    var onCountrySelected: OnCountrySelected
) :
    RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>() {



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
        return countries.size

    }

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        val countryName:String = countries[position].name
        holder.btnCountry.text = countryName
        holder.btnCountry.setOnClickListener {
            onCountrySelected.onCountrySelected(countries[position].id,countryName,countries[position].countryFlag,countries[position].countryCode)
        }
    }


    class CountriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var btnCountry: TextView = view.findViewById(R.id.btnCity)
    }
    interface OnCountrySelected{
        fun onCountrySelected(id:Int, countryName:String, countryFlag: String?,countryCode:String?)
    }

}