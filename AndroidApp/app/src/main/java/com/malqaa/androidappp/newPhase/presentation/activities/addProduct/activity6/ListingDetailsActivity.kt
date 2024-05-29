package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity6

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.lifecycle.ViewModelProvider
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.ConfirmationAddProductActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.viewmodel.AddProductViewModel
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.countryDialog.CountryDialog
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.neighborhoodDialog.NeighborhoodDialog
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.regionDialog.RegionDialog
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.helper.widgets.searchdialog.SearchListItem
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import kotlinx.android.synthetic.main.activity_list_details_add_product.*
import kotlinx.android.synthetic.main.toolbar_main.*


class ListingDetailsActivity : BaseActivity() {
    var selectedCountry: SearchListItem? = null
    var selectedRegion: SearchListItem? = null
    var selectedCity: SearchListItem? = null

    private var addProductViewModel: AddProductViewModel? = null
    private var isEdit: Boolean = false
    private val configKey = "ShowProductQuantityInAddProduct"
    override fun onBackPressed() {
        if (isEdit) {
            startActivity(Intent(this, ConfirmationAddProductActivity::class.java).apply {
                putExtra("whereCome", "Add")
            })
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
        // setupCountryCodePiker()

        if (isEdit) {
            setData()
        }

    }

    private fun setUpViewModel() {
        addProductViewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
        addProductViewModel!!.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        }
        addProductViewModel!!.isNetworkFail.observe(this) {
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
        addProductViewModel!!.errorResponseObserver.observe(this) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
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
        }
        addProductViewModel!!.configurationRespObserver.observe(this) { configurationRespObserver ->
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

        addProductViewModel!!.getConfigurationResp(configKey)
    }

    private fun setData() {
        tvTitleAr.setText(AddProductObjectData.itemTitleAr)
        tvTitleEn.setText(AddProductObjectData.itemTitleEn)
        if (ConstantObjects.isModify)
            tvSubtitleAr.isEnabled = (AddProductObjectData.subtitleAr != "")

        if (ConstantObjects.isModify)
            tvSubtitleEn.isEnabled = (AddProductObjectData.subtitleEn != "")


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
        //countryCodePicker.setCountryForNameCode(AddProductObjectData.phoneCountryCode)
        etPhoneNumber.setText(AddProductObjectData.phone)
        quantityavail.number = AddProductObjectData.quantity
        if (AddProductObjectData.productCondition == 2) {
            tv_New.isSelected = true
            tv_used.isSelected = false
//            AddProductObjectData.productCondition = 1
        } else if (AddProductObjectData.productCondition == 1) {
            tv_New.isSelected = false
            tv_used.isSelected = true
//            AddProductObjectData.productCondition = 2

        }

    }

//    private fun setupCountryCodePiker() {
//        if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC) {
//            countryCodePicker.changeDefaultLanguage(CountryCodePicker.Language.ARABIC)
//        } else {
//            countryCodePicker.changeDefaultLanguage(CountryCodePicker.Language.ENGLISH)
//            //  etPhoneNumber.textAlignment=View.TEXT_ALIGNMENT_VIEW_START
//        }
//        countryCodePicker.registerCarrierNumberEditText(etPhoneNumber)
//        countryCodePicker.setPhoneNumberValidityChangeListener { isValidNumber ->
//            isPhoneNumberValid = isValidNumber
//        }
//        countryCodePicker.setOnCountryChangeListener {
//            etPhoneNumber.text = Editable.Factory.getInstance().newEditable("")
//        }
//    }

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
        switchInfoEn.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                txtEdit.visibility = View.VISIBLE
                viewEnglishInfo.visibility = View.VISIBLE
            } else {
                txtEdit.visibility = View.GONE
                viewEnglishInfo.visibility = View.GONE
            }
        })
        box_value.setOnClickListener {
            if (switchInfoEn.isChecked) {
                switchInfoEn.isChecked = false
                viewEnglishInfo.visibility = View.GONE
            } else {
                switchInfoEn.isChecked = true
                viewEnglishInfo.visibility = View.VISIBLE
            }
        }
        boxEnglishInfo.setOnClickListener {
            if (switchInfoEn.isChecked) {
                switchInfoEn.isChecked = false
                viewEnglishInfo.visibility = View.GONE
            } else {
                switchInfoEn.isChecked = true
                viewEnglishInfo.visibility = View.VISIBLE
            }
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


        btnotherr.setOnClickListener { listDetailsConfirmInput() }

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
            override fun onSelectedCountry(
                id: Int,
                countryName: String,
                countryFlag: String?,
                countryCode: String?
            ) {
                /**setCountryData*/
                /**setCountryData*/
                selectedCountry = SearchListItem(id, countryName)
                countryContainer.text = countryName
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
                    regionContainer.text = regionName
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
                    neighborhoodContainer.text = neighborhoodName
                }
            })
        neighborhoodDialog.show()
    }

    /***/

    private fun listDetailsConfirmInput() {
        AddProductObjectData.quantity = quantityavail.number
        if (tvTitleAr.getText().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.item_title)))
        }

