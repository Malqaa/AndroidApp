package com.malka.androidappp.newPhase.presentation.dialogsShared.countryDialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.domain.models.countryResp.Country


class CountriesAdapter(
    var context: Context,
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
        var countryName:String = countries[position].name
        holder.btnCountry.text = countryName
        holder.btnCountry.setOnClickListener {
            onCountrySelected.onCountrySelected(countries[position].id,countryName,countries[position].countryFlag,countries[position].countryCode)
        }
    }


    class CountriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var btnCountry: TextView = view.findViewById(R.id.btnCity)
    }
    interface OnCountrySelected{
        public fun onCountrySelected(id:Int, countryName:String, countryFlag: String?,countryCode:String?)
    }

}