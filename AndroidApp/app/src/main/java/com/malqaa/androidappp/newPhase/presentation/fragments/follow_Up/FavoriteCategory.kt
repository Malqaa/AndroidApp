package com.malqaa.androidappp.fragments.follow_Up

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.FragmentFavoriteCategoryBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity2.AdapterAllCategoriesAdapter
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions


class FavoriteCategory : Fragment(R.layout.fragment_favorite_category) {

    // Declare the binding object
    private var _binding: FragmentFavoriteCategoryBinding? = null
    private val binding get() = _binding!!

    var allCategoryList: List<Category> = ArrayList()
    var position = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the binding object
        _binding = FragmentFavoriteCategoryBinding.bind(view)

        initView()
        setListener()
        getAllCategories()
    }

    private fun initView() {
        // Use binding to access toolbar title
        binding.toolbarMain.toolbarTitle.text = getString(R.string.favorite_categories)
    }

    private fun setListener() {
        // Back button listener using binding
        binding.toolbarMain.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Add new category button listener using binding
        binding.addNewCategoryBtn.setOnClickListener {
            if (position == -1) {
                (requireActivity() as BaseActivity<*>).showError(getString(R.string.Please_choose_catgeory))
            } else {
                startActivity(
                    Intent(requireActivity(), FavoriteSubCategory::class.java).apply {
                        putExtra(ConstantObjects.categoryIdKey, allCategoryList[position].id)
                        putExtra(ConstantObjects.categoryName, allCategoryList[position].name)
                        putExtra("FromFavorite", true)
                    }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the binding object to avoid memory leaks
        _binding = null
        position = -1
    }

    private fun getAllCategories() {
        allCategoryList = ConstantObjects.categoryList
        if (allCategoryList.isNotEmpty()) {
            allCategoryList.forEach {
                it.is_select = false
            }

            // Set adapter using binding
            binding.favCategory.adapter =
                AdapterAllCategoriesAdapter(
                    allCategoryList
                ) { selectedPosition ->
                    position = selectedPosition
                }
            HelpFunctions.dismissProgressBar()
        }
    }
}