//        else if (tvSubtitleAr.getText().isEmpty()) {
//            showError(getString(R.string.Please_enter, getString(R.string.sub_title)))
//        } else if (tvSubtitleEn.getText().isEmpty()) {
//            showError(getString(R.string.Please_enter, getString(R.string.sub_title)))
//        }
//        else if (tvDescriptionAr.getText().toString().isEmpty()) {
//            showError(getString(R.string.Please_enter, getString(R.string.item_details)))
//        } else if (tvDescriptionEn.getText().toString().isEmpty()) {
//            showError(getString(R.string.Please_enter, getString(R.string.item_details)))
//        }

        else if (AddProductObjectData.productCondition == 0) {
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
        }


//        else if (etPhoneNumber.text.toString().isEmpty()) {
//            showError(getString(R.string.Please_enter, getString(R.string.PhoneNumber)))
//        }
//        else if (etPhoneNumber.text.toString().trim().isEmpty()) {
//            showError(getString(R.string.PleaseenteravalidPhoneNumber))
//        }

        else if (switchInfoEn.isChecked){
            if (tvTitleEn.getText().isEmpty()) {
                showError(getString(R.string.Please_enter, getString(R.string.item_title)))
            }
            else {
                checkAllRight()

            }
        }else{
            checkAllRight()
        }


    }

    fun checkAllRight(){
        AddProductObjectData.itemTitleAr = tvTitleAr.text.toString()
        AddProductObjectData.itemTitleEn = tvTitleEn.getText().trim()
        AddProductObjectData.subtitleAr = tvSubtitleAr.text.toString()
        AddProductObjectData.subtitleEn = tvSubtitleEn.text.toString()
        AddProductObjectData.itemDescriptionAr = tvDescriptionAr.text.trim().toString()
        AddProductObjectData.itemDescriptionEn = tvDescriptionEn.text.trim().toString()
        AddProductObjectData.country = selectedCountry
        AddProductObjectData.region = selectedRegion
        AddProductObjectData.city = selectedCity
        AddProductObjectData.phone = etPhoneNumber.text.toString().trim()
        // AddProductObjectData.phoneCountryCode = countryCodePicker.selectedCountryCodeWithPlus
        if (isEdit) {
            startActivity(Intent(this, ConfirmationAddProductActivity::class.java).apply {
                putExtra("whereCome", "Add")
                putExtra(
                    ConstantObjects.isEditKey,
                    intent.getBooleanExtra(ConstantObjects.isEditKey, false)
                )
                finish()
            })
        } else {
            startActivity(Intent(this, PricingActivity::class.java).apply {
                putExtra(
                    ConstantObjects.isEditKey,
                    intent.getBooleanExtra(ConstantObjects.isEditKey, false)
                )

            })

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addProductViewModel?.baseCancel()
        addProductViewModel = null
    }
}