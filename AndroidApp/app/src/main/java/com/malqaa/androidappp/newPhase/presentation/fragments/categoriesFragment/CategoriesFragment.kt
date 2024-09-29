package com.malqaa.androidappp.newPhase.presentation.fragments.categoriesFragment

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.SearchCategoryActivity
import com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.viewModel.HomeViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.ConstantObjects.Companion.categoryList
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import kotlinx.android.synthetic.main.fragment_homee.progressBarAllCAtegory


class CategoriesFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backTextView = view.findViewById<TextView>(R.id.text_back)

        // Initialize NavController
        val navController = findNavController()

        backTextView.setOnClickListener {
            // Navigate with popUpTo, ensuring we go back to `navigation_home`
            navController.navigate(
                R.id.action_categoriesFragment_to_navigation_home,
                null,  // No additional arguments
                NavOptions.Builder()
                    .setPopUpTo(
                        R.id.navigation_home,
                        false
                    )  // popUpTo navigation_home without removing it
                    .build()
            )
        }

        categoryAdapter =
            CategoryAdapter(categoryList, object : CategoryAdapter.OnItemClickListener {
                override fun onItemClick(category: Category) {
                    // Handle category item click
                    startActivity(
                        Intent(
                            requireContext(),
                            SearchCategoryActivity::class.java
                        ).apply {
                            putExtra("CategoryDesc", category.name)
                            putExtra("CategoryID", category.id)
                            putExtra("ComeFrom", ConstantObjects.search_categoriesDetails)
                            putExtra("SearchQuery", "")
                            putExtra("isMapShow", category.id == 3)

                        })
                }
            })

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_categories)
        recyclerView.adapter = categoryAdapter

        setupLoginViewModel()
    }

    private fun setupLoginViewModel() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // is loading
        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it)
                HelpFunctions.startProgressBar(requireActivity())
            else
                HelpFunctions.dismissProgressBar()
        }
        // is loading all category
        homeViewModel.isLoadingAllCategory.observe(viewLifecycleOwner) {
            if (it)
                progressBarAllCAtegory.show()
            else
                progressBarAllCAtegory.hide()
        }
        // is network fail
        homeViewModel.isNetworkFail.observe(viewLifecycleOwner) {
            if (it) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.connectionError),
                    requireActivity()
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    requireActivity()
                )
            }
        }
        //error response observer
        homeViewModel.errorResponseObserver.observe(viewLifecycleOwner) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), requireActivity())
            } else {
                if (it.message != null && it.message != "") {
                    HelpFunctions.ShowLongToast(
                        it.message!!,
                        requireActivity()
                    )
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.serverError),
                        requireActivity()
                    )
                }
            }
        }
        // categories observer
        homeViewModel.categoriesObserver.observe(viewLifecycleOwner) { categoriesResp ->
            categoryList = Gson().fromJson(
                Gson().toJson(categoriesResp.data),
                object : TypeToken<ArrayList<Category>>() {}.type
            )

            homeViewModel.getListHomeCategoryProduct()
        }
    }
}