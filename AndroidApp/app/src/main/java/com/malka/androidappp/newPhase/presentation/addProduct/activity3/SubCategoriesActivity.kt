package com.malka.androidappp.newPhase.presentation.addProduct.activity3

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.presentation.addProduct.activity4.AddPhotoActivity
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.presentation.addProduct.AddProductObjectData
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category
import com.malka.androidappp.newPhase.presentation.addProduct.activity1.ListCategoryViewModel
import com.malka.androidappp.newPhase.presentation.addProduct.viewmodel.AddProductViewModel
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
                    lastCategoryId = if (!categoryListObserver.categoryList.isNullOrEmpty()) {
                        allCategoryList?.add(categoryListObserver.categoryList[0])
                        categoryListObserver.categoryList[0].id
                    } else {
                        0
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
                HelpFunctions.ShowLongToast(
                    getString(R.string.noSubCategoryFound),
                    this
                )
            }
        }
    }

//    private fun getSubCategoriesByTemplateID(categoryId: String) {
//        HelpFunctions.startProgressBar(this)
//        try {
//            val malqaa: MalqaApiService =
//                getRetrofitBuilder()
//
//            val call: Call<GeneralResponses> =
//                malqaa.GetSubCategoryByMainCategory(categoryId)
//
//            call.enqueue(object : Callback<GeneralResponses> {
//                @SuppressLint("UseRequireInsteadOfGet")
//                override fun onResponse(
//                    call: Call<GeneralResponses>,
//                    response: Response<GeneralResponses>
//                ) {
//
//                    if (response.isSuccessful) {
//
//                        response.body()?.run {
//                            if (status_code == 200) {
//                                allCategoryList = Gson().fromJson(
//                                    Gson().toJson(data),
//                                    object : TypeToken<ArrayList<Category>>() {}.type
//                                )
//
//                                if (allCategoryList.count() > 0) {
//                                    allCategoriesRecyclerView.adapter =
//                                        AdapterSubCategories(
//                                            allCategoryList,
//                                            this@SubCategoriesActivity
//                                        )
//
//                                    HelpFunctions.dismissProgressBar()
//                                } else {
//                                    HelpFunctions.dismissProgressBar()
//                                    HelpFunctions.ShowLongToast(
//                                        getString(R.string.noSubCategoryFound),
//                                        this@SubCategoriesActivity
//                                    )
//                                    goNextScreen()
//
//                                }
//                            }
//
//                        }
//                    }
//
//                }
//
//                override fun onFailure(call: Call<GeneralResponses>, t: Throwable) {
//                    HelpFunctions.dismissProgressBar()
//                    Toast.makeText(this@SubCategoriesActivity, t.message, Toast.LENGTH_LONG).show()
//                }
//            })
//        } catch (ex: Exception) {
//            HelpFunctions.dismissProgressBar()
//            throw ex
//        }
//    }


    override fun OnItemClickHandler(position: Int) {
        super.OnItemClickHandler(position)
        AddProductObjectData.selectedCategoryId = allCategoryList?.get(position)?.id?:0
        AddProductObjectData.selectedCategoryName == allCategoryList!![position].name

        if (lastCategoryId != 0) {
            listCategoryViewModel!!.getSubCategoriesByCategoryID(lastCategoryId)
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