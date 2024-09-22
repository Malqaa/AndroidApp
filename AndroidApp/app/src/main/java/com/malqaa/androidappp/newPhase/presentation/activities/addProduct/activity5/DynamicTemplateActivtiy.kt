package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity5

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationItem
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationSentObject
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.SubSpecificationItem
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity6.ListingDetailsActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.viewmodel.AddProductViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
import kotlinx.android.synthetic.main.fragment_dynamic_template.btn_dynamic_next
import kotlinx.android.synthetic.main.fragment_dynamic_template.progressBar
import kotlinx.android.synthetic.main.fragment_dynamic_template.rvDynamicList
import kotlinx.android.synthetic.main.toolbar_main.back_btn
import kotlinx.android.synthetic.main.toolbar_main.toolbar_title
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DynamicTemplateActivtiy : BaseActivity(), DynamicSpecificationsAdapter.OnChangeValueListener {

    private var dataList: ArrayList<DynamicSpecificationSentObject>? = null
    private var isEdit: Boolean = false
    private lateinit var addProductViewModel: AddProductViewModel
    private lateinit var dynamicSpecificationsAdapter: DynamicSpecificationsAdapter
    private var dynamicSpecificationsArrayList: ArrayList<DynamicSpecificationItem> = ArrayList()
    private var selectSpecificationsArrayList: ArrayList<DynamicSpecificationItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_dynamic_template)
        toolbar_title.text = getString(R.string.item_details)
        setViewClickListeners()
        setDynamicListAdapter()
        setUpViewModel()
        dataList = arrayListOf()
        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)

        addProductViewModel.getDynamicSpecification(AddProductObjectData.selectedCategoryId)
    }

    private fun setDataForUpdate() {
        if (isEdit) {
            selectSpecificationsArrayList = dynamicSpecificationsArrayList
            for (item in selectSpecificationsArrayList) {
                if (item.type == 2 || item.type == 3 || item.type == 4) {
                    val obj =
                        AddProductObjectData.productSpecificationList!!.find { it.SpecificationId == item.id }
                    if (obj != null) {
                        item.setData(obj)
                        item.subSpecifications?.add(
                            SubSpecificationItem(
                                id = obj.SpecificationId,
                                nameAr = obj.ValueSpeAr,
                                nameEn = obj.ValueSpeEn,
                                isDataSelected = true
                            )
                        )
                    }
                } else if (item.type == 5 || item.type == 6) {

                } else {
                    item.subSpecifications?.forEach {
                        for (j in AddProductObjectData.productSpecificationList!!) {
                            if (j.ValueSpeAr == it.id.toString()) {
                                item.setData(j)
                                it.isDataSelected = true
                            }
                        }
                    }
                }

            }
            dynamicSpecificationsAdapter.updateAdapter(selectSpecificationsArrayList)
        }

    }

    private fun setUpViewModel() {
        addProductViewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
        addProductViewModel.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
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
        addProductViewModel.getDynamicSpecificationObserver.observe(this) { dynamicSpecificationResp ->
            if (dynamicSpecificationResp.status_code == 200) {
                if (dynamicSpecificationResp.dynamicList != null && dynamicSpecificationResp.dynamicList.isNotEmpty()) {
                    dynamicSpecificationsArrayList.clear()
                    dynamicSpecificationsArrayList.addAll(dynamicSpecificationResp.dynamicList)

                    setDataForUpdate()
                    dynamicSpecificationsAdapter.updateAdapter(dynamicSpecificationsArrayList)
                } else {
                    goNextScreen(true)
                    HelpFunctions.ShowLongToast(
                        getString(R.string.noSpecificationFound),
                        this
                    )
                }
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.noSpecificationFound),
                    this
                )
            }
        }
    }

    private fun setDynamicListAdapter() {
        dynamicSpecificationsAdapter =
            DynamicSpecificationsAdapter(dynamicSpecificationsArrayList, this)
        rvDynamicList.apply {
            adapter = dynamicSpecificationsAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    private fun setViewClickListeners() {
        back_btn.setOnClickListener {
            finish()
        }

        btn_dynamic_next.setOnClickListener {
            checkAllDataSet(dataList!!)
        }
    }

    private fun checkAllDataSet(data: ArrayList<DynamicSpecificationSentObject>) {
        lifecycleScope.launch(Dispatchers.IO) {
            var allValuesSet = true

            dynamicSpecificationsArrayList.forEach { dynamicSpecificationItem ->

                val supIdAr =
                    dynamicSpecificationItem.subSpecifications?.find { it.nameAr == dynamicSpecificationItem.valueArText }
                val supIdEn =
                    dynamicSpecificationItem.subSpecifications?.find { it.nameEn == dynamicSpecificationItem.valueEnText }

                if (dynamicSpecificationItem.type == 1) {
                    if (!dynamicSpecificationItem.subSpecifications.isNullOrEmpty()) {
                        if (dynamicSpecificationItem.valueArText.isNullOrEmpty()) {
                            allValuesSet = false
                        } else {
                            val newObject = DynamicSpecificationSentObject(
                                HeaderSpeAr = dynamicSpecificationItem.nameAr ?: "",
                                HeaderSpeEn = dynamicSpecificationItem.nameEn ?: "",
                                ValueSpeAr = supIdAr?.id?.toString() ?: "",
                                ValueSpeEn = supIdEn?.id?.toString() ?: "",
                                Type = dynamicSpecificationItem.type,
                                SpecificationId = dynamicSpecificationItem.id
                            )

                            if (!data.any { it.SpecificationId == newObject.SpecificationId }) {
                                data.add(newObject)
                            }
                        }
                    }
                } else if (dynamicSpecificationItem.type == 5 || dynamicSpecificationItem.type == 6) {
                    val required = dynamicSpecificationItem.isRequired
                    if (required) {
                        val supIdAr =
                            dynamicSpecificationItem.subSpecifications?.find { dynamicSpecificationItem.valueBoolean }
                        if (supIdAr == null) {
                            allValuesSet = false
                            if (dynamicSpecificationItem.isRequired) {
                                runOnUiThread {
                                    HelpFunctions.ShowLongToast(
                                        getString(R.string.enterAllSpecificaiton),
                                        this@DynamicTemplateActivtiy
                                    )
                                }
                            }
                        } else {
                            val newObject = DynamicSpecificationSentObject(
                                HeaderSpeAr = dynamicSpecificationItem.nameAr ?: "",
                                HeaderSpeEn = dynamicSpecificationItem.nameEn ?: "",
                                ValueSpeAr = supIdAr.id.toString(),
                                ValueSpeEn = supIdEn?.id?.toString() ?: "",
                                Type = dynamicSpecificationItem.type,
                                SpecificationId = dynamicSpecificationItem.id
                            )

                            if (!data.any { it.SpecificationId == newObject.SpecificationId }) {
                                data.add(newObject)
                            }
                        }
                    }
                } else if (dynamicSpecificationItem.type == 4) {
                    if (dynamicSpecificationItem.isRequired && dynamicSpecificationItem.valueArText.isNullOrEmpty()) {
                        allValuesSet = false
                    } else {
                        val newObject = DynamicSpecificationSentObject(
                            HeaderSpeAr = dynamicSpecificationItem.nameAr ?: "",
                            HeaderSpeEn = dynamicSpecificationItem.nameAr ?: "",
                            ValueSpeAr = dynamicSpecificationItem.valueArText ?: "",
                            Type = dynamicSpecificationItem.type,
                            ValueSpeEn = dynamicSpecificationItem.valueArText ?: "",
                            SpecificationId = dynamicSpecificationItem.id
                        )

                        if (!data.any { it.SpecificationId == newObject.SpecificationId }) {
                            data.add(newObject)
                        }
                    }
                } else {
                    if (dynamicSpecificationItem.isRequired && dynamicSpecificationItem.valueArText.isNullOrEmpty()) {
                        allValuesSet = false
                    } else {
                        val newObject = DynamicSpecificationSentObject(
                            HeaderSpeAr = dynamicSpecificationItem.nameAr ?: "",
                            HeaderSpeEn = dynamicSpecificationItem.nameEn ?: "",
                            ValueSpeAr = dynamicSpecificationItem.valueArText ?: "",
                            Type = dynamicSpecificationItem.type,
                            ValueSpeEn = dynamicSpecificationItem.valueEnText ?: "",
                            SpecificationId = dynamicSpecificationItem.id
                        )

                        if (!data.any { it.SpecificationId == newObject.SpecificationId }) {
                            data.add(newObject)
                        }
                    }
                }
            }

            withContext(Dispatchers.Main) {
                if (allValuesSet) {
                    AddProductObjectData.productSpecificationList = data
                    goNextScreen(false)
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.enterAllSpecificaiton),
                        this@DynamicTemplateActivtiy
                    )
                }
            }
        }
    }

    private fun goNextScreen(isFinished: Boolean) {
        if (isFinished) {
            startActivity(Intent(this, ListingDetailsActivity::class.java).apply {
                putExtra(
                    ConstantObjects.isEditKey,
                    intent.getBooleanExtra(ConstantObjects.isEditKey, false)
                )

            })
            finish()
        } else {
            startActivity(Intent(this, ListingDetailsActivity::class.java).apply {
                putExtra(
                    ConstantObjects.isEditKey,
                    intent.getBooleanExtra(ConstantObjects.isEditKey, false)
                )
            })
            finish()
        }

    }

    override fun setOnTextBoxTextChangeAR(value: String, position: Int) {
        dynamicSpecificationsArrayList[position].valueArText = value
    }

    override fun setOnTextBoxTextChangeEN(value: String, position: Int) {
        dynamicSpecificationsArrayList[position].valueEnText = value
    }

    override fun setCheckClicked(position: Int) {
        dynamicSpecificationsArrayList[position].valueBoolean =
            dynamicSpecificationsArrayList[position].valueBoolean
    }

    override fun setOnSpinnerListSelected(mainAttributesPosition: Int, spinnerPosition: Int) {
        dynamicSpecificationsArrayList[mainAttributesPosition].valueArText =
            dynamicSpecificationsArrayList[mainAttributesPosition].subSpecifications?.get(
                spinnerPosition
            )?.nameAr ?: ""
        dynamicSpecificationsArrayList[mainAttributesPosition].valueEnText =
            dynamicSpecificationsArrayList[mainAttributesPosition].subSpecifications?.get(
                spinnerPosition
            )?.nameEn ?: ""
    }

}