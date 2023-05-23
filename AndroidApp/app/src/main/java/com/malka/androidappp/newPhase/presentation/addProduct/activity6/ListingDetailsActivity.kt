package com.malka.androidappp.newPhase.presentation.addProduct.activity6

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.lifecycle.ViewModelProvider
import com.hbb20.CountryCodePicker
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.helper.widgets.searchdialog.SearchListItem
import com.malka.androidappp.newPhase.presentation.addProduct.AddProductObjectData
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.presentation.addProduct.ConfirmationAddProductActivity
import com.malka.androidappp.newPhase.presentation.addProduct.viewmodel.AddProductViewModel
import com.malka.androidappp.newPhase.presentation.dialogsShared.countryDialog.CountryDialog
import com.malka.androidappp.newPhase.presentation.dialogsShared.neighborhoodDialog.NeighborhoodDialog
import com.malka.androidappp.newPhase.presentation.dialogsShared.regionDialog.RegionDialog
import com.yariksoffice.lingver.Lingver
import kotlinx.android.synthetic.main.activity_list_details_add_product.*
import kotlinx.android.synthetic.main.toolbar_main.*


class ListingDetailsActivity : BaseActivity() {
    var selectedCountry: SearchListItem? = null
    var selectedRegion: SearchListItem? = null
    var selectedCity: SearchListItem? = null

    //    var selectedCountryId: Int = 0
//    var selectedRegionId: Int = 0
//    var selectedNeighborhoodId: Int = 0
    private lateinit var addProductViewModel: AddProductViewModel
    var isEdit: Boolean = false
    var isPhoneNumberValid: Boolean = false
    val configKey = "ShowProductQuantityInAddProduct"
    override fun onBackPressed() {
        if (isEdit) {
            startActivity(Intent(this, ConfirmationAddProductActivity::class.java))
            finish()
        } else {
            finish()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_details_add_product)
        toolbar_title.text = getString(R.string.item_details)
        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)
        setUpViewModel()
//        AddProductObjectData.productCondition = 0
//        AddProductObjectData.quantity = ""
        setViewClickListeners()
        setupCountryCodePiker()

        if (isEdit) {
            setData()
        }

//  if(!AddProductObjectData.brand_new_item.isEmpty()){
//            isEdit=true
//        }
//        ConstantObjects.countryList.filter {
//            it.id == ConstantObjects.defaltCountry
//        }.let {
//            if (it.size > 0) {
//                it.get(0).run{
//                    selectedCountry = SearchListItem(id,name)
//                    phone_number_edittext._setEndText(countryCode)
//                    countryContainer._setStartIconImage(flagimglink)
//                    countryContainer.text=it.get(0).name
//                }
//
//            }
//        }

