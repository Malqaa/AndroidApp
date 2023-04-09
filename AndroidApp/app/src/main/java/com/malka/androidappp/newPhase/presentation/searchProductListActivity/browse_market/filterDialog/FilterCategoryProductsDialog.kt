package com.malka.androidappp.newPhase.presentation.searchProductListActivity.browse_market.filterDialog

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseDialog
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.categoryResp.CategoriesResp
import com.malka.androidappp.newPhase.domain.models.countryResp.CountriesResp
import com.malka.androidappp.newPhase.domain.models.countryResp.Country
import com.malka.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationItem
import com.malka.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationResp
import com.malka.androidappp.newPhase.domain.models.regionsResp.Region
import com.malka.androidappp.newPhase.domain.models.regionsResp.RegionsResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category
import com.malka.androidappp.newPhase.presentation.searchProductListActivity.browse_market.filterDialog.adapter.CountryFilterAdapter
import com.malka.androidappp.newPhase.presentation.searchProductListActivity.browse_market.filterDialog.adapter.SpecificationFilterAdapter
import com.malka.androidappp.newPhase.presentation.searchProductListActivity.browse_market.filterDialog.adapter.SubCategoryFilterAdapter
import kotlinx.android.synthetic.main.dialog_filter_category_products.*
import kotlinx.android.synthetic.main.dialog_filter_category_products.progressBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilterCategoryProductsDialog(
    context: Context,
    var selectionType: Int,
    var mianCategoryId: Int,
    var setOnClickListeners: SetOnClickListeners
) : BaseDialog(context), CountryFilterAdapter.SetonClickListeners,
    SpecificationFilterAdapter.OnChangeValueListener {

    private var dynamicSpecificationCallback: Call<DynamicSpecificationResp>? = null
    private var neighborhoodsCallback: Call<RegionsResp>? = null
    private var regionsCallback: Call<RegionsResp>? = null
    private var subCagtegoryCallback: Call<CategoriesResp>? = null
    lateinit var countryFilterAdapter: CountryFilterAdapter
    lateinit var specificationFilterAdapter: SpecificationFilterAdapter
    lateinit var subCategoryAdaper: SubCategoryFilterAdapter
    lateinit var categoryList: ArrayList<Category>

    //========
    lateinit var mainCountriesList: ArrayList<Country>
    lateinit var countryResp: CountriesResp
    var countriesCallback: Call<CountriesResp>? = null

    var lastSelectedCountryPosition = 0
    var lastSelectedCityPosition = 0
    var countryIdsList: ArrayList<Int> = ArrayList()
    var cityIdsList: ArrayList<Int> = ArrayList()
    var neiberhoodIdsList: ArrayList<Int> = ArrayList()
    var subCategoryIdsList: ArrayList<Int> = ArrayList()
    var dynamicSpecificationsArrayList: ArrayList<DynamicSpecificationItem> = ArrayList()
    override fun getViewId(): Int = R.layout.dialog_filter_category_products

    companion object { // 1 for region ,2 for sub category  ,3 for specification
        const val regionType = 1
        const val subCategoryType = 2
        const val specificationType = 3
    }

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = true
    override fun isLoadingDialog(): Boolean = false
    override fun initialization() {
        setupCountryAdapter()
        setupSpecificationAdapter()
        setupSubCategoryAdapetr()
        restViewForTargetFilter(selectionType)
        when (selectionType) {
            regionType -> {
                btnRegion.performClick()
            }
            subCategoryType -> {
                btnSubCategory.performClick()
            }
            specificationType -> {
                btnSpecification.performClick()
            }
        }
        setClickListeners()

    }

    fun setSelectedTap(selectedOne: Int) {
        selectionType = selectedOne
        when (selectionType) {
            regionType -> {
                btnRegion.performClick()
            }
            subCategoryType -> {
                btnSubCategory.performClick()
            }
            specificationType -> {
                btnSpecification.performClick()
            }
        }
    }

    private fun setupSubCategoryAdapetr() {
        categoryList = ArrayList()
        subCategoryAdaper = SubCategoryFilterAdapter(categoryList,
            object : SubCategoryFilterAdapter.SetOnSubCategorySelectListents {
                override fun onSubCategorySelected(position: Int) {
                    if (categoryList[position].isSelected) {
                        if (subCategoryIdsList.contains(categoryList[position].id)) {
                            subCategoryIdsList.remove(categoryList[position].id)
                        }
                    } else {
                        if (!subCategoryIdsList.contains(categoryList[position].id)) {
                            subCategoryIdsList.add(categoryList[position].id)
                        }
                    }
                    categoryList[position].isSelected = !categoryList[position].isSelected
                    updateCategoryAdapter()
                    // println("hhhh subCategories "+Gson().toJson(subCategoryIdsList))
                }

            })
        rv_sub_category.apply {
            adapter = subCategoryAdaper
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    private fun updateCategoryAdapter() {
        subCategoryAdaper.notifyDataSetChanged()
    }


    private fun setupCountryAdapter() {
        mainCountriesList = ArrayList()
        countryFilterAdapter = CountryFilterAdapter(mainCountriesList, this)
        rv_region.apply {
            adapter = countryFilterAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    private fun setupSpecificationAdapter() {
        specificationFilterAdapter =
            SpecificationFilterAdapter(dynamicSpecificationsArrayList, this)
        rv_specification.apply {
            adapter = specificationFilterAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    override fun setOnTextBoxTextChange(value: String, position: Int) {
        dynamicSpecificationsArrayList[position].filterValue = value

    }

    override fun setOnSelectedSpecificationItemFromList(
        parentPosition: Int,
        childPosition: Int
    ) {
        var isSelected: Boolean? =
            dynamicSpecificationsArrayList[parentPosition].subSpecifications?.get(childPosition)?.isSelected
        if (isSelected == true) {
            dynamicSpecificationsArrayList[parentPosition].subSpecifications?.get(childPosition)?.isSelected =
                false
            specificationFilterAdapter.notifyItemChanged(parentPosition)
        } else {
            dynamicSpecificationsArrayList[parentPosition].subSpecifications?.get(childPosition)?.isSelected =
                true
            specificationFilterAdapter.notifyItemChanged(parentPosition)
        }

        //     println("hhhh "+Gson().toJson(dynamicSpecificationsArrayList))
    }

    private fun setClickListeners() {
        btnRegion.setOnClickListener {
            tvError.hide()
            selectionType = regionType
            restViewForTargetFilter(selectionType)
            if (mainCountriesList.isEmpty())
                getCountries()

        }
        btnSubCategory.setOnClickListener {
            selectionType = subCategoryType
            restViewForTargetFilter(selectionType)
            tvError.hide()
            if (categoryList.isEmpty()) {
                getSubCategoriesByCategoryID(categoryId = mianCategoryId)
            }

        }
        btnSpecification.setOnClickListener {
            tvError.hide()
            selectionType = specificationType
            restViewForTargetFilter(selectionType)
            if (dynamicSpecificationsArrayList.isEmpty()) {
                getDynamicSpecification(mianCategoryId)
            }
        }
        btnApplyFilter.setOnClickListener {
            var stringSpecification: ArrayList<String> = ArrayList()
            for (item in dynamicSpecificationsArrayList) {
                if (item.subSpecifications != null && item.subSpecifications?.isNotEmpty() == true) {
                    for (spec in item.subSpecifications!!) {
                        if (spec.isSelected) {
                            item.nameEn?.let { it1 -> stringSpecification.add(it1) }
                        }
                    }
                } else if (item.filterValue != "") {
                    stringSpecification.add(item.filterValue)
                }
            }
            setOnClickListeners.onApplyFilter(
                countryList = countryIdsList,
                regionList = cityIdsList,
                neighoodList = neiberhoodIdsList,
                subCategoryList = subCategoryIdsList,
                specificationList = stringSpecification,
                startPrice = rangePrice.valueFrom,
                endProce = rangePrice.valueTo
            )
            dismiss()
        }
        btnResetFilter.setOnClickListener {
            try {
                for (item in dynamicSpecificationsArrayList) {
                    if (item.subSpecifications != null && item.subSpecifications?.isNotEmpty() == true) {
                        for (spec in item.subSpecifications!!) {
                            spec.isSelected = false
                        }
                    } else if (item.filterValue != "") {
                        item.filterValue = ""
                    }
                }
                specificationFilterAdapter.notifyDataSetChanged()
                rangePrice.setValues(0f,0f)
                //===resetSubCategory
                for (item in categoryList) {
                    item.isSelected = false
                }
                subCategoryIdsList.clear()
                subCategoryAdaper.notifyDataSetChanged()
                //=========reset country
                countryIdsList.clear()
                cityIdsList.clear()
                neiberhoodIdsList.clear()
                for (country in mainCountriesList) {
                    if (country.regionsList != null)
                        for ((index, city) in country.regionsList!!.withIndex()) {
                            city.isSelected = false
                            city.mainNeighborhoodList?.let {
                                for (neighborhood in city.mainNeighborhoodList!!) {
                                    neighborhood.isSelected = false
                                }
                            }

                        }
                }
                countryFilterAdapter.notifyDataSetChanged()
                setOnClickListeners.resetFilter()
            }catch (e:java.lang.Exception){}
        }
    }


    private fun restViewForTargetFilter(type: Int) {
        when (type) {
            regionType -> {
                //region
                btnRegion.setTextColor(context.getColorCompat(R.color.orange))
                btnSubCategory.setTextColor(context.getColorCompat(R.color.black))
                btnSpecification.setTextColor(context.getColorCompat(R.color.black))
                rv_region.show()
                rv_sub_category.hide()
                rv_specification.hide()
                containerPriceSpecification.hide()
            }
            subCategoryType -> {
                //sub category
                btnRegion.setTextColor(context.getColorCompat(R.color.black))
                btnSubCategory.setTextColor(context.getColorCompat(R.color.orange))
                btnSpecification.setTextColor(context.getColorCompat(R.color.black))
                rv_region.hide()
                rv_sub_category.show()
                rv_specification.hide()
                containerPriceSpecification.hide()
            }
            specificationType -> {
                //specification
                btnRegion.setTextColor(context.getColorCompat(R.color.black))
                btnSubCategory.setTextColor(context.getColorCompat(R.color.black))
                btnSpecification.setTextColor(context.getColorCompat(R.color.orange))
                rv_region.hide()
                rv_sub_category.hide()
                rv_specification.show()
                containerPriceSpecification.show()
            }
        }

    }


    fun getSubCategoriesByCategoryID(categoryId: Int) {
        progressBar.show()
        subCagtegoryCallback = RetrofitBuilder.GetRetrofitBuilder()
            .getSubCategoryByMainCategory2(categoryId.toString())
        subCagtegoryCallback?.enqueue(object : Callback<CategoriesResp> {
            override fun onFailure(call: Call<CategoriesResp>, t: Throwable) {
                progressBar.hide()
            }

            override fun onResponse(
                call: Call<CategoriesResp>,
                response: Response<CategoriesResp>
            ) {
                progressBar.hide()
                if (response.isSuccessful) {
                    var categoriesResp: CategoriesResp? = response.body()
                    categoriesResp?.let { categoriesResp ->
                        if (categoriesResp.status_code == 200) {
                            if (categoriesResp.categoryList == null || categoriesResp.categoryList.isEmpty()) {
                                tvError.text = context.getString(R.string.noSubCategoryFound)
                                tvError.show()
                            } else {
                                categoryList.clear()
                                categoryList.addAll(categoriesResp.categoryList)
                                subCategoryAdaper.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        })
    }


    fun getCountries() {
        progressBar.visibility = View.VISIBLE
        countriesCallback = RetrofitBuilder.GetRetrofitBuilder().getCountryNew()
        countriesCallback?.enqueue(object : Callback<CountriesResp> {
            override fun onFailure(call: Call<CountriesResp>, t: Throwable) {
                // println("hhhh "+t.message)
                progressBar.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<CountriesResp>,
                response: Response<CountriesResp>
            ) {
                progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            countryResp = it
                            countryResp.countriesList?.let { countryList ->
                                //ConstantObjects.countryList = countryList
                                getCountriesList(countryList)
                            }
                        }

                    } else {
                        HelpFunctions.ShowLongToast(
                            context.getString(R.string.serverError),
                            context
                        )

                    }
                } catch (e: Exception) {
                }
            }

        })
    }

    fun getRegions(countryId: Int) {
        progressBar.visibility = View.VISIBLE
        regionsCallback = RetrofitBuilder.GetRetrofitBuilder().getRegionNew(countryId)
        regionsCallback?.enqueue(object : Callback<RegionsResp> {
            override fun onFailure(call: Call<RegionsResp>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<RegionsResp>,
                response: Response<RegionsResp>
            ) {
                progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            it.regionsList?.let { regionsList ->
                                //ConstantObjects.countryList = countryList
                                var regionListtemp: ArrayList<Region> = ArrayList()
                                regionListtemp.add(
                                    Region(
                                        mainCountriesList[lastSelectedCountryPosition].id,
                                        context.getString(R.string.all)
                                    )
                                )
                                regionListtemp.addAll(regionsList)
                                mainCountriesList[lastSelectedCountryPosition].regionsList =
                                    regionListtemp
                                updateCountryAdapter()
                            }
                        }

                    } else {
                        HelpFunctions.ShowLongToast(
                            context.getString(R.string.serverError),
                            context
                        )

                    }
                } catch (e: Exception) {
                }
            }

        })
    }

    fun getNeighborhoods(cityId: Int) {
        progressBar.visibility = View.VISIBLE
        neighborhoodsCallback =
            RetrofitBuilder.GetRetrofitBuilder().getNeighborhoodByRegionNew(cityId)
        neighborhoodsCallback?.enqueue(object : Callback<RegionsResp> {
            override fun onFailure(call: Call<RegionsResp>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<RegionsResp>,
                response: Response<RegionsResp>
            ) {
                progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            it.regionsList?.let { regionsList ->
                                var neighborhoodsArrayList: ArrayList<Region> = ArrayList()
                                //ConstantObjects.countryList = countryList
                                neighborhoodsArrayList.add(
                                    Region(
                                        mainCountriesList[lastSelectedCountryPosition].regionsList?.get(
                                            lastSelectedCityPosition
                                        )?.id
                                            ?: 0, context.getString(R.string.all)
                                    )
                                )
                                neighborhoodsArrayList.addAll(regionsList)
                                mainCountriesList[lastSelectedCountryPosition].regionsList?.get(
                                    lastSelectedCityPosition
                                )?.mainNeighborhoodList = neighborhoodsArrayList
                                updateCountryAdapter()
                            }
                        }

                    }
                } catch (e: Exception) {
                }
            }

        })
    }

    private fun updateCountryAdapter() {
        countryFilterAdapter.notifyDataSetChanged()
    }

    private fun getCountriesList(data: List<Country>) {
        mainCountriesList.clear()
        mainCountriesList.addAll(data)
        countryFilterAdapter.notifyDataSetChanged()

    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(listener)
        if (subCagtegoryCallback != null) {
            subCagtegoryCallback?.cancel()
        }
        if (countriesCallback != null) {
            countriesCallback?.cancel()
        }
        if (regionsCallback != null) {
            regionsCallback?.cancel()
        }
        if (neighborhoodsCallback != null) {
            neighborhoodsCallback?.cancel()
        }
        if (dynamicSpecificationCallback != null) {
            dynamicSpecificationCallback?.cancel()
        }

    }

    override fun onSelectedCountry(position: Int, countryId: Int) {
        lastSelectedCountryPosition = position
        getRegions(countryId)
    }

    override fun onSelectedCity(positionCounty: Int, positionCity: Int, cityId: Int) {
        lastSelectedCountryPosition = positionCounty
        lastSelectedCityPosition = positionCity
        getNeighborhoods(cityId)
    }

    override fun setOnSaveCountryToQuery(countryPosition: Int, cityPosition: Int) {
        try {
            var isSelected: Boolean? =
                mainCountriesList[countryPosition].regionsList?.get(cityPosition)?.isSelected

            if (isSelected == true) {
                mainCountriesList[countryPosition].regionsList?.get(cityPosition)?.isSelected =
                    false
                if (countryIdsList.contains(mainCountriesList[countryPosition].id)) {
                    countryIdsList.remove(mainCountriesList[countryPosition].id)
                }

            } else {
                mainCountriesList[countryPosition].regionsList?.get(cityPosition)?.isSelected = true
                if (!countryIdsList.contains(mainCountriesList[countryPosition].id)) {
                    countryIdsList.add(mainCountriesList[countryPosition].id)
                }

            }
            updateCountryAdapter()
            //println("hhhh country "+Gson().toJson(countryIdsList))
        } catch (e: Exception) {
        }
    }

    override fun setOnSaveCityToQuery(
        countryPosition: Int,
        cityPostion: Int,
        mainNeighborhoodPosition: Int
    ) {
        try {


            var isSelected: Boolean? =
                mainCountriesList[countryPosition].regionsList?.get(cityPostion)?.mainNeighborhoodList?.get(
                    mainNeighborhoodPosition
                )?.isSelected
            if (isSelected == true) {
                mainCountriesList[countryPosition].regionsList?.get(cityPostion)?.mainNeighborhoodList?.get(
                    mainNeighborhoodPosition
                )?.isSelected = false
                if (cityIdsList.contains(
                        mainCountriesList[countryPosition].regionsList?.get(
                            cityPostion
                        )?.id
                    )
                ) {
                    cityIdsList.remove(
                        mainCountriesList[countryPosition].regionsList?.get(
                            cityPostion
                        )?.id
                    )
                }

            } else {
                mainCountriesList[countryPosition].regionsList?.get(cityPostion)?.mainNeighborhoodList?.get(
                    mainNeighborhoodPosition
                )?.isSelected = true
                if (!cityIdsList.contains(
                        mainCountriesList[countryPosition].regionsList?.get(
                            cityPostion
                        )?.id
                    )
                ) {
                    cityIdsList.add(
                        mainCountriesList[countryPosition].regionsList?.get(cityPostion)?.id ?: 0
                    )
                }
            }
            updateCountryAdapter()
        } catch (e: Exception) {
        }
        // println("hhhh city ids "+Gson().toJson(cityIdsList))
    }

    override fun setOnSaveNeighborhoodToQuery(
        countryPosition: Int,
        cityPostion: Int,
        mainNeighborhoodPosition: Int
    ) {
        try {
            var isSelected: Boolean? =
                mainCountriesList[countryPosition].regionsList?.get(cityPostion)?.mainNeighborhoodList?.get(
                    mainNeighborhoodPosition
                )?.isSelected
            if (isSelected == true) {
                mainCountriesList[countryPosition].regionsList?.get(cityPostion)?.mainNeighborhoodList?.get(
                    mainNeighborhoodPosition
                )?.isSelected = false
                if (neiberhoodIdsList.contains(
                        mainCountriesList[countryPosition].regionsList?.get(
                            cityPostion
                        )?.mainNeighborhoodList?.get(mainNeighborhoodPosition)?.id
                    )
                ) {
                    neiberhoodIdsList.remove(
                        mainCountriesList[countryPosition].regionsList?.get(
                            cityPostion
                        )?.mainNeighborhoodList?.get(mainNeighborhoodPosition)?.id
                    )
                }

            } else {
                mainCountriesList[countryPosition].regionsList?.get(cityPostion)?.mainNeighborhoodList?.get(
                    mainNeighborhoodPosition
                )?.isSelected = true
                if (!neiberhoodIdsList.contains(
                        mainCountriesList[countryPosition].regionsList?.get(
                            cityPostion
                        )?.mainNeighborhoodList?.get(mainNeighborhoodPosition)?.id
                    )
                ) {
                    neiberhoodIdsList.add(
                        mainCountriesList[countryPosition].regionsList?.get(
                            cityPostion
                        )?.mainNeighborhoodList?.get(mainNeighborhoodPosition)?.id ?: 0
                    )
                }
            }
            updateCountryAdapter()
        } catch (e: java.lang.Exception) {
        }
        //  println("hhhh neigberhood ids "+Gson().toJson(neiberhoodIdsList))
    }

    /***get Specification**/
    fun getDynamicSpecification(categoryId: Int) {
        progressBar.visibility = View.VISIBLE
        dynamicSpecificationCallback = RetrofitBuilder.GetRetrofitBuilder()
            .getDynamicSpecificationForCategory(categoryId.toString())
        dynamicSpecificationCallback?.enqueue(object : Callback<DynamicSpecificationResp> {
            override fun onFailure(call: Call<DynamicSpecificationResp>, t: Throwable) {
                progressBar.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<DynamicSpecificationResp>,
                response: Response<DynamicSpecificationResp>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    response.body()?.let {
                        it.dynamicList?.let { dynamicList ->
                            if (dynamicList == null || dynamicList.isEmpty()) {
                                tvError.text = context.getString(R.string.noSpecificationFound)
                                tvError.show()
                            }else {
                                dynamicSpecificationsArrayList.clear()
                                dynamicSpecificationsArrayList.addAll(dynamicList)
                                specificationFilterAdapter.notifyDataSetChanged()
                            }
                        }

                    }
                }
            }
        })
    }

    interface SetOnClickListeners {
        fun onApplyFilter(
            countryList: List<Int>,
            regionList: List<Int>,
            neighoodList: List<Int>,
            subCategoryList: List<Int>,
            specificationList: List<String>,
            startPrice: Float,
            endProce: Float
        )
        fun resetFilter()
    }
}