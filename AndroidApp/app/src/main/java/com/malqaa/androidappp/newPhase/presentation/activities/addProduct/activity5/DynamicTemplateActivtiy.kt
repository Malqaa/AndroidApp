package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.FragmentDynamicTemplateBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationItem
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationSentObject
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity6.ListingDetailsActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.viewmodel.AddProductViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DynamicTemplateActivity : BaseActivity<FragmentDynamicTemplateBinding>(),
    DynamicSpecificationsAdapter.OnChangeValueListener {

    private var dataList = ArrayList<DynamicSpecificationSentObject>()
    private var isEdit: Boolean = false
    private lateinit var addProductViewModel: AddProductViewModel
    private lateinit var dynamicSpecificationsAdapter: DynamicSpecificationsAdapter
    private var dynamicSpecificationsArrayList = ArrayList<DynamicSpecificationItem>()
    private var selectSpecificationsArrayList = ArrayList<DynamicSpecificationItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDynamicTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarMain.toolbarTitle.text = getString(R.string.item_details)

        setViewClickListeners()
        setDynamicListAdapter()
        setUpViewModel()

        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)
        addProductViewModel.getDynamicSpecification(AddProductObjectData.selectedCategoryId)
    }

    private fun setDataForUpdate() {
        if (isEdit) {
            selectSpecificationsArrayList = dynamicSpecificationsArrayList
            for (item in selectSpecificationsArrayList) {
                val existingSpec =
                    AddProductObjectData.productSpecificationList?.find { it.SpecificationId == item.id }
                existingSpec?.let {
                    item.setData(it)
                    item.subSpecifications?.find { sub -> sub.id == it.SpecificationId }?.apply {
                        isDataSelected = true
                    }
                }
            }
            dynamicSpecificationsAdapter.updateAdapter(selectSpecificationsArrayList)
        }
    }

    private fun setUpViewModel() {
        addProductViewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)

        addProductViewModel.apply {
            isLoading.observe(this@DynamicTemplateActivity) {
                binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            }
            isNetworkFail.observe(this@DynamicTemplateActivity) {
                HelpFunctions.ShowLongToast(
                    getString(if (it) R.string.connectionError else R.string.serverError),
                    this@DynamicTemplateActivity
                )
            }
            errorResponseObserver.observe(this@DynamicTemplateActivity) { response ->
                response?.let {
                    HelpFunctions.ShowLongToast(
                        it.message ?: getString(R.string.serverError),
                        this@DynamicTemplateActivity
                    )
                }
            }
            getDynamicSpecificationObserver.observe(this@DynamicTemplateActivity) { response ->
                response?.let {
                    if (it.status_code == 200 && !it.dynamicList.isNullOrEmpty()) {
                        dynamicSpecificationsArrayList.clear()
                        dynamicSpecificationsArrayList.addAll(it.dynamicList)
                        setDataForUpdate()
                        dynamicSpecificationsAdapter.updateAdapter(dynamicSpecificationsArrayList)
                    } else {
                        showNoSpecificationMessage()
                    }
                }
            }
        }
    }

    private fun setDynamicListAdapter() {
        dynamicSpecificationsAdapter =
            DynamicSpecificationsAdapter(dynamicSpecificationsArrayList, this)
        binding.rvDynamicList.apply {
            adapter = dynamicSpecificationsAdapter
            layoutManager = LinearLayoutManager(this@DynamicTemplateActivity)
        }
    }

    private fun setViewClickListeners() {
        binding.toolbarMain.backBtn.setOnClickListener { finish() }
        binding.btnDynamicNext.setOnClickListener { checkAllDataSet(dataList) }
    }

    private fun checkAllDataSet(data: ArrayList<DynamicSpecificationSentObject>) {
        lifecycleScope.launch(Dispatchers.IO) {
            var allValuesSet = true

            dynamicSpecificationsArrayList.forEach { item ->
                when (SpecificationType.fromType(item.type)) {
                    SpecificationType.DropdownList -> handleDropdownListType(item, data)
                    SpecificationType.Checkbox, SpecificationType.Radio ->
                        handleCheckboxRadioType(item, data)

                    SpecificationType.Number -> handleNumberType(item, data)
                    else -> handleOtherTypes(item, data)
                }

                if (item.isRequired && item.isValueMissing()) {
                    allValuesSet = false
                    // Exit the loop early if a required value is missing
                    return@forEach
                }
            }

            withContext(Dispatchers.Main) {
                if (allValuesSet) {
                    AddProductObjectData.productSpecificationList = null
                    AddProductObjectData.productSpecificationList = data
                    Log.i("TAG", "checkAllDataSet: $data")
                    goNextScreen(false)
                } else {
                    showToast(getString(R.string.enterAllSpecification))
                }
            }
        }
    }

    private fun handleDropdownListType(
        item: DynamicSpecificationItem,
        data: ArrayList<DynamicSpecificationSentObject>
    ) {
        val selectedAr = item.subSpecifications?.find { it.nameAr == item.valueArText }
        val selectedEn = item.subSpecifications?.find { it.nameEn == item.valueEnText }

        if (!item.valueArText.isNullOrBlank() && !item.valueEnText.isNullOrBlank()) {
            data.add(
                DynamicSpecificationSentObject(
                    HeaderSpeAr = item.nameAr.orEmpty(),
                    HeaderSpeEn = item.nameEn.orEmpty(),
                    ValueSpeAr = selectedAr?.id.toString(),
                    ValueSpeEn = selectedEn?.id.toString(),
                    Type = item.type,
                    SpecificationId = item.id
                )
            )
        }
    }

    private fun handleCheckboxRadioType(
        item: DynamicSpecificationItem,
        data: ArrayList<DynamicSpecificationSentObject>
    ) {
        val selectedSubs = item.subSpecifications?.filter { it.isDataSelected }

        selectedSubs?.forEach {
            data.add(
                DynamicSpecificationSentObject(
                    HeaderSpeAr = item.nameAr.orEmpty(),
                    HeaderSpeEn = item.nameEn.orEmpty(),
                    ValueSpeAr = it.id.toString(),
                    ValueSpeEn = it.id.toString(),
                    Type = item.type,
                    SpecificationId = item.id
                )
            )
        }
    }

    private fun handleNumberType(
        item: DynamicSpecificationItem,
        data: ArrayList<DynamicSpecificationSentObject>
    ) {
        if (ConstantObjects.currentLanguage == "ar" && item.valueArText.isNullOrBlank()) {
            return
        } else if (ConstantObjects.currentLanguage == "en" && item.valueEnText.isNullOrBlank()) {
            return
        }

        data.add(
            DynamicSpecificationSentObject(
                HeaderSpeAr = item.nameAr.orEmpty(),
                HeaderSpeEn = item.nameEn.orEmpty(),
                ValueSpeAr = item.valueArText.orEmpty(),
                ValueSpeEn = item.valueEnText.orEmpty(),
                Type = item.type,
                SpecificationId = item.id
            )
        )
    }

    private fun handleOtherTypes(
        item: DynamicSpecificationItem,
        data: ArrayList<DynamicSpecificationSentObject>
    ) {
        if (item.isRequired && item.valueArText.isNullOrBlank() || item.valueEnText.isNullOrBlank()) return

        data.add(
            DynamicSpecificationSentObject(
                HeaderSpeAr = item.nameAr.orEmpty(),
                HeaderSpeEn = item.nameEn.orEmpty(),
                ValueSpeAr = item.valueArText.orEmpty(),
                ValueSpeEn = item.valueEnText.orEmpty(),
                Type = item.type,
                SpecificationId = item.id
            )
        )
    }

    private fun goNextScreen(isFinished: Boolean) {
        val intent = Intent(this, ListingDetailsActivity::class.java).apply {
            putExtra(ConstantObjects.isEditKey, isEdit)
        }
        startActivity(intent)
        finish()
    }

    private fun showNoSpecificationMessage() {
        showToast(getString(R.string.noSpecificationFound))
        goNextScreen(true)
    }

    private fun showToast(message: String) {
        runOnUiThread {
            HelpFunctions.ShowLongToast(message, this)
        }
    }

    override fun setOnTextBoxTextChangeAR(value: String, position: Int) {
        dynamicSpecificationsArrayList[position].valueArText = value
    }

    override fun setOnTextBoxTextChangeEN(value: String, position: Int) {
        dynamicSpecificationsArrayList[position].valueEnText = value
    }

    override fun setOnSpinnerListSelected(mainAttributesPosition: Int, spinnerPosition: Int) {
        dynamicSpecificationsArrayList[mainAttributesPosition].apply {
            valueArText = subSpecifications?.get(spinnerPosition)?.nameAr.orEmpty()
            valueEnText = subSpecifications?.get(spinnerPosition)?.nameEn.orEmpty()
        }
    }

    override fun onRadioItemSelected(position: Int, selectedId: Int) {
        dynamicSpecificationsArrayList[position].subSpecifications?.forEach {
            it.isDataSelected = it.id == selectedId
        }
    }

    override fun onCheckboxItemChecked(position: Int, checkboxId: Int, isChecked: Boolean) {
        dynamicSpecificationsArrayList[position].subSpecifications?.forEach {
            // If the checkbox ID matches, update its state
            if (it.id == checkboxId) {
                it.isDataSelected = isChecked
            }
        }
    }
}

// Helper extension function to check if required value is missing based on the current language
private fun DynamicSpecificationItem.isValueMissing(): Boolean {
    return if (ConstantObjects.currentLanguage == "ar") valueArText.isNullOrBlank()
    else valueEnText.isNullOrBlank()
}