        //   title_tv.setText(AddProductObjectData.productTitle)


//        if (isEdit){
//            item_description.setText( AddProductObjectData.item_description)
//            subtitle.setText(AddProductObjectData.subtitle)
//            title_tv.setText(AddProductObjectData.productTitle)
//            quantityavail.number = AddProductObjectData.quantity
//            countryContainer.setText(AddProductObjectData.country!!.title)
//            selectedCountry = AddProductObjectData.country
//            regionContainer.setText(AddProductObjectData.region!!.title)
//            selectedRegion = AddProductObjectData.region
//            neighborhoodContainer.setText(AddProductObjectData.city!!.title)
//            selectedCity = AddProductObjectData.city
//            if (AddProductObjectData.brand_new_item.equals(tv_New.text.toString())){
//                tv_New.performClick()
//            }else{
//                tv_used.performClick()
//            }
//
//            ConstantObjects.countryList.forEach {
//                if (AddProductObjectData.phone.startsWith(it.countryCode!!)){
//                    phone_number_edittext.text = AddProductObjectData.phone.replace(it.countryCode!!,"")
//                    phone_number_edittext._setEndText(it.countryCode)
//                }
//
//            }
//
//            btnotherr.setOnClickListener {
//                ListDetailsconfirmInput() }
//
//        }

    }

    private fun setUpViewModel() {
        addProductViewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
        addProductViewModel.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        }
        addProductViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.connectionError),
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        }
        addProductViewModel.errorResponseObserver.observe(this) {
            if (it.message != null && it.message != "") {
                HelpFunctions.ShowLongToast(
                    it.message!!,
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        }
        addProductViewModel.configurationRespObserver.observe(this) { configurationRespObserver ->
            if (configurationRespObserver.status_code == 200) {
                configurationRespObserver.configurationData?.let {
                    if (it.configValue == "1") {
                        containerQuantity.show()
                    } else {
                        AddProductObjectData.quantity = "1"
                        quantityavail.number = "1"
                        containerQuantity.hide()
                    }
                }
            }
        }

        addProductViewModel.getConfigurationResp(configKey)
    }

    private fun setData() {
        tvTitleAr.setText(AddProductObjectData.itemTitleAr)
        tvTitleEn.setText(AddProductObjectData.itemTitleEn)
        tvSubtitleAr.setText(AddProductObjectData.subtitleAr)
        tvSubtitleEn.setText(AddProductObjectData.subtitleEn)
        tvDescriptionAr.setText(AddProductObjectData.itemDescriptionAr)
        tvDescriptionEn.setText(AddProductObjectData.itemDescriptionEn)
        selectedCountry = AddProductObjectData.country
        selectedRegion = AddProductObjectData.region
        selectedCity = AddProductObjectData.city
        countryContainer.text = selectedCountry?.title ?: ""
        regionContainer.text = selectedRegion?.title ?: ""
        neighborhoodContainer.text = selectedCity?.title ?: ""
        countryCodePicker.setCountryForNameCode(AddProductObjectData.phoneCountryCode)
        etPhoneNumber.setText(
            AddProductObjectData.phone.replace(
                AddProductObjectData.phoneCountryCode,
                ""
            )
        )
        quantityavail.number = AddProductObjectData.quantity
        if (AddProductObjectData.productCondition == 2) {
            tv_New.isSelected = true
            tv_used.isSelected = false
            AddProductObjectData.productCondition = 2
        } else {
            tv_New.isSelected = false
            tv_used.isSelected = true
            AddProductObjectData.productCondition = 1
        }

    }

    private fun setupCountryCodePiker() {
        if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC) {
            countryCodePicker.changeDefaultLanguage(CountryCodePicker.Language.ARABIC)
        } else {
            countryCodePicker.changeDefaultLanguage(CountryCodePicker.Language.ENGLISH)
            //  etPhoneNumber.textAlignment=View.TEXT_ALIGNMENT_VIEW_START
        }
        countryCodePicker.registerCarrierNumberEditText(etPhoneNumber)
        countryCodePicker.setPhoneNumberValidityChangeListener { isValidNumber ->
            isPhoneNumberValid = isValidNumber
        }
        countryCodePicker.setOnCountryChangeListener {
            etPhoneNumber.text = Editable.Factory.getInstance().newEditable("")
        }
    }

    private fun setViewClickListeners() {
        countryContainer._setOnClickListener {
            openCountryDialog()
//            val list: ArrayList<SearchListItem> = ArrayList()
//            ConstantObjects.countryList.forEachIndexed { index, country ->
//                list.add(SearchListItem(country.id, country.name))
//            }
//            countryContainer.showSpinner(
//                this,
//                list,
//                getString(R.string.Select, getString(R.string.Country))
//            ) {
//                countryContainer.text = it.title
//                selectedCountry = it
//                ConstantObjects.countryList.filter {
//                    it.id == selectedCountry!!.id
//                }.let {
//                    if (it.size > 0) {
//                        phone_number_edittext._setEndText(it.get(0).countryCode)
//                        countryContainer._setStartIconImage(it.get(0).flagimglink)
//                    }
//                }
//                regionContainer.text = ""
//                selectedRegion = null
//
//                neighborhoodContainer.text = ""
//                selectedCity = null
//            }

        }
        regionContainer._setOnClickListener {
            if (selectedCountry == null) {
                showError(getString(R.string.Please_select, getString(R.string.Country)))
            } else {
                openRegionDialog()
            }
//            if (countryContainer.text.toString().isEmpty()) {
//                showError(getString(R.string.Please_select, getString(R.string.Country)))
//            } else {
//                CommonAPI().getRegion(selectedCountry!!.id, this) {
//                    val list: ArrayList<SearchListItem> = ArrayList()
//                    it.forEachIndexed { index, country ->
//                        list.add(SearchListItem(country.id, country.name))
//                    }
//                    regionContainer.showSpinner(
//                        this,
//                        list,
//                        getString(R.string.Select, getString(R.string.Region))
//                    ) {
//                        regionContainer.text = it.title
//                        selectedRegion = it
//
//
//                        neighborhoodContainer.text = ""
//                        selectedCity = null
//
//                    }
//                }
//            }

        }
        neighborhoodContainer._setOnClickListener {
            if (selectedRegion == null) {
                showError(getString(R.string.Please_select, getString(R.string.Region)))
            } else {
                openNeighborhoodDialog()
            }
//            if (regionContainer.text.toString().isEmpty()) {
//                showError(getString(R.string.Please_select, getString(R.string.Region)))
//            } else {
//                CommonAPI().getCity(selectedRegion!!.id,this) {
//                    val list: ArrayList<SearchListItem> = ArrayList()
//                    it.forEachIndexed { index, country ->
//                        list.add(SearchListItem(country.id, country.name))
//                    }
//                    neighborhoodContainer.showSpinner(
//                        this,
//                        list,
//                        getString(R.string.Select, getString(R.string.district))
//                    ) {
//                        neighborhoodContainer.text = it.title
//                        selectedCity = it
//                    }
//                }
//            }
        }


        btnotherr.setOnClickListener { listDetailsconfirmInput() }

        back_btn.setOnClickListener {
            onBackPressed()
        }

        tv_New.setOnClickListener {
            tv_New.isSelected = true
            tv_used.isSelected = false
            AddProductObjectData.brand_new_item = tv_New.text.toString()
            AddProductObjectData.productCondition = 2
        }

        tv_used.setOnClickListener {
            tv_New.isSelected = false
            tv_used.isSelected = true
            AddProductObjectData.brand_new_item = tv_used.text.toString()
            AddProductObjectData.productCondition = 1
        }

    }

    /**countries , region and Neighborhood Dialogs**/
    private fun openCountryDialog() {
        val countryDialog = CountryDialog(this, object : CountryDialog.GetSelectedCountry {
            override fun onSelectedCountry(id: Int, countryName: String, countryFlag: String?, countryCode: String?) {
                /**setCountryData*/
                /**setCountryData*/
                selectedCountry = SearchListItem(id, countryName)
                countryContainer.text = countryName.toString()
                countryContainer._setStartIconImage(countryFlag)
                /**resetRegion*/
                /**resetRegion*/
                selectedRegion = null
                regionContainer.text = null
                /**resetRegion*/
                /**resetRegion*/
                selectedCity = null
                neighborhoodContainer.text = null
            }
        })
        countryDialog.show()
    }

    private fun openRegionDialog() {
        val regionDialog =
            RegionDialog(this, selectedCountry!!.id, object : RegionDialog.GetSelectedRegion {
                override fun onSelectedRegion(id: Int, regionName: String) {
                    /**setRegionData*/
                    /**setRegionData*/
                    selectedRegion = SearchListItem(id, regionName)
                    regionContainer.text = regionName.toString()
                    /**resetNeighborhood*/
                    /**resetNeighborhood*/
                    selectedCity = null
                    neighborhoodContainer.text = null
                }
            })
        regionDialog.show()
    }

    private fun openNeighborhoodDialog() {
        val neighborhoodDialog = NeighborhoodDialog(
            this,
            selectedRegion!!.id,
            object : NeighborhoodDialog.GetSelectedNeighborhood {
                override fun onSelectedNeighborhood(id: Int, neighborhoodName: String) {
                    /**setNeighborhoodData*/
                    /**setNeighborhoodData*/
                    selectedCity = SearchListItem(id, neighborhoodName)
                    neighborhoodContainer.text = neighborhoodName.toString()
                }
            })
        neighborhoodDialog.show()
    }

    /***/

    fun listDetailsconfirmInput() {
        AddProductObjectData.quantity = quantityavail.number.toString()
        if (tvTitleAr.getText().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.item_title)))
        } else if (tvTitleEn.getText().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.item_title)))
        } else if (tvSubtitleAr.getText().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.sub_title)))
        } else if (tvSubtitleEn.getText().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.sub_title)))
        } else if (tvDescriptionAr.getText().toString().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.item_details)))
        } else if (tvDescriptionEn.getText().toString().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.item_details)))
        } else if (AddProductObjectData.productCondition == 0) {
            showError(getString(R.string.Please_select, getString(R.string.item_condition)))
        } else if (AddProductObjectData.quantity.isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.QuantityAvailable)))
        } else if (AddProductObjectData.quantity.toInt() <= 0) {
            showError(getString(R.string.Please_select, getString(R.string.QuantityAvailable)))
        } else if (countryContainer.text.toString().isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.selectCountry)))
        } else if (regionContainer.text.toString().isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.selectCountry)))
        } else if (neighborhoodContainer.text.toString().isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.district)))
        } else if (etPhoneNumber.text.toString().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.PhoneNumber)))
        } else if (!isPhoneNumberValid) {
            showError(getString(R.string.PleaseenteravalidPhoneNumber))
        } else {
            AddProductObjectData.itemTitleAr = tvTitleAr.getText().trim().toString()
            AddProductObjectData.itemTitleEn = tvTitleEn.getText().trim().toString()
            AddProductObjectData.subtitleAr = tvSubtitleAr.getText().trim().toString()
            AddProductObjectData.subtitleEn = tvSubtitleEn.getText().trim().toString()
            AddProductObjectData.itemDescriptionAr = tvDescriptionAr.text.trim().toString()
            AddProductObjectData.itemDescriptionEn = tvDescriptionEn.text.trim().toString()
            AddProductObjectData.country = selectedCountry
            AddProductObjectData.region = selectedRegion
            AddProductObjectData.city = selectedCity
            AddProductObjectData.phone = countryCodePicker.fullNumberWithPlus
            AddProductObjectData.phoneCountryCode = countryCodePicker.selectedCountryCodeWithPlus
            if (isEdit) {
                startActivity(Intent(this, ConfirmationAddProductActivity::class.java).apply {
                    finish()
                })
            } else {
                startActivity(Intent(this, PricingActivity::class.java).apply {
                })

            }
        }
    }
}