package com.malqaa.androidappp.fragments.follow_Up

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity2.AdapterAllCategoriesAdapter
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity3.SubCategoriesActivity
import kotlinx.android.synthetic.main.fragment_favorite_category.*
import kotlinx.android.synthetic.main.toolbar_main.*


class FavoriteCategory : Fragment(R.layout.fragment_favorite_category) {

    var allCategoryList: List<Category> = ArrayList()
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

                    startActivity(Intent(requireActivity(), FavoriteSubCategory::class.java).apply {
                        putExtra(ConstantObjects.categoryIdKey, allCategoryList[position].id)
                        putExtra(ConstantObjects.categoryName, allCategoryList[position].name)
                        putExtra("FromFavorite", true)
                    })

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        position=-1
    }


    private fun getAllCategories() {
        allCategoryList = ConstantObjects.categoryList
        if (allCategoryList.isNotEmpty()) {
            allCategoryList.forEach {
                it.is_select=false
            }
            fav_category.adapter =
                AdapterAllCategoriesAdapter(
                    allCategoryList
                ) {
                    position = it
                }
            HelpFunctions.dismissProgressBar()
        }

    }
}