package com.malqaa.androidappp.newPhase.presentation.fragments.categoriesFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.FragmentCategoriesBinding
import com.malqaa.androidappp.databinding.FragmentHomeeBinding
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.SearchCategoryActivity
import com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.viewModel.HomeViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.ConstantObjects.Companion.categoryList
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

class CategoriesFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var categoryAdapter: CategoryAdapter

    // Declare the binding objects
    private var _categoriesBinding: FragmentCategoriesBinding? = null
    private val categoriesBinding get() = _categoriesBinding!!

    // Add a binding object for FragmentHomeBinding
    private var _homeBinding: FragmentHomeeBinding? = null
    private val homeBinding get() = _homeBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize the FragmentCategoriesBinding object
        _categoriesBinding = FragmentCategoriesBinding.inflate(inflater, container, false)

        // Optionally inflate FragmentHomeBinding if needed
        _homeBinding = FragmentHomeeBinding.inflate(inflater, container, false)

        return categoriesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize NavController
        val navController = findNavController()

        // Use binding to reference the backTextView
        categoriesBinding.textBack.setOnClickListener {
            navController.navigate(
                R.id.action_categoriesFragment_to_navigation_home,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_home, false)
                    .build()
            )
        }

        // Initialize the category adapter with the list of categories
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

        // Set the adapter to the RecyclerView using binding
        categoriesBinding.recyclerViewCategories.adapter = categoryAdapter

        // Set up the ViewModel and observers
        setupLoginViewModel()
    }

    private fun setupLoginViewModel() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // isLoading observer
        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it)
                HelpFunctions.startProgressBar(requireActivity())
            else
                HelpFunctions.dismissProgressBar()
        }

        // isLoadingAllCategory observer
        homeViewModel.isLoadingAllCategory.observe(viewLifecycleOwner) {
            if (it)
                homeBinding.progressBarAllCAtegory.show() // Corrected to match XML ID
            else
                homeBinding.progressBarAllCAtegory.hide() // Corrected to match XML ID
        }

        // isNetworkFail observer
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

        // errorResponseObserver
        homeViewModel.errorResponseObserver.observe(viewLifecycleOwner) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), requireActivity())
            } else {
                if (it.message != null && it.message != "") {
                    HelpFunctions.ShowLongToast(it.message!!, requireActivity())
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
                }
            }
        }

        // categoriesObserver
        homeViewModel.categoriesObserver.observe(viewLifecycleOwner) { categoriesResp ->
            categoryList = Gson().fromJson(
                Gson().toJson(categoriesResp.data),
                object : TypeToken<ArrayList<Category>>() {}.type
            )
            homeViewModel.getListHomeCategoryProduct()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the binding to avoid memory leaks
        _categoriesBinding = null
        _homeBinding = null
    }
}
