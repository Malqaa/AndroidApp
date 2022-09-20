package com.malka.androidappp.fragments.Follow_Up

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.add_product.all_categories.AdapterAllCategories
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.model.Category
import kotlinx.android.synthetic.main.fragment_favorite_category.*
import kotlinx.android.synthetic.main.toolbar_main.*


class favorite_category : Fragment(R.layout.fragment_favorite_category) {

    var allCategoryList: List<Category> = ArrayList()
    var position = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setListenser()
        getAllCategories()

    }

    private fun initView() {
        toolbar_title.visibility = View.GONE
    }


    private fun setListenser() {

        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }

//        add_new_category_btn.setOnClickListener {
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
//                    startActivity(Intent(requireActivity(), AddPhoto::class.java).apply {
//                        putExtra("Title", allCategoryList[position].categoryName.toString())
//                    })
//                } else {
//
//                    StaticClassAdCreate.subCategoryPath.add(allCategoryList[position].categoryName.toString())
//
//                    startActivity(Intent(requireActivity(), SubCategories::class.java).apply {
//                        putExtra("categoryid", allCategoryList[position].categoryKey.toString())
//                        putExtra("categoryName", allCategoryList[position].categoryName.toString())
//                    })
//                }
//            }
//        }


        add_new_category_btn.setOnClickListener() {

            if (position==-1){
                (requireActivity() as BaseActivity).showError(getString(R.string.Please_choose_catgeory))
            }else{
                findNavController().navigate(R.id.newSubCategory)
            }
        }

    }




    fun getAllCategories() {
        allCategoryList = ConstantObjects.categoryList
        if (allCategoryList.count() > 0) {
            allCategoryList.forEach {
                it.is_select=false
            }
            fav_category.adapter =
                AdapterAllCategories(
                    allCategoryList
                ) {
                    position = it
                }
            HelpFunctions.dismissProgressBar()
        }

    }
}