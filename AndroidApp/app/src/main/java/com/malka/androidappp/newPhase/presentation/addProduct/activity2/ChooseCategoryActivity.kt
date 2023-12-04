package com.malka.androidappp.newPhase.presentation.addProduct.activity2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.presentation.addProduct.activity3.SubCategoriesActivity
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.presentation.addProduct.AddProductObjectData
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category
import com.malka.androidappp.newPhase.presentation.addProduct.activity4.AddPhotoActivity
import com.malka.androidappp.newPhase.presentation.addProduct.viewmodel.AddProductViewModel
import kotlinx.android.synthetic.main.activity_choose_category.*
import kotlinx.android.synthetic.main.toolbar_main.*

class ChooseCategoryActivity : BaseActivity() {
    var allCategoryList: List<Category> = ArrayList()
    var position = -1
    private lateinit var addProductViewModel: AddProductViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_category)
        toolbar_title.text = getString(R.string.choose_department)
        back_btn.setOnClickListener {
            finish()
        }
        add_product_button4.setOnClickListener {
//            AddProductObjectData.subCategoryPath.clear()
            if (position == -1) {
                showError(getString(R.string.Please_choose_catgeory))
            } else {
                // AddProductObjectData.subCategoryPath.add(allCategoryList[position].name.toString())
                //  AddProductObjectData.selectedCategory=allCategoryList[position]
                AddProductObjectData.selectedCategoryId = allCategoryList[position].id
                AddProductObjectData.selectedCategoryName = allCategoryList[position].name
//                startActivity(Intent(this, SubCategoriesActivity::class.java).apply {
//                    putExtra(ConstantObjects.categoryIdKey, allCategoryList[position].id.toString())
//                    putExtra(ConstantObjects.categoryName, allCategoryList[position].name.toString())
//                })
                AddProductObjectData.selectedCategory = allCategoryList[position]

                startActivity(Intent(this, SubCategoriesActivity::class.java).apply {
                    putExtra(ConstantObjects.categoryIdKey, allCategoryList[position].id)
                    putExtra(ConstantObjects.categoryName, allCategoryList[position].name)
                })
//                goNextScreen(false)


//                if (!allCategoryList[position].isCategory) {
//                    AddProductObjectData.subCategoryPath.add(allCategoryList[position].name.toString())
//                    val templateName = truncateString(allCategoryList[position].template.toString())
//                    AddProductObjectData.template = templateName
//                    startActivity(Intent(this, AddPhotoActivity::class.java).apply {
//                        putExtra("Title", allCategoryList[position].name.toString())
//                    })
//                } else {
//
//                    AddProductObjectData.subCategoryPath.add(allCategoryList[position].name.toString())
//                    startActivity(Intent(this, SubCategoriesActivity::class.java).apply {
//                        putExtra("categoryid", allCategoryList[position].categoryKey.toString())
//                        putExtra("categoryName", allCategoryList[position].name.toString())
//                    })
//                }
            }


        }
        //  getAllCategories()
        setupLoginViewModel()
        allCategoryList = ConstantObjects.categoryList
        if(allCategoryList.isNotEmpty()){
            getAllCategories()
        }else{
            addProductViewModel.getAllCategories()
        }
    }


    private fun setupLoginViewModel() {
        addProductViewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
        addProductViewModel.isLoadingAllCategory.observe(this, Observer {
            if (it)
                progressbar.show()
            else
                progressbar.hide()
        })

        addProductViewModel.categoriesObserver.observe(this) { categoriesResp ->
            if (categoriesResp.status_code == 200) {
                ConstantObjects.categoryList = Gson().fromJson(
                    Gson().toJson(categoriesResp.data),
                    object : TypeToken<ArrayList<Category>>() {}.type
                )
                getAllCategories()
            }
        }
        addProductViewModel.categoriesErrorResponseObserver.observe(this) {
            //HelpFunctions.ShowLongToast(getString(R.string.NoCategoriesfound), context)
            if(it.status!=null && it.status=="409"){
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            }else{
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
            recycler_all_category.adapter =
                AdapterAllCategoriesAdapter(
                    allCategoryList
                ) {
                    position = it
                }
            //HelpFunctions.dismissProgressBar()
        } else {
            //HelpFunctions.dismissProgressBar()
            Toast.makeText(this@ChooseCategoryActivity, "No Categories found", Toast.LENGTH_LONG)
                .show()
        }

    }

    private fun goNextScreen(isFinish:Boolean) {
        if(isFinish) {
            startActivity(Intent(this, AddPhotoActivity::class.java))
            finish()
        }else{
            startActivity(Intent(this, AddPhotoActivity::class.java))
        }
    }
}