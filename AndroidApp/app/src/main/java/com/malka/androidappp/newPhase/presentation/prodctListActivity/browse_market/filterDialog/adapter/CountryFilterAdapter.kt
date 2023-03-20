package com.malka.androidappp.newPhase.presentation.prodctListActivity.browse_market.filterDialog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.databinding.ItemCountryCityInFilterBinding
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager

class CountryFilterAdapter  :
RecyclerView.Adapter<CountryFilterAdapter.CountryFilterViewHolder>(){
    lateinit var context: Context
    class CountryFilterViewHolder(var viewBinding: ItemCountryCityInFilterBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryFilterViewHolder {
        context = parent.context
        return  CountryFilterViewHolder(
            ItemCountryCityInFilterBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: CountryFilterViewHolder, position: Int) {
        setupCityAdapter(holder.viewBinding.rvCities)
    }
    private fun setupCityAdapter( rvCities: RecyclerView) {
        val cityAdapter= CityFilterAdapter()
        rvCities.apply {
            adapter=cityAdapter
            layoutManager=linearLayoutManager(RecyclerView.VERTICAL)
            isNestedScrollingEnabled=true
        }
    }
}