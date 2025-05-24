package com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.DialogFilterCategoryProductsBinding
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
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.domain.models.regionsResp.Region
import com.malqaa.androidappp.newPhase.domain.models.regionsResp.RegionsResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.CategoryProductViewModel
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog.adapter.CategoryAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog.adapter.CategorySearchFilterSpinnerAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog.adapter.CountryFilterAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog.adapter.OnCategorySelectedListener
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog.adapter.SpecificationFilterAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog.adapter.SubCategoryFilterAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog.adapter.SubCategorySearchFilterAdapter
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.getColorCompat
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
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
) : BaseDialog<DialogFilterCategoryProductsBinding>(context),
    CountryFilterAdapter.SetonClickListeners, ListenerCallBackRegions,
    ListenerCallDynamicSpecification,
    ListenerCallBackNeighborhoods, OnCategorySelectedListener {

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
    lateinit var categoryAdapter: CategoryAdapter

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

    companion object { // 1 for region ,2 for sub category  ,3 for specification
        const val regionType = 1
        const val subCategoryType = 2
        const val specificationType = 3
    }

    override fun inflateViewBinding(): DialogFilterCategoryProductsBinding {
        return DialogFilterCategoryProductsBinding.inflate(layoutInflater)
    }

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = true
    override fun isLoadingDialog(): Boolean = false
    override fun initialization() {
        setupCountryAdapter()
        setupSpecificationAdapter()
        setupSubCategoryFromCategoryAdapetr()
        initCategoryExpandableListAdapter()
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
                binding.btnRegion.performClick()
            }

            subCategoryType -> {
                binding.btnSubCategory.performClick()
            }

            specificationType -> {
                binding.btnSpecification.performClick()
            }
        }
        when (comeFrom) {
            ConstantObjects.search_categoriesDetails -> {
                binding.btnSubCategory.text = context.getText(R.string.Category)
            }

            ConstantObjects.search_product -> {
                binding.btnSubCategory.text = context.getText(R.string.Categories)
            }

            ConstantObjects.search_seller -> {
                binding.btnSubCategory.text = context.getText(R.string.Categories)
            }
        }
        setClickListeners()

    }

    fun initProduct(products: List<Product>) {
        Log.i("text #1", "products size: ${products.size}, products: $products")

        val minPrice = products.minOfOrNull { it.price } ?: 0f
        var maxPrice = products.maxOfOrNull { it.price } ?: 2000000f

        // Ensure maxPrice is always greater than minPrice
        if (minPrice == maxPrice) {
            maxPrice += 1f
        }

        // Set range slider values
        binding.rangePrice.valueFrom = minPrice
        binding.rangePrice.valueTo = maxPrice
        binding.rangePrice.values = listOf(minPrice, maxPrice)

        // Set EditText values
        binding.etPriceFrom.setText(minPrice.toInt().toString())
        binding.etPriceTo.setText(maxPrice.toInt().toString())

        binding.rangePrice.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            binding.etPriceFrom.setText(values[0].toInt().toString())
            binding.etPriceTo.setText(values[1].toInt().toString())
        }
    }

    private fun initCategoryExpandableListAdapter() {
        categoryAdapter =
            CategoryAdapter(subCategoryFromCategoryList, onCategorySelectedListener = this)
        binding.rvSubCategory.setAdapter(categoryAdapter)
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
        binding.rvSubCategory2.apply {
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
//        binding.rvSubCategory.apply {
//            adapter = subCategoryAdaper
//            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
//        }
    }

    private fun updateSubCategoryAdapter() {
        subCategoryAdaper.notifyDataSetChanged()
        categoryAdapter.notifyDataSetChanged()
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
        binding.spinnerDegree.adapter = categorySearchFilterSpinnerAdapter
        binding.spinnerDegree.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position != 0) {
                    mianCategoryId = categoryList[position].id
                    getSubCategoryForCategory(mianCategoryId)

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun setupCountryAdapter() {
        mainCountriesList = ArrayList()
        countryFilterAdapter = CountryFilterAdapter(mainCountriesList, this)
        binding.rvRegion.apply {
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
        binding.rvSpecification.apply {
            adapter = specificationFilterAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }


    fun getCountries() {
        binding.progressBar.visibility = View.VISIBLE
        countriesCallback = getRetrofitBuilder().getCountryNew()
        countriesCallback?.enqueue(object : Callback<CountriesResp> {
            override fun onFailure(call: Call<CountriesResp>, t: Throwable) {
                // println("hhhh "+t.message)
                binding.progressBar.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<CountriesResp>,
                response: Response<CountriesResp>
            ) {
                binding.progressBar.visibility = View.GONE
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
        binding.progressBar.visibility = View.VISIBLE
        binding.tvError.hide()
        subCategoriesCall =
            getRetrofitBuilder()
                .getSubCategoryByMainCategory2(categoryId.toString())
        subCategoriesCall?.enqueue(object : Callback<CategoriesResp> {
            override fun onFailure(call: Call<CategoriesResp>, t: Throwable) {
                binding.tvError.show()
                binding.tvError.text = context.getString(R.string.noCategoryFound)
            }

            override fun onResponse(
                call: Call<CategoriesResp>,
                response: Response<CategoriesResp>
            ) {
                binding.progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            it.categoryList?.let { categoryList2 ->
                                subCategoryFromCategoryList.clear()
                                subCategoryFromCategoryList.addAll(categoryList2)

                                if (subCategoryFromCategoryList.isEmpty()) {
                                    binding.tvError.show()
                                    binding.tvError.text =
                                        context.getString(R.string.noCategoryFound)
                                } else {
                                    updateCategoryWithSubCategoriesAdapter()
                                    updateSubCategoryAdapter()
                                }
                            }
                        }

                    }
                } catch (e: Exception) {
                    binding.tvError.show()
                    binding.tvError.text = context.getString(R.string.noCategoryFound)
                }
            }

        })
    }

    private fun updateCategoryWithSubCategoriesAdapter() {
        subCategorySearchFilterAdapter.notifyDataSetChanged()
    }


    /***click Listeners**/
    private fun setClickListeners() {
        binding.btnRegion.setOnClickListener {
            binding.tvError.hide()
            selectionType = regionType
            restViewForTargetFilter(selectionType)
            if (mainCountriesList.isEmpty())
                getCountries()

        }
        binding.btnSubCategory.setOnClickListener {
            selectionType = subCategoryType
            restViewForTargetFilter(selectionType)
            binding.tvError.hide()
            if (categoryList.isEmpty()) {
                binding.tvError.show()
                binding.tvError.text = context.getString(R.string.noCategoryFound)
            }
        }
        binding.btnSpecification.setOnClickListener {
            binding.tvError.hide()
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
                        binding.tvError.show()
                        binding.tvError.text = context.getString(R.string.SelectCategory)
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
                        binding.tvError.show()
                        binding.tvError.text = context.getString(R.string.SelectCategory)
                    } else {
                        dynamicSpecificationsArrayList.clear()
                        // if (dynamicSpecificationsArrayList.isEmpty()) {
                        productsListViewModel.getDynamicSpecification(mianCategoryId, this)
                        //   }
                    }

                }

            }

        }
        binding.btnApplyFilter.setOnClickListener {
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

            val selectedRange = binding.rangePrice.values
            val startPrice = selectedRange[0]
            val endPrice = selectedRange[1]

            Log.i("test #", "subCategoryIdsList: $subCategoryIdsList")

            setOnClickListeners.onApplyFilter(
                countryList = countryIdsList,
                regionList = cityIdsList,
                neighoodList = neiberhoodIdsList,
                subCategoryList = subCategoryIdsList,
                specificationList = stringSpecification,
                startPrice = startPrice,
                endPrice = endPrice,
                mainCategoryId = mianCategoryId
            )

            // Call reset when needed
            categoryAdapter.reset()
            dismiss()
        }
        binding.btnResetFilter.setOnClickListener {
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
                binding.rangePrice.setValues(0f, 0f)
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
                binding.btnRegion.performClick()
            }

            subCategoryType -> {
                binding.btnSubCategory.performClick()
            }

            specificationType -> {
                binding.btnSpecification.performClick()
            }
        }
    }

    private fun restViewForTargetFilter(type: Int) {
        when (type) {
            regionType -> {
                //region
                binding.btnRegion.setTextColor(context.getColorCompat(R.color.orange))
                binding.btnSubCategory.setTextColor(context.getColorCompat(R.color.black))
                binding.btnSpecification.setTextColor(context.getColorCompat(R.color.black))
                binding.rvRegion.show()
                binding.rvSubCategory.hide()
                binding.containerCategory.hide()
                binding.rvSpecification.hide()
                binding.containerPriceSpecification.hide()
            }

            subCategoryType -> {
                //sub category
                binding.btnRegion.setTextColor(context.getColorCompat(R.color.black))
                binding.btnSubCategory.setTextColor(context.getColorCompat(R.color.orange))
                binding.btnSpecification.setTextColor(context.getColorCompat(R.color.black))
                binding.rvRegion.hide()
                binding.rvSpecification.hide()
                binding.containerPriceSpecification.hide()
                when (comeFrom) {
                    ConstantObjects.search_categoriesDetails -> {
                        binding.rvSubCategory.show()
                        binding.containerCategory.hide()
                    }

                    ConstantObjects.search_product -> {
                        binding.rvSubCategory.hide()
                        binding.containerCategory.show()
                    }

                    ConstantObjects.search_seller -> {
                        binding.rvSubCategory.hide()
                        binding.containerCategory.show()
                    }
                }
            }

            specificationType -> {
                //specification
                binding.btnRegion.setTextColor(context.getColorCompat(R.color.black))
                binding.btnSubCategory.setTextColor(context.getColorCompat(R.color.black))
                binding.btnSpecification.setTextColor(context.getColorCompat(R.color.orange))
                binding.rvRegion.hide()
                binding.rvSubCategory.hide()
                binding.containerCategory.hide()
                binding.rvSpecification.show()
                binding.containerPriceSpecification.show()
            }
        }

    }


    fun setCategories(categories: List<CategoriesSearchItem>) {
        categoryList.clear()

        if (isShowing) {
            when (comeFrom) {
                ConstantObjects.search_categoriesDetails -> {
                    Log.i("test #1", "search_categoriesDetails")
                    getSubCategoryForCategory(mianCategoryId)
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
        binding.progressBar.visibility = View.VISIBLE
        productsListViewModel.getRegions(countryId, this, context)
    }

    override fun onSelectedCity(positionCounty: Int, positionCity: Int, cityId: Int) {
        lastSelectedCountryPosition = positionCounty
        lastSelectedCityPosition = positionCity
        binding.progressBar.visibility = View.VISIBLE
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
        cityPosition: Int,
        mainNeighborhoodPosition: Int
    ) {
        try {
            val countryId = mainCountriesList[countryPosition].id
            val cityId = mainCountriesList[countryPosition].regionsList?.get(cityPosition)?.id
            val isSelected: Boolean? =
                mainCountriesList[countryPosition].regionsList?.get(cityPosition)?.mainNeighborhoodList?.get(
                    mainNeighborhoodPosition
                )?.isSelected

            if (isSelected == true) {
                // Deselect the city
                mainCountriesList[countryPosition].regionsList?.get(cityPosition)?.mainNeighborhoodList?.get(
                    mainNeighborhoodPosition
                )?.isSelected = false

                if (cityIdsList.contains(cityId)) {
                    cityIdsList.remove(cityId)
                }
            } else {
                // Only add if the City ID is not the same as the Country ID
                if (cityId != countryId) {
                    mainCountriesList[countryPosition].regionsList?.get(cityPosition)?.mainNeighborhoodList?.get(
                        mainNeighborhoodPosition
                    )?.isSelected = true

                    if (!cityIdsList.contains(cityId)) {
                        cityIdsList.add(cityId ?: 0) // Add city ID or 0 as a fallback
                    }
                }
            }
            updateCountryAdapter()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setOnSaveNeighborhoodToQuery(
        countryPosition: Int,
        cityPosition: Int,
        mainNeighborhoodPosition: Int
    ) {
        try {
            val countryId = mainCountriesList[countryPosition].id
            val cityId = mainCountriesList[countryPosition].regionsList?.get(cityPosition)?.id
            val neighborhoodId = mainCountriesList[countryPosition].regionsList
                ?.get(cityPosition)
                ?.mainNeighborhoodList
                ?.get(mainNeighborhoodPosition)
                ?.id

            val isSelected: Boolean? = mainCountriesList[countryPosition].regionsList
                ?.get(cityPosition)
                ?.mainNeighborhoodList
                ?.get(mainNeighborhoodPosition)
                ?.isSelected

            if (isSelected == true) {
                // Deselect the neighborhood
                mainCountriesList[countryPosition].regionsList
                    ?.get(cityPosition)
                    ?.mainNeighborhoodList
                    ?.get(mainNeighborhoodPosition)
                    ?.isSelected = false

                if (neiberhoodIdsList.contains(neighborhoodId)) {
                    neiberhoodIdsList.remove(neighborhoodId)
                }
            } else {
                // Only add the neighborhood if its ID is unique and valid
                if (neighborhoodId != countryId && neighborhoodId != cityId) {
                    mainCountriesList[countryPosition].regionsList
                        ?.get(cityPosition)
                        ?.mainNeighborhoodList
                        ?.get(mainNeighborhoodPosition)
                        ?.isSelected = true

                    if (!neiberhoodIdsList.contains(neighborhoodId)) {
                        neiberhoodIdsList.add(
                            neighborhoodId ?: 0
                        ) // Add neighborhood ID or 0 as fallback
                    }
                }
            }
            updateCountryAdapter()
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
            endPrice: Float,
            mainCategoryId: Int
        )

        fun resetFilter()
    }

    override fun callBackListener(isFailed: Boolean, response: RegionsResp?) {
        binding.progressBar.visibility = View.GONE
        if (!isFailed) {
            response?.regionsList?.let { regionsList ->
                if (lastSelectedCountryPosition in mainCountriesList.indices) {
                    val regionList: ArrayList<Region> = ArrayList()
                    // Add "All" region
                    regionList.add(
                        Region(
                            mainCountriesList[lastSelectedCountryPosition].id,
                            context.getString(R.string.all)
                        )
                    )
                    regionList.addAll(regionsList)
                    // Update the regions list
                    mainCountriesList[lastSelectedCountryPosition].regionsList = regionList
                    updateCountryAdapter()
                } else {
                    // Handle invalid country position
                    Log.e(
                        "callBackListener",
                        "Invalid country position: $lastSelectedCountryPosition"
                    )
                }
            }
        }
    }

    override fun callBackListenerNeighborhoods(isFailed: Boolean, response: RegionsResp?) {
        binding.progressBar.visibility = View.GONE
        if (!isFailed) {
            if (lastSelectedCountryPosition in mainCountriesList.indices &&
                mainCountriesList[lastSelectedCountryPosition].regionsList?.let { lastSelectedCityPosition in it.indices } == true
            ) {
                val neighborhoodsArrayList: ArrayList<Region> = ArrayList()

                // Add "All" neighborhood option
                neighborhoodsArrayList.add(
                    Region(
                        mainCountriesList[lastSelectedCountryPosition].regionsList?.get(
                            lastSelectedCityPosition
                        )?.id ?: 0,
                        context.getString(R.string.all)
                    )
                )
                response?.regionsList?.let {
                    neighborhoodsArrayList.addAll(it)
                }

                mainCountriesList[lastSelectedCountryPosition].regionsList?.get(
                    lastSelectedCityPosition
                )?.mainNeighborhoodList = neighborhoodsArrayList
                updateCountryAdapter()
            } else {
                Log.e("callBackListenerNeighborhoods", "Invalid country or city position")
            }
        }
    }

    override fun callBackDynamicSpecification(
        isFailed: Boolean,
        response: DynamicSpecificationResp?
    ) {
        binding.progressBar.visibility = View.GONE
        if (!isFailed) {
            val dynamicList = response?.dynamicList
            if (dynamicList.isNullOrEmpty()) {
                binding.tvError.text = context.getString(R.string.noSpecificationFound)
                binding.tvError.show()
            } else {
                dynamicSpecificationsArrayList.clear()
                dynamicSpecificationsArrayList.addAll(dynamicList)
                specificationFilterAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCategorySelected(category: Category) {
        // Check if the category is selected
        if (category.isSelected) {
            // If the category is selected, add it to the list if it doesn't exist
            if (!subCategoryIdsList.contains(category.id)) {
                subCategoryIdsList.add(category.id)
            }
        } else {
            // If the category is not selected, remove it from the list if it exists
            subCategoryIdsList.remove(category.id)
        }

        // Update the adapter after modifying the list
        updateSubCategoryAdapter()
    }
}