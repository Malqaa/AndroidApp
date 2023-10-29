package com.malka.androidappp.fragments.follow_Up

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.presentation.addProduct.activity2.AdapterAllCategoriesAdapter
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category
import com.malka.androidappp.newPhase.presentation.addProduct.AddProductObjectData
import com.malka.androidappp.newPhase.presentation.addProduct.activity3.AdapterSubCategories
import com.malka.androidappp.newPhase.presentation.addProduct.activity3.SubCategoriesActivity
import com.malka.androidappp.newPhase.presentation.addProduct.viewmodel.AddProductViewModel
import kotlinx.android.synthetic.main.fragment_favorite_category.*
import kotlinx.android.synthetic.main.fragment_sub_categories.allCategoriesRecyclerView
import kotlinx.android.synthetic.main.fragment_sub_categories.progressBar
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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

                    startActivity(Intent(requireActivity(), SubCategoriesActivity::class.java).apply {
                        putExtra(ConstantObjects.categoryIdKey, allCategoryList[position].id)
                        putExtra(ConstantObjects.categoryName, allCategoryList[position].name)
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