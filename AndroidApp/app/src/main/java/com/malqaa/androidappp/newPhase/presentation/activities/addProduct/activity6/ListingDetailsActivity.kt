package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity6

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.lifecycle.ViewModelProvider
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityListDetailsAddProductBinding
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


class ListingDetailsActivity : BaseActivity<ActivityListDetailsAddProductBinding>() {
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
        // Initialize view binding
        binding = ActivityListDetailsAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarMain.toolbarTitle.text = getString(R.string.item_details)
        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)
        setUpViewModel()
        setViewClickListeners()

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
        addProductViewModel!!.configurationRespDidNotReceive.observe(this) { configurationRespObserver ->
            if (configurationRespObserver.status_code == 200) {
                configurationRespObserver.configurationData?.let {
                    if (it.configValue == "1") {
                        binding.containerQuantity.show()
                    } else {
                        AddProductObjectData.quantity = "1"
                        binding.quantityavail.number = "1"
                        binding.containerQuantity.hide()
                    }
                }
            }
        }

        addProductViewModel!!.getConfigurationResp(configKey)
    }

    private fun setData() {
        binding.tvTitleAr.setText(AddProductObjectData.itemTitleAr)
        binding.tvTitleEn.setText(AddProductObjectData.itemTitleEn)
        if (ConstantObjects.isModify)
            binding.tvSubtitleAr.isEnabled = (AddProductObjectData.subtitleAr != "")

        if (ConstantObjects.isModify)
            binding.tvSubtitleEn.isEnabled = (AddProductObjectData.subtitleEn != "")


        binding.tvSubtitleAr.setText(AddProductObjectData.subtitleAr)
        binding.tvSubtitleEn.setText(AddProductObjectData.subtitleEn)
        binding.tvDescriptionAr.setText(AddProductObjectData.itemDescriptionAr)
        binding.tvDescriptionEn.setText(AddProductObjectData.itemDescriptionEn)
        selectedCountry = AddProductObjectData.country
        selectedRegion = AddProductObjectData.region
        selectedCity = AddProductObjectData.city
        binding.countryContainer.text = selectedCountry?.title ?: ""
        binding.regionContainer.text = selectedRegion?.title ?: ""
        binding.neighborhoodContainer.text = selectedCity?.title ?: ""
        binding.etPhoneNumber.setText(AddProductObjectData.phone)
        binding.quantityavail.number = AddProductObjectData.quantity
        if (AddProductObjectData.productCondition == 2) {
            binding.tvNew.isSelected = true
            binding.tvUsed.isSelected = false
        } else if (AddProductObjectData.productCondition == 1) {
            binding.tvNew.isSelected = false
            binding.tvUsed.isSelected = true
        }

    }

    private fun setViewClickListeners() {
        binding.countryContainer._setOnClickListener {
            openCountryDialog()
        }
        binding.switchInfoEn.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.viewEnglishInfo.visibility = View.VISIBLE
            } else {
                binding.viewEnglishInfo.visibility = View.GONE
            }
        })
        binding.boxValue.setOnClickListener {
            if (binding.switchInfoEn.isChecked) {
                binding.switchInfoEn.isChecked = false
                binding.viewEnglishInfo.visibility = View.GONE
            } else {
                binding.switchInfoEn.isChecked = true
                binding.viewEnglishInfo.visibility = View.VISIBLE
            }
        }
        binding.boxEnglishInfo.setOnClickListener {
            if (binding.switchInfoEn.isChecked) {
                binding.switchInfoEn.isChecked = false
                binding.viewEnglishInfo.visibility = View.GONE
            } else {
                binding.switchInfoEn.isChecked = true
                binding.viewEnglishInfo.visibility = View.VISIBLE
            }
        }
        binding.regionContainer._setOnClickListener {
            if (selectedCountry == null) {
                showError(getString(R.string.Please_select, getString(R.string.Country)))
            } else {
                openRegionDialog()
            }
        }
        binding.neighborhoodContainer._setOnClickListener {
            if (selectedRegion == null) {
                showError(getString(R.string.Please_select, getString(R.string.Region)))
            } else {
                openNeighborhoodDialog()
            }
        }


        binding.btnotherr.setOnClickListener { listDetailsConfirmInput() }

        binding.toolbarMain.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.tvNew.setOnClickListener {
            binding.tvNew.isSelected = true
            binding.tvUsed.isSelected = false
            AddProductObjectData.brand_new_item = binding.tvNew.text.toString()
            AddProductObjectData.productCondition = 2
        }

        binding.tvUsed.setOnClickListener {
            binding.tvNew.isSelected = false
            binding.tvUsed.isSelected = true
            AddProductObjectData.brand_new_item = binding.tvUsed.text.toString()
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
                binding.countryContainer.text = countryName
                binding.countryContainer._setStartIconImage(countryFlag)
                /**resetRegion*/
                /**resetRegion*/
                selectedRegion = null
                binding.regionContainer.text = null
                /**resetRegion*/
                /**resetRegion*/
                selectedCity = null
                binding.neighborhoodContainer.text = null
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
                    binding.regionContainer.text = regionName
                    /**resetNeighborhood*/
                    /**resetNeighborhood*/
                    selectedCity = null
                    binding.neighborhoodContainer.text = null
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
                    binding.neighborhoodContainer.text = neighborhoodName
                }
            })
        neighborhoodDialog.show()
    }

    /***/

    private fun listDetailsConfirmInput() {
        AddProductObjectData.quantity = binding.quantityavail.number
        if (binding.tvTitleAr.getText().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.item_title)))
        }

        else if (AddProductObjectData.productCondition == 0) {
            showError(getString(R.string.Please_select, getString(R.string.item_condition)))
        } else if (AddProductObjectData.quantity.isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.QuantityAvailable)))
        } else if (AddProductObjectData.quantity.toInt() <= 0) {
            showError(getString(R.string.Please_select, getString(R.string.QuantityAvailable)))
        } else if (binding.countryContainer.text.toString().isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.selectCountry)))
        } else if (binding.regionContainer.text.toString().isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.selectCountry)))
        } else if (binding.neighborhoodContainer.text.toString().isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.district)))
        }

        else if (binding.switchInfoEn.isChecked) {
            if (binding.tvTitleEn.getText().isEmpty()) {
                showError(getString(R.string.Please_enter, getString(R.string.item_title)))
            } else {
                checkAllRight()

            }
        } else {
            checkAllRight()
        }


    }

    fun checkAllRight() {
        AddProductObjectData.itemTitleAr = binding.tvTitleAr.text.toString()
        AddProductObjectData.subtitleAr = binding.tvSubtitleAr.text.toString()
        AddProductObjectData.itemDescriptionAr = binding.tvDescriptionAr.text.trim().toString()
        if (!binding.switchInfoEn.isChecked) {
            AddProductObjectData.itemTitleEn = binding.tvTitleAr.text.toString()
            AddProductObjectData.subtitleEn =binding.tvSubtitleAr.text.toString()
            AddProductObjectData.itemDescriptionEn = binding.tvDescriptionAr.text.trim().toString()
        } else {
            AddProductObjectData.itemTitleEn = binding.tvTitleEn.getText().toString()
            AddProductObjectData.subtitleEn = binding.tvSubtitleEn.text.toString()
            AddProductObjectData.itemDescriptionEn = binding.tvDescriptionEn.text.trim().toString()
        }


        AddProductObjectData.country = selectedCountry
        AddProductObjectData.region = selectedRegion
        AddProductObjectData.city = selectedCity
        AddProductObjectData.phone = binding.etPhoneNumber.text.toString().trim()
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