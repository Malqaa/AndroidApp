package com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.data.network.service.ListenerCallBackNeighborhoods
import com.malqaa.androidappp.newPhase.data.network.service.ListenerCallBackRegions
import com.malqaa.androidappp.newPhase.data.network.service.ListenerCallDynamicSpecification
import com.malqaa.androidappp.newPhase.domain.models.categoryResp.CategoriesResp
import com.malqaa.androidappp.newPhase.domain.models.countryResp.CountriesResp
import com.malqaa.androidappp.newPhase.domain.models.countryResp.Country
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationItem
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationResp
import com.malqaa.androidappp.newPhase.domain.models.productResp.CategoriesSearchItem
import com.malqaa.androidappp.newPhase.domain.models.regionsResp.Region
import com.malqaa.androidappp.newPhase.domain.models.regionsResp.RegionsResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.CategoryProductViewModel
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog.adapter.*
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.getColorCompat
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
import kotlinx.android.synthetic.main.dialog_filter_category_products.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilterCategoryProductsDialog(
    val productsListViewModel: CategoryProductViewModel,
    context: Context,
    var selectionType: Int,
    var mianCategoryId: Int,
    var setOnClickListeners: SetOnClickListeners,
    var comeFrom: Int
) : BaseDialog(context), CountryFilterAdapter.SetonClickListeners, ListenerCallBackRegions,
    ListenerCallDynamicSpecification,
    ListenerCallBackNeighborhoods {

    private var dynamicSpecificationCallback: Call<DynamicSpecificationResp>? = null
    private var neighborhoodsCallback: Call<RegionsResp>? = null
    private var regionsCallback: Call<RegionsResp>? = null
    private var subCagtegoryCallback: Call<CategoriesResp>? = null
    lateinit var countryFilterAdapter: CountryFilterAdapter
    lateinit var specificationFilterAdapter: SpecificationFilterAdapter
    lateinit var subCategoryAdaper: SubCategoryFilterAdapter
    lateinit var categorySearchFilterSpinnerAdapter: CategorySearchFilterSpinnerAdapter
    var categoryList: ArrayList<CategoriesSearchItem> = ArrayList()
    lateinit var subCategoryFromCategoryList: ArrayList<Category>
    lateinit var subCategorySearchFilterAdapter: SubCategorySearchFilterAdapter

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

    var subCategoriesCall: Call<CategoriesResp>? = null

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
        setupSubCategoryFromCategoryAdapetr()
        when (comeFrom) {
            ConstantObjects.search_categoriesDetails -> {
                setupSubCategoryAdapetr()
            }

            ConstantObjects.search_product -> {
                setupCategorySpinnerAdapter()
            }

            ConstantObjects.search_seller -> {
                setupCategorySpinnerAdapter()

            }

        }

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
        when (comeFrom) {
            ConstantObjects.search_categoriesDetails -> {
                btnSubCategory.text = context.getText(R.string.sub_categories)
            }

            ConstantObjects.search_product -> {
                btnSubCategory.text = context.getText(R.string.Categories)
            }

            ConstantObjects.search_seller -> {
                btnSubCategory.text = context.getText(R.string.Categories)
            }
        }
        setClickListeners()

    }

    private fun setupSubCategoryFromCategoryAdapetr() {
        subCategoryFromCategoryList = ArrayList()
        subCategorySearchFilterAdapter = SubCategorySearchFilterAdapter(subCategoryFromCategoryList,
            object : SubCategorySearchFilterAdapter.SetOnselectedListerner {
                override fun setOnSelectSubCategories(position: Int, subCategoryId: Int) {
                    if (subCategoryFromCategoryList[position].isSelected) {
                        if (subCategoryIdsList.contains(subCategoryFromCategoryList[position].id)) {
                            subCategoryIdsList.remove(subCategoryFromCategoryList[position].id)
                        }
                    } else {
                        if (!subCategoryIdsList.contains(subCategoryFromCategoryList[position].id)) {
                            subCategoryIdsList.add(subCategoryFromCategoryList[position].id)
                        }
                    }
                    subCategoryFromCategoryList[position].isSelected =
                        !subCategoryFromCategoryList[position].isSelected
                    subCategorySearchFilterAdapter.notifyDataSetChanged()
                }

            })
        rv_sub_category_2.apply {
            adapter = subCategorySearchFilterAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    private fun setupSubCategoryAdapetr() {
        //  categoryList = ArrayList()
        subCategoryAdaper = SubCategoryFilterAdapter(
            categoryList,
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
                    updateSubCategoryAdapter()
                    // println("hhhh subCategories "+Gson().toJson(subCategoryIdsList))
                }

            })
        rv_sub_category.apply {
            adapter = subCategoryAdaper
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    private fun updateSubCategoryAdapter() {
        subCategoryAdaper.notifyDataSetChanged()
    }

    private fun setupCategorySpinnerAdapter() {
        categoryList = ArrayList()
        val header = CategoriesSearchItem(
            id = 0,
            name = context.getString(R.string.Categories)
        )
        categoryList.add(header)
        categorySearchFilterSpinnerAdapter =
            CategorySearchFilterSpinnerAdapter(context, categoryList)
        spinnerDegree.adapter = categorySearchFilterSpinnerAdapter
        spinnerDegree.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position != 0) {
                    mianCategoryId = categoryList[position].id
                    getSubCategoryForCategory(mianCategoryId)

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
//        spinnerDegree.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    adapterView: AdapterView<*>?,
//                    view: View,
//                    position: Int,
//                    l: Long
//                ) {
//                    if (position != 0) {
//
//                    }
//
//                }
//
//                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
//            }
//        categorySearchFilterAdapter = CategorySearchFilterAdapter(categoryList,
//            object :CategorySearchFilterAdapter.SetonClickListeners{
//            override fun onSelectedCategory(position: Int, categoryId: Int) {
//                getSubCategoryForCategory(position,categoryId)
//            }
//
//        })
//        rv_sub_category.apply {
//            adapter = categorySearchFilterAdapter
//            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
//        }

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
            SpecificationFilterAdapter(
                dynamicSpecificationsArrayList,
                object : SpecificationFilterAdapter.OnChangeValueListener {
                    override fun setOnTextBoxTextChange(value: String, position: Int) {
                        dynamicSpecificationsArrayList[position].filterValue = value
                    }

                    override fun setOnSelectedSpecificationItemFromList(
                        parentPosition: Int,
                        childPosition: Int
                    ) {
                        val isSelected: Boolean? =
                            dynamicSpecificationsArrayList[parentPosition].subSpecifications?.get(
                                childPosition
                            )?.isSelected
                        if (isSelected == true) {
                            dynamicSpecificationsArrayList[parentPosition].subSpecifications?.get(
                                childPosition
                            )?.isSelected =
                                false
                            specificationFilterAdapter.notifyItemChanged(parentPosition)
                        } else {
                            dynamicSpecificationsArrayList[parentPosition].subSpecifications?.get(
                                childPosition
                            )?.isSelected =
                                true
                            specificationFilterAdapter.notifyItemChanged(parentPosition)
                        }

                        //     println("hhhh "+Gson().toJson(dynamicSpecificationsArrayList))
                    }

                })
        rv_specification.apply {
            adapter = specificationFilterAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }


    fun getCountries() {
        progressBar.visibility = View.VISIBLE
        countriesCallback = getRetrofitBuilder().getCountryNew()
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
                } catch (_: Exception) {
                }
            }

        })
    }

    private fun getSubCategoryForCategory(categoryId: Int) {
        progressBar.visibility = View.VISIBLE
        tvError.hide()
        subCategoriesCall =
            getRetrofitBuilder()
                .getSubCategoryByMainCategory2(categoryId.toString())
        subCategoriesCall?.enqueue(object : Callback<CategoriesResp> {
            override fun onFailure(call: Call<CategoriesResp>, t: Throwable) {
                tvError.show()
                tvError.text = context.getString(R.string.noSubCategoryFound)
            }

            override fun onResponse(
                call: Call<CategoriesResp>,
                response: Response<CategoriesResp>
            ) {
                progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            it.categoryList?.let { categoryList2 ->
                                subCategoryFromCategoryList.clear()
                                subCategoryFromCategoryList.addAll(categoryList2)

                                if (subCategoryFromCategoryList.isEmpty()) {
                                    tvError.show()
                                    tvError.text = context.getString(R.string.noSubCategoryFound)
                                } else {
                                    updateCategoryWithSubCategoriesAdapter()
                                }
                            }
                        }

                    }
                } catch (e: Exception) {
                    tvError.show()
                    tvError.text = context.getString(R.string.noSubCategoryFound)
                }
            }

        })
    }

    private fun updateCategoryWithSubCategoriesAdapter() {
        subCategorySearchFilterAdapter.notifyDataSetChanged()
    }


    /***click Listeners**/
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
                tvError.show()
                tvError.text = context.getString(R.string.noSubCategoryFound)
            }
