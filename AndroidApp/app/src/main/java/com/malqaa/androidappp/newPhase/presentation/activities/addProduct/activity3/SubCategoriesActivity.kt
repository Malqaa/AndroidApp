package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity3

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity4.AddPhotoActivity
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity1.ListCategoryViewModel
import kotlinx.android.synthetic.main.fragment_sub_categories.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SubCategoriesActivity : BaseActivity(), AdapterSubCategories.OnItemClickListener {

    var categoryid: Int = 0
    var categoryName: String = ""

    var allCategoryList: ArrayList<Category>? = null
    private var listCategoryViewModel: ListCategoryViewModel?=null
    var lastCategoryId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_sub_categories)
        toolbar_title.text = getString(R.string.subsection)
        back_btn.setOnClickListener {
            finish()
        }
        allCategoryList= arrayListOf()
        categoryid = intent.getIntExtra(ConstantObjects.categoryIdKey, 0)
        categoryName = intent.getStringExtra(ConstantObjects.categoryName).toString()
        setUpViewModel()

        lastCategoryId = AddProductObjectData.selectedCategoryId
        listCategoryViewModel!!.getSubCategoriesByCategoryID(categoryid)
    }

    private fun setUpViewModel() {
        val subCategoryAdapter=AdapterSubCategories(allCategoryList?: arrayListOf(), this@SubCategoriesActivity)
        allCategoriesRecyclerView.adapter =subCategoryAdapter
        listCategoryViewModel = ViewModelProvider(this).get(ListCategoryViewModel::class.java)
        listCategoryViewModel!!.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
        }
        listCategoryViewModel!!.isNetworkFail.observe(this) {
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
        listCategoryViewModel!!.errorResponseObserver.observe(this) {
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
        listCategoryViewModel!!.categoryListObserver.observe(this) { categoryListObserver ->
            if (categoryListObserver.status_code == 200) {
                allCategoryList = arrayListOf()
                lifecycleScope.launch(Dispatchers.IO) {
                     if (!categoryListObserver.categoryList.isNullOrEmpty()) {
                        for(i in categoryListObserver.categoryList){
                            allCategoryList?.add(i)

                        }
                    } else {
                         lastCategoryId =   0
                    }
                    if (lastCategoryId != 0)
                        subCategoryAdapter.updateAdapter(allCategoryList?: arrayListOf())
                    else {
                        if (allCategoryList?.isEmpty() == true) {
                            goNextScreen()
                            finish()
                        }
                    }


                }
            } else {
//                HelpFunctions.ShowLongToast(
//                    getString(R.string.noSubCategoryFound),
//                    this
//                )
            }
        }
    }

    override fun OnItemClickHandler(position: Int) {
        super.OnItemClickHandler(position)
        AddProductObjectData.selectedCategoryId = allCategoryList?.get(position)?.id?:0
        AddProductObjectData.selectedCategoryName == allCategoryList!![position].name

        if (lastCategoryId != 0) {
            listCategoryViewModel!!.getSubCategoriesByCategoryID(allCategoryList?.get(position)?.id?:0)
        } else {
            goNextScreen()

        }
    }


    private fun goNextScreen() {
        startActivity(Intent(this, AddPhotoActivity::class.java).apply {
            putExtra(ConstantObjects.isEditKey, intent.getBooleanExtra(ConstantObjects.isEditKey, false))
        })

    }
    override fun onDestroy() {
        super.onDestroy()
        listCategoryViewModel?.closeAllCall()
        listCategoryViewModel = null
        allCategoryList =null

    }

}