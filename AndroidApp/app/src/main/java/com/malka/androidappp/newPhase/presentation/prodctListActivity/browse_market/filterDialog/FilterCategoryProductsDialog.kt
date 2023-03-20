package com.malka.androidappp.newPhase.presentation.prodctListActivity.browse_market.filterDialog

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseDialog
import com.malka.androidappp.newPhase.data.helper.getColorCompat
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.presentation.prodctListActivity.browse_market.filterDialog.adapter.CountryFilterAdapter
import com.malka.androidappp.newPhase.presentation.prodctListActivity.browse_market.filterDialog.adapter.SpecificationFilterAdapter
import com.malka.androidappp.newPhase.presentation.prodctListActivity.browse_market.filterDialog.adapter.SubCategoryFilterAdapter
import kotlinx.android.synthetic.main.dialog_filter_category_products.*

class FilterCategoryProductsDialog(context: Context, var selectionType: Int) : BaseDialog(context) {

    lateinit var countryFilterAdapter: CountryFilterAdapter
    lateinit var specificationFilterAdapter: SpecificationFilterAdapter
    lateinit var subCategoryAdaper: SubCategoryFilterAdapter
    override fun getViewId(): Int = R.layout.dialog_filter_category_products

    companion object { // 1 for region ,2 for sub category  ,3 for specification
       const  val regionType = 1
        const  val subCategoryType = 2
        const  val specificationType = 3
    }

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = true
    override fun isLoadingDialog(): Boolean = false
    override fun initialization() {
        restViewForTargetFilter(selectionType)
        setupCountryAdapter()
        setupSpecificationAdapter()
        setupSubCategoryAdapetr()
        setClickListeners()

    }

    private fun setupSubCategoryAdapetr() {
        subCategoryAdaper = SubCategoryFilterAdapter()
        rv_sub_category.apply {
            adapter = subCategoryAdaper
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }


    private fun setupCountryAdapter() {
        countryFilterAdapter = CountryFilterAdapter()
        rv_region.apply {
            adapter = countryFilterAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    private fun setupSpecificationAdapter() {
        specificationFilterAdapter = SpecificationFilterAdapter()
        rv_specification.apply {
            adapter = specificationFilterAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    private fun setClickListeners() {
        btnRegion.setOnClickListener {
            selectionType = regionType
            restViewForTargetFilter(selectionType)

        }
        btnSubCategory.setOnClickListener {
            selectionType = subCategoryType
            restViewForTargetFilter(selectionType)
        }
        btnSpecification.setOnClickListener {
            selectionType = specificationType
            restViewForTargetFilter(selectionType)
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


}