//            if (categoryList.isEmpty()) {
//                getSubCategoriesByCategoryID(categoryId = mianCategoryId)
//            }

        }
        btnSpecification.setOnClickListener {
            tvError.hide()
            when (comeFrom) {
                ConstantObjects.search_categoriesDetails -> {
                    selectionType = specificationType
                    restViewForTargetFilter(selectionType)
                    if (dynamicSpecificationsArrayList.isEmpty()) {
                        productsListViewModel.getDynamicSpecification(mianCategoryId, this)
                    }
                }

                ConstantObjects.search_product -> {
                    selectionType = specificationType
                    restViewForTargetFilter(selectionType)
                    if (mianCategoryId == 0) {
                        tvError.show()
                        tvError.text = context.getString(R.string.SelectCategory)
                    } else {
                        dynamicSpecificationsArrayList.clear()
                        // if (dynamicSpecificationsArrayList.isEmpty()) {
                        productsListViewModel.getDynamicSpecification(mianCategoryId, this)
                        //   }
                    }

                }

                ConstantObjects.search_seller -> {
                    selectionType = specificationType
                    restViewForTargetFilter(selectionType)
                    if (mianCategoryId == 0) {
                        tvError.show()
                        tvError.text = context.getString(R.string.SelectCategory)
                    } else {
                        dynamicSpecificationsArrayList.clear()
                        // if (dynamicSpecificationsArrayList.isEmpty()) {
                        productsListViewModel.getDynamicSpecification(mianCategoryId, this)
                        //   }
                    }

                }

            }

        }
        btnApplyFilter.setOnClickListener {
            val stringSpecification: ArrayList<String> = ArrayList()
            println("hhhh " + Gson().toJson(dynamicSpecificationsArrayList))
            for (item in dynamicSpecificationsArrayList) {
                if (item.subSpecifications != null && item.subSpecifications?.isNotEmpty() == true) {
                    for (spec in item.subSpecifications!!) {
                        if (spec.isSelected) {
                            if (ConstantObjects.currentLanguage == ConstantObjects.ARABIC) {
                                spec.nameAr?.let { it1 -> stringSpecification.add(it1) }
                            } else {
                                spec.nameEn?.let { it1 -> stringSpecification.add(it1) }
                            }
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
                endProce = rangePrice.valueTo,
                mainCategoryId = mianCategoryId
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
                rangePrice.setValues(0f, 0f)
                //===resetSubCategory
                when (comeFrom) {
                    ConstantObjects.search_categoriesDetails -> {
                        for (item in categoryList) {
                            item.isSelected = false
                        }
                        subCategoryIdsList.clear()
                        subCategoryAdaper.notifyDataSetChanged()
                    }

                    ConstantObjects.search_product -> {
                        subCategoryIdsList.clear()
                        for (item in subCategoryFromCategoryList) {
                            item.isSelected = false
                        }
                        subCategorySearchFilterAdapter.notifyDataSetChanged()
                    }

                    ConstantObjects.search_seller -> {
                        subCategoryIdsList.clear()
                        for (item in subCategoryFromCategoryList) {
                            item.isSelected = false
                        }
                        subCategorySearchFilterAdapter.notifyDataSetChanged()
                    }
                }

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
                dismiss()
            } catch (e: java.lang.Exception) {
            }
        }
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

    private fun restViewForTargetFilter(type: Int) {
        when (type) {
            regionType -> {
                //region
                btnRegion.setTextColor(context.getColorCompat(R.color.orange))
                btnSubCategory.setTextColor(context.getColorCompat(R.color.black))
                btnSpecification.setTextColor(context.getColorCompat(R.color.black))
                rv_region.show()
                rv_sub_category.hide()
                containerCategory.hide()
                rv_specification.hide()
                containerPriceSpecification.hide()
            }

            subCategoryType -> {
                //sub category
                btnRegion.setTextColor(context.getColorCompat(R.color.black))
                btnSubCategory.setTextColor(context.getColorCompat(R.color.orange))
                btnSpecification.setTextColor(context.getColorCompat(R.color.black))
                rv_region.hide()
                rv_specification.hide()
                containerPriceSpecification.hide()
                when (comeFrom) {
                    ConstantObjects.search_categoriesDetails -> {
                        rv_sub_category.show()
                        containerCategory.hide()
                    }

                    ConstantObjects.search_product -> {
                        rv_sub_category.hide()
                        containerCategory.show()
                    }

                    ConstantObjects.search_seller -> {
                        rv_sub_category.hide()
                        containerCategory.show()
                    }
                }
            }

            specificationType -> {
                //specification
                btnRegion.setTextColor(context.getColorCompat(R.color.black))
                btnSubCategory.setTextColor(context.getColorCompat(R.color.black))
                btnSpecification.setTextColor(context.getColorCompat(R.color.orange))
                rv_region.hide()
                rv_sub_category.hide()
                containerCategory.hide()
                rv_specification.show()
                containerPriceSpecification.show()
            }
        }

    }


    fun setCategories(categories: List<CategoriesSearchItem>) {
        categoryList.clear()

        if (isShowing) {
            when (comeFrom) {
                ConstantObjects.search_categoriesDetails -> {
                    categoryList.addAll(categories)
                    subCategoryAdaper.notifyDataSetChanged()
                }

                ConstantObjects.search_product -> {
                    val header = CategoriesSearchItem(
                        id = 0,
                        name = context.getString(R.string.Categories)
                    )
                    categoryList.add(header)
                    categoryList.addAll(categories)
                    categorySearchFilterSpinnerAdapter.notifyDataSetChanged()
                }

                ConstantObjects.search_seller -> {
                    val header = CategoriesSearchItem(
                        id = 0,
                        name = context.getString(R.string.Categories)
                    )
                    categoryList.add(header)
                    categoryList.addAll(categories)
                    categorySearchFilterSpinnerAdapter.notifyDataSetChanged()
                }
            }
        }
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
        progressBar.visibility = View.VISIBLE
        productsListViewModel.getRegions(countryId, this, context)
    }

    override fun onSelectedCity(positionCounty: Int, positionCity: Int, cityId: Int) {
        lastSelectedCountryPosition = positionCounty
        lastSelectedCityPosition = positionCity
        progressBar.visibility = View.VISIBLE
        productsListViewModel.getNeighborhoods(cityId, this)
    }

    override fun setOnSaveCountryToQuery(countryPosition: Int, cityPosition: Int) {
        try {
            val isSelected: Boolean? =
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
            val isSelected: Boolean? =
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

    /*****************************/
    /*****************************/
    /*****************************/
    /*****************************/


    interface SetOnClickListeners {
        fun onApplyFilter(
            countryList: List<Int>,
            regionList: List<Int>,
            neighoodList: List<Int>,
            subCategoryList: List<Int>,
            specificationList: List<String>,
            startPrice: Float,
            endProce: Float,
            mainCategoryId: Int
        )

        fun resetFilter()
    }

    override fun callBackListener(isFailed: Boolean, response: RegionsResp?) {
        progressBar.visibility = View.GONE
        if (!isFailed) {
            response?.let {
                it.regionsList?.let { regionsList ->
                    val regionList: ArrayList<Region> = ArrayList()
                    regionList.add(
                        Region(
                            mainCountriesList[lastSelectedCountryPosition].id,
                            context.getString(R.string.all)
                        )
                    )
                    regionList.addAll(regionsList)
                    mainCountriesList[lastSelectedCountryPosition].regionsList =
                        regionList
                    updateCountryAdapter()
                }
            }
        }
    }

    override fun callBackListenerNeighborhoods(isFailed: Boolean, response: RegionsResp?) {
        progressBar.visibility = View.GONE
        if (!isFailed) {
            val neighborhoodsArrayList: ArrayList<Region> = ArrayList()
            //ConstantObjects.countryList = countryList
            neighborhoodsArrayList.add(
                Region(
                    mainCountriesList[lastSelectedCountryPosition].regionsList?.get(
                        lastSelectedCityPosition
                    )?.id
                        ?: 0, context.getString(R.string.all)
                )
            )
            neighborhoodsArrayList.addAll(response!!.regionsList ?: arrayListOf())
            mainCountriesList[lastSelectedCountryPosition].regionsList?.get(
                lastSelectedCityPosition
            )?.mainNeighborhoodList = neighborhoodsArrayList
            updateCountryAdapter()
        }
    }

    override fun callBackDynamicSpecification(
        isFailed: Boolean,
        response: DynamicSpecificationResp?
    ) {
        progressBar.visibility = View.GONE
        if (!isFailed) {
            response?.dynamicList?.let { dynamicList ->
                if (dynamicList == null || dynamicList.isEmpty()) {
                    tvError.text = context.getString(R.string.noSpecificationFound)
                    tvError.show()
                } else {
                    dynamicSpecificationsArrayList.clear()
                    dynamicSpecificationsArrayList.addAll(dynamicList)
                    specificationFilterAdapter.notifyDataSetChanged()
                }
            }
        }
    }

}