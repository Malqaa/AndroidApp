package com.malka.androidappp.newPhase.presentation.searchProductListActivity.browse_market.filterDialog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.databinding.ItemCountryCityInFilterBinding
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.countryResp.Country
import com.malka.androidappp.newPhase.domain.models.regionsResp.Region

class CountryFilterAdapter(
    var mainCountriesList: ArrayList<Country>,
    var setonClickListeners: SetonClickListeners
) :
    RecyclerView.Adapter<CountryFilterAdapter.CountryFilterViewHolder>() {
    lateinit var context: Context

    class CountryFilterViewHolder(var viewBinding: ItemCountryCityInFilterBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryFilterViewHolder {
        context = parent.context
        return CountryFilterViewHolder(
            ItemCountryCityInFilterBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = mainCountriesList.size

    override fun onBindViewHolder(holder: CountryFilterViewHolder, position: Int) {
        holder.viewBinding.tvCountry.text = mainCountriesList[position].name
        holder.viewBinding.containerRegion.setOnClickListener {
            if (mainCountriesList[position].regionsList == null)
                setonClickListeners.onSelectedCountry(position, mainCountriesList[position].id)
            else {
                if (holder.viewBinding.rvCities.isVisible) {
                    holder.viewBinding.rvCities.hide()
                } else {
                    holder.viewBinding.rvCities.show()
                }
            }
        }
        if (mainCountriesList[position].regionsList != null) {
            setupCityAdapter(
                holder.viewBinding.rvCities,
                mainCountriesList[position].regionsList!!,
                position
            )
        }

        //
    }

    private fun setupCityAdapter(
        rvCities: RecyclerView,
        regions: List<Region>,
        positionCountry: Int
    ) {
        val cityAdapter = CityFilterAdapter(
            regions,
            positionCountry,
            object : CityFilterAdapter.SetOnselectedListerner {
                override fun setOnSelectCitiy(positionCity: Int, cityId: Int) {
                    setonClickListeners.onSelectedCity(positionCountry, positionCity, cityId)
                }

                override fun setOnSaveCountryToQuery(countryPosition: Int, cityPosition: Int) {
                    setonClickListeners.setOnSaveCountryToQuery(countryPosition, cityPosition)
                }

                override fun setOnSaveCityToQuery(
                    countryPosition: Int,
                    cityPostion: Int,
                    mainNeighborhoodPosition: Int
                ) {
                    setonClickListeners.setOnSaveCityToQuery(
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
                    setonClickListeners.setOnSaveNeighborhoodToQuery(
                        countryPosition,
                        cityPostion,
                        mainNeighborhoodPosition
                    )
                }

            })
        rvCities.apply {
            adapter = cityAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            isNestedScrollingEnabled = true
        }
    }

    interface SetonClickListeners {
        fun onSelectedCountry(position: Int, countryId: Int)
        fun onSelectedCity(positionCounty: Int, positionCity: Int, cityId: Int)
        fun setOnSaveCountryToQuery(countryPosition: Int, cityPosition: Int)
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