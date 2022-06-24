package com.malka.androidappp.botmnav_fragments.Follow_Up

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.add_product.all_categories.AdapterAllCategories
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.model.AllCategoriesModel
import kotlinx.android.synthetic.main.fragment_favorite_category.*
import kotlinx.android.synthetic.main.toolbar_main.*


class favorite_category : Fragment(R.layout.fragment_favorite_category) {

    var allCategoryList: List<AllCategoriesModel> = ArrayList()
    var position = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setListenser()
        getAllCategories()

    }

    private fun initView() {
        toolbar_title.text = getString(R.string.favorite_categories)
    }


    private fun setListenser() {

        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }

//        add_product_button4.setOnClickListener {
//            StaticClassAdCreate.subCategoryPath.clear()
//            if(position==-1){
//                (this as BaseActivity).showError(getString(R.string.Please_choose_catgeory))
//            }else{
//                if (!allCategoryList[position].isCategory) {
//
//                    StaticClassAdCreate.subCategoryPath.add(allCategoryList[position].categoryName.toString())
//                    val templateName =
//                        Extension.truncateString(allCategoryList[position].template.toString())
//                    StaticClassAdCreate.template = templateName
//                    startActivity(Intent(this, AddPhoto::class.java).apply {
//                        putExtra("Title", allCategoryList[position].categoryName.toString())
//                    })
//                } else {
//
//                    StaticClassAdCreate.subCategoryPath.add(allCategoryList[position].categoryName.toString())
//                    startActivity(Intent(this, SubCategories::class.java).apply {
//                        putExtra("categoryid", allCategoryList[position].categoryKey.toString())
//                        putExtra("categoryName", allCategoryList[position].categoryName.toString())
//                    })
//                }
//            }
//        }


        add_new_category_btn.setOnClickListener() {
            findNavController().navigate(R.id.newSubCategory)
        }

    }




    fun getAllCategories() {
        allCategoryList = ConstantObjects.categoryList
        if (allCategoryList.count() > 0) {
            allCategoryList.forEach {
                it.is_select=false
            }
            all_category_rcv.adapter =
                AdapterAllCategories(
                    allCategoryList
                ) {
                    position = it
                }
            HelpFunctions.dismissProgressBar()
        }

    }
}