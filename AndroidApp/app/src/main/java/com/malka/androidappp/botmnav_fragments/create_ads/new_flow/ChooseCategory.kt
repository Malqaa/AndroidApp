package com.malka.androidappp.botmnav_fragments.create_ads.new_flow

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.BaseActivity
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.botmnav_fragments.create_ads.new_flow.all_categories.AdapterAllCategories
import com.malka.androidappp.botmnav_fragments.create_ads.new_flow.all_categories.SubCategories
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesModel
import com.malka.androidappp.helper.Extension.truncateString
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_choose_cate.*
import kotlinx.android.synthetic.main.toolbar_main.*

class ChooseCategory : BaseActivity() {
    var allCategoryList: List<AllCategoriesModel> = ArrayList()
    var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_choose_cate)
        toolbar_title.text = getString(R.string.choose_department)
        back_btn.setOnClickListener {
            finish()
        }

        add_product_button4.setOnClickListener {
            StaticClassAdCreate.subCategoryPath.clear()
            if(position==-1){
                showError(getString(R.string.Please_choose_catgeory))
            }else{
                if (!allCategoryList[position].isCategory) {

                    StaticClassAdCreate.subCategoryPath.add(allCategoryList[position].categoryName.toString())
                    val templateName =
                        truncateString(allCategoryList[position].template.toString())
                    StaticClassAdCreate.template = templateName
                    startActivity(Intent(this, AddPhoto::class.java).apply {
                        putExtra("Title", allCategoryList[position].categoryName.toString())
                    })
                } else {

                    StaticClassAdCreate.subCategoryPath.add(allCategoryList[position].categoryName.toString())
                    startActivity(Intent(this, SubCategories::class.java).apply {
                        putExtra("categoryid", allCategoryList[position].categoryKey.toString())
                        putExtra("categoryName", allCategoryList[position].categoryName.toString())
                    })
                }
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
                AdapterAllCategories(
                    allCategoryList
                ) {
                    position = it
                }
            HelpFunctions.dismissProgressBar()
        } else {
            HelpFunctions.dismissProgressBar()
            Toast.makeText(this@ChooseCategory, "No Categories found", Toast.LENGTH_LONG)
                .show()
        }

    }
}