package com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.exoplayer2.util.Log
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.FragmentBrowseMarketBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.data.network.service.SetOnProductItemListeners
import com.malqaa.androidappp.newPhase.domain.enums.ProductPosition
import com.malqaa.androidappp.newPhase.domain.models.ErrorResponse
import com.malqaa.androidappp.newPhase.domain.models.productResp.CategoriesSearchItem
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.ProductDetailsActivity
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.CategoryProductViewModel
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.ProductRowFullAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog.FilterCategoryProductsDialog
import com.malqaa.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.EndlessRecyclerViewScrollListener
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.isTextInEnglish
import com.malqaa.androidappp.newPhase.utils.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody


class SearchCategoryActivity : BaseActivity<FragmentBrowseMarketBinding>(),
    SwipeRefreshLayout.OnRefreshListener,
    SetOnProductItemListeners, FilterCategoryProductsDialog.SetOnClickListeners {

    var categoryID: Int = 0
    var categoryName = ""
    var countryList: List<Int> = ArrayList()
    var regionList: List<Int> = ArrayList()
    var neighoodList: List<Int> = ArrayList()
    var subCategoryList: List<Int> = ArrayList()
    var specificationList: List<String> = ArrayList()
    var startPrice: Float = 0f
    var endProce: Float = 0f
    var comeFrom = 1
    private var flagList = true

    private lateinit var productSearchCategoryAdapter: ProductHorizontalAdapter
    private lateinit var productCategoryAdapter: ProductHorizontalAdapter
    private lateinit var productRowFullAdapter: ProductRowFullAdapter

    lateinit var gridViewLayoutManager: GridLayoutManager
    lateinit var linerlayout: LinearLayoutManager
    lateinit var filterCategoryProductsDialog: FilterCategoryProductsDialog
    private lateinit var productsListViewModel: CategoryProductViewModel
    private lateinit var productList: ArrayList<Product>
    private lateinit var productListVip: ArrayList<Product>
    private var lastUpdateIndex = -1
    var strSearch = ""
    var clickSearch = false
    var productName: String? = null
    private var added_product_id_to_fav = 0
    var isLoading = false

    lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    lateinit var endlessRecyclerViewScrollListener2: EndlessRecyclerViewScrollListener
    var isFollowCategory: Boolean = false
    var categoriesForProductList: ArrayList<CategoriesSearchItem> = ArrayList()
    var getCategoryForFirstTimeOnlyToUseInFilter = false
    var page = 1
    var typeView = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = FragmentBrowseMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)
        setProductSearchCategoryAdapter()
        setViewClickListeners()

        typeView = intent.getStringExtra("typeView") ?: ""
        if (typeView == "SearchHome") {
            comeFrom = 3
            categoryID = 0
            binding.followCategory.visibility = View.GONE
            binding.laySearch.visibility = View.VISIBLE
        } else {
            categoryName = intent.getStringExtra("CategoryDesc") ?: "CategoryName"
            categoryID = intent.getIntExtra("CategoryID", 0)
            comeFrom = intent.getIntExtra("ComeFrom", 0)
            binding.laySearch.visibility = View.GONE
            binding.followCategory.visibility = View.VISIBLE
        }
        productName = intent.getStringExtra("productName")

        binding.lblToolbarCategory.text = categoryName
        setupViewModel()
        when (comeFrom) {
            ConstantObjects.search_categoriesDetails -> {
                binding.btnSubCatgeoryFilter.text = getText(R.string.sub_categories)
            }

            ConstantObjects.search_product -> {
                binding.btnSubCatgeoryFilter.text = getText(R.string.Categories)
            }

            ConstantObjects.search_seller -> {
                binding.btnSubCatgeoryFilter.text = getText(R.string.Categories)
            }
        }

        filterCategoryProductsDialog = FilterCategoryProductsDialog(
            productsListViewModel,
            this,
            FilterCategoryProductsDialog.subCategoryType,
            categoryID,
            this,
            comeFrom
        )
        filterCategoryProductsDialog.setCategories(categoriesForProductList)
        getResetAll()
    }

    private fun setupViewModel() {
        productsListViewModel = ViewModelProvider(this).get(CategoryProductViewModel::class.java)
        productsListViewModel.isLoading.observe(this) {
            if (it)
                binding.progressBar.show()
            else
                binding.progressBar.hide()
        }


        productsListViewModel.isloadingMore.observe(this) {
            if (it)
                binding.progressBarMore.show()
            else
                binding.progressBarMore.hide()
        }
        productsListViewModel.isNetworkFail.observe(this) {
            if (it) {
                showProductApiError(getString(R.string.connectionError))
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        productsListViewModel.errorResponseObserver.observe(this) {
            if (it.message != null) {
                showProductApiError(it.message!!)
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }

        productsListViewModel.saveSearchObserver.observe(this) {
            if (it != null) {
                if (it.status_code == 200) {
                    strSearch = binding.etSearch.text.toString()
                    HelpFunctions.ShowLongToast(getString(R.string.saveDone), this)
                    binding.imgFollow.setImageResource(R.drawable.notification)
                }
            }
        }
        productsListViewModel.searchProductListRespObserver.observe(this) { productListResp ->
            if (productListResp.status_code == 200) {
                val products = productListResp.data?.products.orEmpty()

                // Update the total result text view
                binding.totalResultTv.text =
                    "${productListResp.totaRecords} ${getString(R.string.results)}"

                if (products.isNotEmpty()) {
                    val listVip =
                        products.filter { it.productPosition == ProductPosition.Vip.value }
                    val allListOutVip =
                        products.filter { it.productPosition != ProductPosition.Vip.value }

                    // Handle VIP product list
                    if (listVip.isNotEmpty()) {
                        binding.recyclerMarketFull.visibility = View.VISIBLE
                        productListVip.addAll(listVip) // Append to existing list

                        // Update adapter if necessary
                        productCategoryAdapter.updateAdapter(productListVip)
                    } else {
                        binding.recyclerMarketFull.visibility = View.GONE
                    }

                    binding.recyclerViewMarket.visibility = View.VISIBLE

                    // Check if search was initiated by a click and clear the list if necessary
                    if (clickSearch) {
                        productList.clear()  // Clear the list on a new search
                        clickSearch = false
                    }

                    // Append new non-VIP products to the existing list
                    productList.addAll(allListOutVip)

                    // Update adapter without resetting it each time
                    if (flagList) {
                        if (binding.recyclerViewMarket.adapter != productRowFullAdapter) {
                            binding.recyclerViewMarket.adapter = productRowFullAdapter
                            binding.recyclerViewMarket.layoutManager = linerlayout
                        }
                        productRowFullAdapter.updateAdapter(productList)
                    } else {
                        if (binding.recyclerViewMarket.adapter != productSearchCategoryAdapter) {
                            binding.recyclerViewMarket.adapter = productSearchCategoryAdapter
                            binding.recyclerViewMarket.layoutManager = gridViewLayoutManager
                        }
                        productSearchCategoryAdapter.updateAdapter(productList)
                    }

                } else {
                    // No products found
                    if (productList.isEmpty()) {
                        binding.recyclerViewMarket.visibility = View.GONE
                        binding.recyclerMarketFull.visibility = View.GONE
                        showProductApiError(getString(R.string.noProductsFound))
                    }
                }

                // Only set the category filter once
                if (!getCategoryForFirstTimeOnlyToUseInFilter) {
                    productListResp.data?.categories?.let { categories ->
                        categoriesForProductList.clear()
                        categoriesForProductList.addAll(categories)
                        filterCategoryProductsDialog.setCategories(categoriesForProductList)
                        getCategoryForFirstTimeOnlyToUseInFilter = true
                    }
                }
            }
        }

        productsListViewModel.isNetworkFailProductToFav.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.connectionError),
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        }
        productsListViewModel.errorResponseObserverProductToFav.observe(this) {
            if (it.message != null && it.message != "") {
                HelpFunctions.ShowLongToast(
                    it.message!!,
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        }
        productsListViewModel.addProductToFavObserver.observe(this) {
            if (it.status_code == 200) {
                lifecycleScope.launch(Dispatchers.IO) {
                    var selectedSimilerProduct: Product? = null
                    for (product in productList) {
                        if (product.id == added_product_id_to_fav) {
                            product.isFavourite = !product.isFavourite
                            selectedSimilerProduct = product
                            break
                        }
                    }
                    withContext(Dispatchers.Main) {
                        /**update similer product*/
                        /**update similer product*/
                        selectedSimilerProduct?.let { product ->
                            productSearchCategoryAdapter.notifyItemChanged(
                                productList.indexOf(
                                    product
                                )
                            )
                        }
                    }
                }

            }
        }
        productsListViewModel.categoryFollowRespObserver.observe(this) { categoryFolloeResp ->
            if (categoryFolloeResp.status_code == 200) {
                categoryFolloeResp.CategoryFollowList?.let {
                    for (item in it) {
                        if (item.id == categoryID) {
                            isFollowCategory = true
                            checkFollowIcon(true)
                            break
                        }
                    }
                }
            }

        }

        productsListViewModel.isFollowCategory.observe(this) { isFollow ->

            if (isFollow) {
                isFollowCategory = true
                checkFollowIcon(true)
            } else {
                isFollowCategory = false
                checkFollowIcon(false)
            }

        }

    }


    private fun showProductApiError(message: String) {
        binding.tvError.show()
        binding.tvError.text = message
    }

    private fun setViewClickListeners() {
        binding.lblToolbarCategory.setOnClickListener {
            onBackPressed()
        }

        binding.etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (binding.etSearch.text.trim().toString() == "") {
                        binding.etSearch.error =
                            getString(R.string.enter_the_name_of_the_product_you_want_to_sell)
                    } else {

                        advanceSearch()
                        binding.etSearch.setText("")
                    }
                    return true
                }
                return false
            }

        })
        binding.ivSearch.setOnClickListener {
            if (binding.etSearch.text.toString().trim() != "") {
                advanceSearch()
            } else {
                binding.etSearch.error =
                    getString(R.string.want_to_search_for_a_commodity_or_an_auction)
            }
        }
        binding.saveSearch.setOnClickListener {
            if (!HelpFunctions.isUserLoggedIn()) {
                startActivity(Intent(this, SignInActivity::class.java))
            } else {

                if (strSearch != binding.etSearch.text.toString()) {
                    productsListViewModel.saveSearch(binding.etSearch.text.toString())
                } else
                    HelpFunctions.ShowLongToast(getString(R.string.saveDone), this)


            }
        }

        binding.iconGrid.setOnClickListener {
            flagList = false
            binding.iconGrid.setImageResource(R.drawable.ic_icon_grid_active)
            binding.iconList.setImageResource(R.drawable.icon_list)

            binding.recyclerViewMarket.apply {
                productSearchCategoryAdapter.updateAdapter(productList, isHorizontal = false)

                adapter = productSearchCategoryAdapter
                layoutManager = gridViewLayoutManager
            }
        }
        binding.iconList.setOnClickListener {
            flagList = true
            binding.iconList.setImageResource(R.drawable.ic_icon_list_active)
            binding.iconGrid.setImageResource(R.drawable.icon_grid)
            binding.recyclerViewMarket.apply {
                adapter = productRowFullAdapter
                layoutManager = linerlayout
            }

        }
        binding.btnSubCatgeoryFilter.setOnClickListener {
            filterCategoryProductsDialog.show()
            filterCategoryProductsDialog.setCategories(categoriesForProductList)
            filterCategoryProductsDialog.setSelectedTap(FilterCategoryProductsDialog.subCategoryType)

        }
        binding.btnRegion.setOnClickListener {
            filterCategoryProductsDialog.show()
            filterCategoryProductsDialog.setCategories(categoriesForProductList)
            filterCategoryProductsDialog.setSelectedTap(FilterCategoryProductsDialog.regionType)

        }
        binding.btnSpecification.setOnClickListener {
            filterCategoryProductsDialog.show()
            filterCategoryProductsDialog.setCategories(categoriesForProductList)
            filterCategoryProductsDialog.setSelectedTap(FilterCategoryProductsDialog.specificationType)

        }
        binding.followCategory.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                if (isFollowCategory) {
                    productsListViewModel.removeFollow(categoryID, this)
                } else {
                    productsListViewModel.followCategoryAPI(categoryID, this)
                }
            } else {
                goToSignInActivity()
            }
        }

    }

    fun advanceSearch() {
        clickSearch = true
        productName = binding.etSearch.text.toString()
        productsListViewModel.searchForProduct(
            categoryID,
            if (isTextInEnglish(productName.toString())) "en" else "ar",
            1,
            countryList,
            regionList,
            neighoodList,
            subCategoryList,
            specificationList,
            startPrice,
            endProce,
            productName,
            comeFrom
        )

        hideSoftKeyboard(binding.etSearch)
    }

    private fun setProductSearchCategoryAdapter() {
        productList = ArrayList()
        productListVip = arrayListOf()
        binding.iconList.setImageResource(R.drawable.ic_icon_list_active)
        binding.iconGrid.setImageResource(R.drawable.icon_grid)
        gridViewLayoutManager = GridLayoutManager(this, 2)
        linerlayout = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        productSearchCategoryAdapter = ProductHorizontalAdapter(productList, this, 0, false, false)
        productCategoryAdapter = ProductHorizontalAdapter(productList, this, 0, false, false)
        productRowFullAdapter = ProductRowFullAdapter(arrayListOf(), 0, this)

        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(linerlayout) {
                override fun onLoadMore(_page: Int, totalItemsCount: Int, view: RecyclerView) {
                    page++
                    productsListViewModel.searchForProduct(
                        categoryID,
                        ConstantObjects.currentLanguage,
                        page,
                        countryList,
                        regionList,
                        neighoodList,
                        subCategoryList,
                        specificationList,
                        startPrice,
                        endProce,
                        productName,
                        comeFrom
                    )
                }
            }
        endlessRecyclerViewScrollListener2 =
            object : EndlessRecyclerViewScrollListener(gridViewLayoutManager) {
                override fun onLoadMore(_page: Int, totalItemsCount: Int, view: RecyclerView) {
                    page++
                    productsListViewModel.searchForProduct(
                        categoryID,
                        ConstantObjects.currentLanguage,
                        page,
                        countryList,
                        regionList,
                        neighoodList,
                        subCategoryList,
                        specificationList,
                        startPrice,
                        endProce,
                        productName,
                        comeFrom
                    )
                }
            }
        binding.recyclerViewMarket.addOnScrollListener(endlessRecyclerViewScrollListener)
        binding.recyclerViewMarket.addOnScrollListener(endlessRecyclerViewScrollListener2)
    }

    override fun onRefresh() {
        binding.tvError.hide()
        getResetAll()


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

    override fun onProductSelect(position: Int, productID: Int, categoryID: Int) {
        productDetailsLauncher.launch(Intent(this, ProductDetailsActivity::class.java).apply {
            putExtra(ConstantObjects.productIdKey, productID)
            putExtra("Template", "")
        })
    }

    private fun refreshFavProductStatus(productId: Int, productFavStatusKey: Boolean) {
        /***for similer product*/
        lifecycleScope.launch(Dispatchers.IO) {
            var selectedSimilerProduct: Product? = null
            for (product in productList) {
                if (product.id == productId) {
                    product.isFavourite = productFavStatusKey
                    selectedSimilerProduct = product
                    break
                }
            }
            withContext(Dispatchers.Main) {
                /**update similer product*/
                selectedSimilerProduct?.let { product ->
                    productSearchCategoryAdapter.notifyItemChanged(
                        productList.indexOf(
                            product
                        )
                    )
                }
            }
        }
    }

    override fun onAddProductToFav(position: Int, productID: Int, categoryID: Int) {
        if (HelpFunctions.isUserLoggedIn()) {
            lastUpdateIndex = position
            added_product_id_to_fav = productList[position].id
            productsListViewModel.addProductToFav(productList[position].id)

        } else {
            startActivity(
                Intent(
                    this,
                    SignInActivity::class.java
                ).apply {})
        }
    }

    override fun onShowMoreSetting(position: Int, productID: Int, categoryID: Int) {

    }

    override fun onApplyFilter(
        countryList: List<Int>,
        regionList: List<Int>,
        neighoodList: List<Int>,
        subCategoryList: List<Int>,
        specificationList: List<String>,
        startPrice: Float,
        endProce: Float,
        mainCategoryId: Int
    ) {
        productList = arrayListOf()
        productSearchCategoryAdapter.notifyDataSetChanged()
        this.countryList = countryList
        this.regionList = regionList
        this.neighoodList = neighoodList
        this.subCategoryList = subCategoryList
        this.specificationList = specificationList
        this.startPrice = startPrice
        this.endProce = endProce
        this.categoryID = mainCategoryId
        applyFilterAll()
        // productsListViewModel.searchForProduct(categoryID, ConstantObjects.currentLanguage, 1,countryList,regionList,neighoodList,subCategoryList,specificationList,startPrice,endProce)
    }

    override fun resetFilter() {
        productList = arrayListOf()
        productSearchCategoryAdapter.notifyDataSetChanged()
        this.countryList = ArrayList()
        this.regionList = ArrayList()
        this.neighoodList = ArrayList()
        this.subCategoryList = ArrayList()
        this.specificationList = ArrayList()
        this.startPrice = 0f
        this.endProce = 0f
        when (comeFrom) {
            ConstantObjects.search_categoriesDetails -> {

            }

            else -> {
                this.categoryID = 0
            }
        }
        applyFilterAll()
        //  productsListViewModel.searchForProduct(categoryID, ConstantObjects.currentLanguage, 1,countryList,regionList,neighoodList,subCategoryList,specificationList,startPrice,endProce)

    }

    fun getErrorResponse(body: ResponseBody?): ErrorResponse {
        try {
            val adapter: TypeAdapter<ErrorResponse> =
                Gson().getAdapter<ErrorResponse>(ErrorResponse::class.java)
            return adapter.fromJson(body?.string())
        } catch (e: Exception) {
            return ErrorResponse()
        }
    }


    private fun checkFollowIcon(isFollow: Boolean) {
        val img = if (isFollow) {
            ContextCompat.getDrawable(
                this,
                R.drawable.notification
            )
        } else {
            ContextCompat.getDrawable(
                this,
                R.drawable.notification_log
            )
        }
        binding.followCategory.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        productsListViewModel.closeAllCall()
        productsListViewModel.baseCancel()
        productSearchCategoryAdapter.onDestroyHandler()
        productCategoryAdapter.onDestroyHandler()
    }

    fun applyFilterAll() {
        endlessRecyclerViewScrollListener.resetState()
        endlessRecyclerViewScrollListener2.resetState()
        binding.swipeToRefresh.isRefreshing = false
        productList = arrayListOf()
        when (comeFrom) {
            ConstantObjects.search_categoriesDetails -> {
                binding.followCategory.show()
                if (HelpFunctions.isUserLoggedIn())
                    productsListViewModel.getCategoryFollow()


                Log.i("test #1", "countryList: $countryList")
                Log.i("test #1", "regionList: $regionList")
                Log.i("test #1", "neighoodList: $neighoodList")

                productsListViewModel.searchForProduct(
                    categoryID,
                    ConstantObjects.currentLanguage,
                    1,
                    countryList,
                    regionList,
                    neighoodList,
                    subCategoryList,
                    specificationList,
                    startPrice,
                    endProce,
                    productName,
                    comeFrom
                )
            }

            ConstantObjects.search_product -> {
                binding.followCategory.hide()
                binding.lblToolbarCategory.text = productName
                productsListViewModel.searchForProduct(
                    categoryID,
                    ConstantObjects.currentLanguage,
                    1,
                    countryList,
                    regionList,
                    neighoodList,
                    subCategoryList,
                    specificationList,
                    startPrice,
                    endProce,
                    productName,
                    comeFrom
                )
            }
        }
    }

    private fun getResetAll() {
        endlessRecyclerViewScrollListener.resetState()
        endlessRecyclerViewScrollListener2.resetState()
        binding.swipeToRefresh.isRefreshing = false
        productList = arrayListOf()
        when (comeFrom) {
            ConstantObjects.search_categoriesDetails -> {
                binding.followCategory.show()
                if (HelpFunctions.isUserLoggedIn())
                    productsListViewModel.getCategoryFollow()
                productsListViewModel.searchForProduct(
                    categoryID,
                    ConstantObjects.currentLanguage,
                    1,
                    arrayListOf(),
                    arrayListOf(),
                    arrayListOf(),
                    arrayListOf(),
                    arrayListOf(),
                    startPrice,
                    endProce,
                    null,
                    comeFrom
                )
            }

            ConstantObjects.search_product -> {
                binding.followCategory.hide()
                binding.etSearch.setText(productName)
                binding.lblToolbarCategory.text = productName
                productsListViewModel.searchForProduct(
                    categoryID,
                    ConstantObjects.currentLanguage,
                    1,
                    arrayListOf(),
                    arrayListOf(),
                    arrayListOf(),
                    arrayListOf(),
                    arrayListOf(),
                    startPrice,
                    endProce,
                    productName,
                    comeFrom
                )
            }
        }

    }

}