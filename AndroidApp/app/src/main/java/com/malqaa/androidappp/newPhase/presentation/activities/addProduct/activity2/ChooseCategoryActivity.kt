package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityChooseCategoryBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.confirmationAddProduct.ConfirmationAddProductActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity3.SubCategoriesActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

class ChooseCategoryActivity : BaseActivity<ActivityChooseCategoryBinding>() {
    var allCategoryList: List<Category> = ArrayList()
    var position = -1
    private var isEdit: Boolean = false
    private var categoryViewModel: CategoryViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize view binding
        binding = ActivityChooseCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)

        setupLoginViewModel()
        binding.toolBar.toolbarTitle.text = getString(R.string.choose_main_category)


        binding.toolBar.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.addProductButton4.setOnClickListener {
            if (isEdit) {

                startActivity(Intent(this, SubCategoriesActivity::class.java).apply {
                    putExtra(ConstantObjects.isEditKey, isEdit)
                    putExtra(ConstantObjects.categoryIdKey, AddProductObjectData.selectedCategoryId)
                    putExtra(
                        ConstantObjects.categoryName,
                        AddProductObjectData.selectedCategoryName
                    )
                    putExtra("FromFavorite", false)
                })
            } else {

                if (position == -1) {
                    showError(getString(R.string.Please_choose_catgeory))
                } else {
                    AddProductObjectData.selectedCategoryId = allCategoryList[position].id
                    AddProductObjectData.selectedCategoryName = allCategoryList[position].name

                    AddProductObjectData.selectedCategory = allCategoryList[position]

                    startActivity(Intent(this, SubCategoriesActivity::class.java).apply {
                        putExtra(ConstantObjects.isEditKey, isEdit)
                        putExtra(ConstantObjects.categoryIdKey, allCategoryList[position].id)
                        putExtra(ConstantObjects.categoryName, allCategoryList[position].name)
                        putExtra("FromFavorite", false)
                    })
                }
            }
        }

        //  getAllCategories()

    }

    private fun setDataForUpdate() {

        if (isEdit) {
            allCategoryList = ConstantObjects.categoryList
            for (i in allCategoryList.indices) {
                if (allCategoryList[i].id == AddProductObjectData.selectedCategoryId) {
                    allCategoryList[i].is_select = true
                }
            }

            binding.recyclerAllCategory.adapter =
                AdapterAllCategoriesAdapter(
                    allCategoryList
                ) {
                    position = it
                }

        }

    }

    private fun setupLoginViewModel() {
        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        allCategoryList = ConstantObjects.categoryList
        if (allCategoryList.isNotEmpty()) {
            getAllCategories()
        } else {
            categoryViewModel?.getAllCategories()
        }

        categoryViewModel?.isLoadingAllCategory?.observe(this, Observer {
            if (it)
                binding.progressbar.show()
            else
                binding.progressbar.hide()
        })

        categoryViewModel?.categoriesObserver?.observe(this) { categoriesResp ->
            if (categoriesResp.status_code == 200) {
                ConstantObjects.categoryList = Gson().fromJson(
                    Gson().toJson(categoriesResp.data),
                    object : TypeToken<ArrayList<Category>>() {}.type
                )
                getAllCategories()
            }
        }
        categoryViewModel!!.errorResponseObserver.observe(this) {
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
    }

    private fun getAllCategories() {
        allCategoryList = ConstantObjects.categoryList
        if (allCategoryList.isNotEmpty()) {
            allCategoryList.forEach {
                it.is_select = false
            }
            setDataForUpdate()
            binding.recyclerAllCategory.adapter =
                AdapterAllCategoriesAdapter(
                    allCategoryList
                ) {
                    position = it
                }
        } else {
            Toast.makeText(this@ChooseCategoryActivity, "No Categories found", Toast.LENGTH_LONG)
                .show()
        }

    }


    override fun onBackPressed() {
        if (isEdit) {
            startActivity(Intent(this, ConfirmationAddProductActivity::class.java).apply {
                putExtra("whereCome", "Add")
                putExtra(ConstantObjects.isEditKey, false)
            })
            finish()
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        categoryViewModel?.closeAllCall()
        categoryViewModel?.baseCancel()
        categoryViewModel = null
    }
}
