package com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.FragmentHomeeBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.data.network.service.SetOnProductItemListeners
import com.malqaa.androidappp.newPhase.domain.models.homeCategoryProductResp.CategoryProductItem
import com.malqaa.androidappp.newPhase.domain.models.homeSilderResp.HomeSliderItem
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category
import com.malqaa.androidappp.newPhase.presentation.MainActivity
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity1.CartActivity
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.ProductDetailsActivity
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.SearchCategoryActivity
import com.malqaa.androidappp.newPhase.presentation.activities.splashActivity.SplashActivity
import com.malqaa.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.adapters.AdapterAllCategories
import com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.adapters.CategoryProductAdapter
import com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.adapters.SliderAdaptor
import com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.viewModel.HomeViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.ConstantObjects.Companion.categoryList
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
import com.yariksoffice.lingver.Lingver
import io.paperdb.Paper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment(R.layout.fragment_homee), AdapterAllCategories.OnItemClickListener,
    SwipeRefreshLayout.OnRefreshListener, SetOnProductItemListeners,
    CategoryProductAdapter.SetOnSelectedProductInCategory, ListenerSlider {

    private var _binding: FragmentHomeeBinding? = null
    private val binding get() = _binding!!

    private var dotscount = 0
    private var homeViewModel: HomeViewModel? = null
    private var dots: ArrayList<ImageView>? = null
    private var lastviewedPorductAdatper: ProductHorizontalAdapter? = null
    private var lastviewedPorductList: ArrayList<Product>? = null
    private var categoryProductHomeList: ArrayList<CategoryProductItem>? = null
    private var categoryPrductAdapter: CategoryProductAdapter? = null
    private val added_from_product_in_category = 1
    private val added_from_last_product_view = 2
    private var status_product_added_to_fav_from = 0
    private var added_product_id_to_fav = 0
    private var selected_category_product_added_to_fav = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeeBinding.bind(view) // Initialize View Binding

        dots = arrayListOf()
        settingUpView()
        setListener()
        setupLastViewedPorductsAdapter()
        setUpCategoryProductAdapter()
        setupLoginViewModel()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity!!.finish()
            }
        }

        if (ConstantObjects.currentLanguage == ConstantObjects.ENGLISH) {
            binding.languageToggle.checkedTogglePosition = 0
        } else {
            binding.languageToggle.checkedTogglePosition = 1
        }

        binding.languageToggle.setOnToggleSwitchChangeListener { position, isChecked ->
            if (Paper.book().read(SharedPreferencesStaticClass.islogin, false) == true)
                homeViewModel?.setLanguageChange(
                    if (Lingver.getInstance()
                            .getLanguage() == ConstantObjects.ARABIC
                    ) ConstantObjects.ENGLISH else ConstantObjects.ARABIC
                )
            else
                setLocate()
        }

        homeViewModel?.languageObserver?.observe(viewLifecycleOwner, Observer {
            HelpFunctions.ShowLongToast(it.message, requireActivity())
            setLocate()
        })


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        // view all categories
        binding.textViewAll.setOnClickListener {
            // Navigate to CategoriesFragment or CategoriesActivity
            val action = HomeFragmentDirections.actionNavigationHomeToCategoriesFragment()
            findNavController().navigate(action)
        }
    }

    private fun setLocate() {
        categoryList = ArrayList()
        ConstantObjects.categoryProductHomeList = ArrayList()
        Lingver.getInstance().setLocale(
            requireContext(),
            if (Lingver.getInstance()
                    .getLanguage() == ConstantObjects.ARABIC
            ) ConstantObjects.ENGLISH else ConstantObjects.ARABIC
        )
        startActivity(Intent(requireContext(), SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        requireActivity().finish()
    }

    private fun settingUpView() {
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)
        if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC) {
            binding.ivNewImage.scaleX = 1f
        } else {
            binding.ivNewImage.scaleX = -1f
        }

        binding.containerLastView.hide()
    }

    private fun setupLastViewedPorductsAdapter() {
        lastviewedPorductList = ArrayList()
        lastviewedPorductAdatper =
            ProductHorizontalAdapter(lastviewedPorductList ?: arrayListOf(), this, 0, true)
        binding.rvLastViewedProducts.apply {
            adapter = lastviewedPorductAdatper
            layoutManager = linearLayoutManager(RecyclerView.HORIZONTAL)
        }

    }

    private fun setUpCategoryProductAdapter() {
        categoryProductHomeList = ArrayList()
        categoryPrductAdapter =
            CategoryProductAdapter(categoryProductHomeList ?: arrayListOf(), this)
        binding.dynamicProductRcv.apply {
            adapter = categoryPrductAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    private fun setupLoginViewModel() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel?.isLoading?.observe(viewLifecycleOwner, Observer {
            if (it)
                HelpFunctions.startProgressBar(requireActivity())
            else
                HelpFunctions.dismissProgressBar()
        })
        homeViewModel?.isLoadingAllCategory?.observe(viewLifecycleOwner, Observer {
            if (it)
                binding.progressBarAllCAtegory.show()
            else
                binding.progressBarAllCAtegory.hide()
        })

        homeViewModel!!.isNetworkFail.observe(viewLifecycleOwner, Observer {
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

        })
        homeViewModel!!.errorResponseObserver.observe(viewLifecycleOwner, Observer {
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

        })

        homeViewModel!!.sliderObserver.observe(viewLifecycleOwner, Observer { homeSliderResp ->
            if (homeSliderResp != null) {
                if (homeSliderResp.status_code == 200) {
                    homeSliderResp.sliderList?.let {
                        setPagerDots(it)
                    }
                }
            }
        })

        homeViewModel!!.categoriesObserver.observe(viewLifecycleOwner) { categoriesResp ->
            categoryList = Gson().fromJson(
                Gson().toJson(categoriesResp.data),
                object : TypeToken<ArrayList<Category>>() {}.type
            )
            binding.allCategoriesRecycler.adapter = AdapterAllCategories(
                categoryList,
                this@HomeFragment
            )
            homeViewModel!!.getListHomeCategoryProduct()
        }
        homeViewModel!!.categoriesErrorResponseObserver.observe(viewLifecycleOwner) {
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
        homeViewModel!!.homeCategoryProductObserver.observe(viewLifecycleOwner) { homeCategoriesProdcutResp ->

            if (homeCategoriesProdcutResp.status_code == 200) {
                (categoryProductHomeList ?: arrayListOf()).clear()
                homeCategoriesProdcutResp.categoryProductList?.let {
                    (categoryProductHomeList ?: arrayListOf()).addAll(it)
                }
                categoryPrductAdapter!!.notifyDataSetChanged()
            }
        }
        homeViewModel!!.homeCategoryProductErrorResponseObserver.observe(viewLifecycleOwner) {
            //HelpFunctions.ShowLongToast(getString(R.string.NoCategoriesfound), context)
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), requireActivity())
            } else {
                HelpFunctions.ShowLongToast(it.message.toString(), context)
            }
        }
        homeViewModel!!.lastViewProductsObserver.observe(viewLifecycleOwner) { productListResp ->
            if (productListResp.productList != null) {
                if (productListResp.productList.isNotEmpty()) {
                    binding.containerLastView.show()
                    (lastviewedPorductList ?: arrayListOf()).clear()
                    (lastviewedPorductList ?: arrayListOf()).addAll(productListResp.productList)
                    lastviewedPorductAdatper!!.notifyDataSetChanged()
                } else {
                    binding.containerLastView.hide()
                }
            }

        }
        homeViewModel!!.isNetworkFailProductToFav.observe(viewLifecycleOwner, Observer {
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

        })
        homeViewModel!!.errorResponseObserverProductToFav.observe(viewLifecycleOwner, Observer {
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

        })
        homeViewModel!!.addProductToFavObserver.observe(viewLifecycleOwner) {
            if (it.status_code == 200) {
                when (status_product_added_to_fav_from) {
                    added_from_product_in_category -> {
                        lifecycleScope.launch(Dispatchers.IO) {
                            var changedCategoryItemPosition = -1
                            var changedProductItemPosition = -1
                            for ((catIndex, category) in (categoryProductHomeList
                                ?: arrayListOf()).withIndex()) {
                                if (category.catId == selected_category_product_added_to_fav) {
                                    category.listProducts?.let {
                                        for ((index, product) in it.withIndex()) {
                                            if (product.id == added_product_id_to_fav) {
                                                product.isFavourite = !product.isFavourite
                                                changedProductItemPosition = index
                                                changedCategoryItemPosition = catIndex
                                                break
                                            }
                                        }
                                    }
                                }
                            }
                            withContext(Dispatchers.Main) {
                                try {
                                    if (changedCategoryItemPosition != -1 && changedProductItemPosition != -1) {
                                        // 1. get ith item of the parent recyclerView
                                        val ithChildViewHolder: CategoryProductAdapter.CategoryProductHolder =
                                            binding.dynamicProductRcv.findViewHolderForAdapterPosition(
                                                changedCategoryItemPosition
                                            ) as CategoryProductAdapter.CategoryProductHolder
                                        val ithChildsRecyclerView: RecyclerView =
                                            ithChildViewHolder.viewBinding.productRcv

                                        // 3. get ithRecyclerView's adapter
                                        val ithChildAdapter: ProductHorizontalAdapter? =
                                            ithChildsRecyclerView.adapter as ProductHorizontalAdapter?
                                        ithChildAdapter?.let { ithChildAdapter ->
                                            ithChildAdapter.notifyItemChanged(
                                                changedProductItemPosition
                                            )
                                        }
                                    }
                                } catch (e: Exception) {
                                    categoryPrductAdapter?.notifyDataSetChanged()
                                }
                            }
                        }
                        lifecycleScope.launch(Dispatchers.IO) {
                            var selectedSimilerProduct: Product? = null
                            for (product in lastviewedPorductList ?: arrayListOf()) {
                                if (product.id == added_product_id_to_fav) {
                                    product.isFavourite = !product.isFavourite
                                    selectedSimilerProduct = product
                                    break
                                }
                            }
                            withContext(Dispatchers.Main) {
                                selectedSimilerProduct?.let { product ->
                                    lastviewedPorductAdatper!!.notifyItemChanged(
                                        lastviewedPorductList!!.indexOf(
                                            product
                                        )
                                    )
                                }
                            }
                        }
                    }

                    added_from_last_product_view -> {
                        lifecycleScope.launch(Dispatchers.IO) {
                            var selectedSimilerProduct: Product? = null
                            for (product in lastviewedPorductList ?: arrayListOf()) {
                                if (product.id == added_product_id_to_fav) {
                                    product.isFavourite = !product.isFavourite
                                    selectedSimilerProduct = product
                                    break
                                }
                            }
                            withContext(Dispatchers.Main) {
                                selectedSimilerProduct?.let { product ->
                                    lastviewedPorductAdatper?.notifyItemChanged(
                                        lastviewedPorductList?.indexOf(
                                            product
                                        )!!
                                    )
                                }
                            }
                        }
                        lifecycleScope.launch(Dispatchers.IO) {
                            var changedCategoryItemPosition = -1
                            var changedProductItemPosition = -1
                            for ((catIndex, category) in (categoryProductHomeList
                                ?: arrayListOf()).withIndex()) {
                                if (category.catId == selected_category_product_added_to_fav) {
                                    category.listProducts?.let {
                                        for ((index, product) in it.withIndex()) {
                                            if (product.id == added_product_id_to_fav) {
                                                product.isFavourite = !product.isFavourite
                                                changedProductItemPosition = index
                                                changedCategoryItemPosition = catIndex
                                                break
                                            }
                                        }
                                    }
                                }
                            }
                            withContext(Dispatchers.Main) {
                                try {
                                    if (changedCategoryItemPosition != -1 && changedProductItemPosition != -1) {
                                        // 1. get ith item of the parent recyclerView
                                        val ithChildViewHolder: CategoryProductAdapter.CategoryProductHolder =
                                            binding.dynamicProductRcv.findViewHolderForAdapterPosition(
                                                changedCategoryItemPosition
                                            ) as CategoryProductAdapter.CategoryProductHolder
                                        val ithChildsRecyclerView: RecyclerView =
                                            ithChildViewHolder.viewBinding.productRcv

                                        // 3. get ithRecyclerView's adapter
                                        val ithChildAdapter: ProductHorizontalAdapter? =
                                            ithChildsRecyclerView.adapter as ProductHorizontalAdapter?
                                        ithChildAdapter?.let { ithChildAdapter ->
                                            ithChildAdapter.notifyItemChanged(
                                                changedProductItemPosition
                                            )
                                        }
                                    }
                                } catch (e: Exception) {
                                    //println("hhh "+e.message)
                                    categoryPrductAdapter?.notifyDataSetChanged()
                                }

                            }
                        }
                    }
                }
            }
        }

        homeViewModel!!.getUnReadNotification(1, 10)
        homeViewModel!!.unreadObserve.observe(viewLifecycleOwner) {
            (activity as? MainActivity)?.numBadge?.value = it.data
        }




        onRefresh()
    }

    private fun setListener() {
        binding.ivCart.setOnClickListener {
            if (ConstantObjects.logged_userid.isEmpty()) {
                startActivity(Intent(context, SignInActivity::class.java).apply {
                })
            } else {
                startActivity(Intent(context, CartActivity::class.java))
            }
        }
        binding.ivSearchIcon.setOnClickListener {
            if (binding.etSearch.text.trim().toString() == "") {
                binding.etSearch.error =
                    getString(R.string.enter_the_name_of_the_product_you_want_to_sell)
            } else {
                startActivity(Intent(requireContext(), SearchCategoryActivity::class.java).apply {
                    putExtra("ComeFrom", ConstantObjects.search_product)
                    putExtra("productName", binding.etSearch.text.trim().toString())
                    putExtra("typeView", "SearchHome")

                })
                binding.etSearch.setText("")
            }
        }
        binding.etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (binding.etSearch.text.trim().toString() == "") {
                        binding.etSearch.error =
                            getString(R.string.enter_the_name_of_the_product_you_want_to_sell)
                    } else {
                        startActivity(
                            Intent(
                                requireContext(),
                                SearchCategoryActivity::class.java
                            ).apply {
                                putExtra("ComeFrom", ConstantObjects.search_product)
                                putExtra("productName", binding.etSearch.text.trim().toString())
                                putExtra("typeView", "SearchHome")
                            })
                        binding.etSearch.setText("")
                    }
                    return true
                }
                return false
            }

        })

        binding.textInputLayout11.onClickListener {
            startActivity(Intent(requireContext(), SearchCategoryActivity::class.java).apply {
                putExtra("ComeFrom", ConstantObjects.search_product)
                putExtra("productName", it.name)
            })
            (requireActivity() as BaseActivity<*>).hideSoftKeyboard(binding.textInputLayout11._view2())
        }
        binding.textInputLayout11._onChange { query ->
            if (query.isNotEmpty()) {
                homeViewModel?.doSearch(mapOf("productName" to query))
            } else {
                binding.textInputLayout11.updateList(arrayListOf())
            }
        }
    }

    override fun pnCategorySelected(position: Int) {
        startActivity(Intent(requireContext(), SearchCategoryActivity::class.java).apply {
            putExtra("CategoryDesc", categoryList[position].name)
            putExtra("CategoryID", categoryList[position].id)
            putExtra("ComeFrom", ConstantObjects.search_categoriesDetails)
            putExtra("SearchQuery", "")
            putExtra("isMapShow", categoryList[position].id == 3)

        })
    }

    private fun setPagerDots(list: List<HomeSliderItem>) {
        if (list.isNotEmpty()) {
            binding.sliderLayout.show()
            val viewPagerAdapter = SliderAdaptor(requireContext(), list, false, this)
            binding.sliderHome.adapter = viewPagerAdapter
            binding.dotsIndicator.attachTo(binding.sliderHome)
            binding.sliderHome.startAutoScroll()
        }
    }

    override fun onRefresh() {
        binding.swipeToRefresh.isRefreshing = false
        dotscount = 0
        dots?.clear()
        binding.SliderDots.removeAllViews()
        homeViewModel?.getSliderData(1)
        homeViewModel?.getAllCategories()
        if (HelpFunctions.isUserLoggedIn()) {
            homeViewModel?.getLastViewedProduct()
        }
    }

    override fun onProductSelect(position: Int, productId: Int, categoryID: Int) {
        goToProductDetails(productId)
    }

    override fun onAddProductToFav(position: Int, productId: Int, categoryID: Int) {
        if (HelpFunctions.isUserLoggedIn()) {
            status_product_added_to_fav_from = added_from_last_product_view
            added_product_id_to_fav = productId
            homeViewModel?.addProductToFav(productId)

        } else {
            requireActivity().startActivity(
                Intent(
                    context,
                    SignInActivity::class.java
                ).apply {})
        }
    }

    override fun onShowMoreSetting(position: Int, productID: Int, categoryID: Int) {
    }

    override fun onSelectedProductInCategory(position: Int, productID: Int, categoryID: Int) {
        goToProductDetails(productID)
    }

    override fun onAddProductInCategoryToFav(position: Int, productID: Int, categoryID: Int) {
        if (HelpFunctions.isUserLoggedIn()) {
            status_product_added_to_fav_from = added_from_product_in_category
            added_product_id_to_fav = productID
            selected_category_product_added_to_fav = categoryID
            homeViewModel?.addProductToFav(productID)

        } else {
            requireActivity().startActivity(
                Intent(
                    context,
                    SignInActivity::class.java
                ).apply {})
        }
    }

    /**open activity product detials functions**/
    private val productDetailsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val productId: Int = result.data?.getIntExtra(ConstantObjects.productIdKey, 0) ?: 0
                val productFavStatusKey: Boolean =
                    result.data?.getBooleanExtra(ConstantObjects.productFavStatusKey, false)
                        ?: false
                refreshFavProductStatus(productId, productFavStatusKey)
            }
        }

    private fun goToProductDetails(productId: Int) {
        productDetailsLauncher.launch(Intent(context, ProductDetailsActivity::class.java).apply {
            putExtra(ConstantObjects.productIdKey, productId)
            putExtra("Template", "")
        })
    }

    override fun onResume() {
        super.onResume()
        if (SharedPreferencesStaticClass.getCartCount() == 0) {
            binding.txtCount.hide()
        } else {
            binding.txtCount.show()
            binding.txtCount.text = SharedPreferencesStaticClass.getCartCount().toString()
        }
    }

    private fun refreshFavProductStatus(productId: Int, productFavStatusKey: Boolean) {
        /***for similer product*/
        lifecycleScope.launch(Dispatchers.IO) {
            var selectedSimilerProduct: Product? = null
            for (product in (lastviewedPorductList ?: arrayListOf())) {
                if (product.id == productId) {
                    product.isFavourite = productFavStatusKey
                    selectedSimilerProduct = product
                    break
                }
            }
            withContext(Dispatchers.Main) {
                /**update similer product*/
                selectedSimilerProduct?.let { product ->
                    lastviewedPorductAdatper?.notifyItemChanged(
                        lastviewedPorductList!!.indexOf(
                            product
                        )
                    )
                }
            }
        }
        /**for category products*/
        lifecycleScope.launch(Dispatchers.IO) {
            var changedCategoryItemPosition = -1
            var changedProductItemPosition = -1
            for ((catIndex, category) in (categoryProductHomeList ?: arrayListOf()).withIndex()) {
                if (category.catId == selected_category_product_added_to_fav) {
                    category.listProducts?.let {
                        for ((index, product) in it.withIndex()) {
                            if (product.id == productId) {
                                product.isFavourite = productFavStatusKey
                                changedProductItemPosition = index
                                changedCategoryItemPosition = catIndex
                                break
                            }
                        }
                    }
                }
            }
            withContext(Dispatchers.Main) {
                /**update  product in Category*/
                try {
                    if (changedCategoryItemPosition != -1 && changedProductItemPosition != -1) {
                        // 1. get ith item of the parent recyclerView
                        val ithChildViewHolder: CategoryProductAdapter.CategoryProductHolder =
                            binding.dynamicProductRcv.findViewHolderForAdapterPosition(
                                changedCategoryItemPosition
                            ) as CategoryProductAdapter.CategoryProductHolder
                        val itChildRecyclerView: RecyclerView =
                            ithChildViewHolder.viewBinding.productRcv

                        // 3. get ithRecyclerView's adapter
                        val ithChildAdapter: ProductHorizontalAdapter? =
                            itChildRecyclerView.adapter as ProductHorizontalAdapter?
                        ithChildAdapter?.let { ithChildAdapter ->
                            ithChildAdapter.notifyItemChanged(
                                changedProductItemPosition
                            )
                        }
                    }
                } catch (e: Exception) {
                    categoryPrductAdapter?.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeViewModel?.closeAllCall()
        homeViewModel?.baseCancel()
        homeViewModel = null
        dots = null
        lastviewedPorductAdatper?.onDestroyHandler()
        lastviewedPorductAdatper = null
        lastviewedPorductList = null
        categoryProductHomeList = null
        categoryPrductAdapter = null
    }

    override fun onClickImage(url: String) {
        TODO("Not yet implemented")
    }
}

