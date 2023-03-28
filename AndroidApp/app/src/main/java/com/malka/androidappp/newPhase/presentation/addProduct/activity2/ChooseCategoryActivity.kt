package com.malka.androidappp.newPhase.presentation.addProduct.activity2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.presentation.addProduct.activity3.SubCategoriesActivity
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.presentation.addProduct.AddProductObjectData
import com.malka.androidappp.newPhase.data.helper.Extension.truncateString
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category
import com.malka.androidappp.newPhase.presentation.addProduct.activity4.AddPhotoActivity
import kotlinx.android.synthetic.main.activity_choose_category.*
import kotlinx.android.synthetic.main.toolbar_main.*

class ChooseCategoryActivity : BaseActivity() {
    var allCategoryList: List<Category> = ArrayList()
    var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_category)
        toolbar_title.text = getString(R.string.choose_department)
        back_btn.setOnClickListener {
            finish()
        }

        add_product_button4.setOnClickListener {
//            AddProductObjectData.subCategoryPath.clear()
            if(position==-1){
                showError(getString(R.string.Please_choose_catgeory))
            }else{
               // AddProductObjectData.subCategoryPath.add(allCategoryList[position].name.toString())
              //  AddProductObjectData.selectedCategory=allCategoryList[position]
                AddProductObjectData.selectedCategoryId=allCategoryList[position].id
                AddProductObjectData.selectedCategoryName=allCategoryList[position].name
                startActivity(Intent(this, SubCategoriesActivity::class.java).apply {
                    putExtra(ConstantObjects.categoryIdKey, allCategoryList[position].id.toString())
                    putExtra(ConstantObjects.categoryName, allCategoryList[position].name.toString())
                })
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
        getAllCategories()

    }


    fun getAllCategories() {
        allCategoryList = ConstantObjects.categoryList
        if (allCategoryList.count() > 0) {
            allCategoryList.forEach {
                it.is_select=false
            }
            recycler_all_category.adapter =
                AdapterAllCategoriesAdapter(
                    allCategoryList
                ) {
                    position = it
                }
            HelpFunctions.dismissProgressBar()
        } else {
            HelpFunctions.dismissProgressBar()
            Toast.makeText(this@ChooseCategoryActivity, "No Categories found", Toast.LENGTH_LONG)
                .show()
        }

    }
}