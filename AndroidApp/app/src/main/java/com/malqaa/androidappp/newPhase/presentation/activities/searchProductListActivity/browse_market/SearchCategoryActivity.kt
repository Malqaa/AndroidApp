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
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.EndlessRecyclerViewScrollListener
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.domain.enums.ProductPosition
import com.malqaa.androidappp.newPhase.domain.models.ErrorResponse
import com.malqaa.androidappp.newPhase.domain.models.productResp.CategoriesSearchItem
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malqaa.androidappp.newPhase.data.network.service.SetOnProductItemListeners
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.ProductDetailsActivity
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.CategoryProductViewModel
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.ProductRowFullAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog.FilterCategoryProductsDialog
import kotlinx.android.synthetic.main.fragment_browse_market.btnRegion
import kotlinx.android.synthetic.main.fragment_browse_market.btnSpecification
import kotlinx.android.synthetic.main.fragment_browse_market.btnSubCatgeoryFilter
import kotlinx.android.synthetic.main.fragment_browse_market.etSearch
import kotlinx.android.synthetic.main.fragment_browse_market.follow_category
import kotlinx.android.synthetic.main.fragment_browse_market.icon_grid
import kotlinx.android.synthetic.main.fragment_browse_market.icon_list
import kotlinx.android.synthetic.main.fragment_browse_market.imgFollow
import kotlinx.android.synthetic.main.fragment_browse_market.ivSearch
import kotlinx.android.synthetic.main.fragment_browse_market.layCategory
import kotlinx.android.synthetic.main.fragment_browse_market.laySearch
import kotlinx.android.synthetic.main.fragment_browse_market.lbl_toolbar_category
import kotlinx.android.synthetic.main.fragment_browse_market.progressBar
import kotlinx.android.synthetic.main.fragment_browse_market.progressBarMore
import kotlinx.android.synthetic.main.fragment_browse_market.recyclerMarketFull
import kotlinx.android.synthetic.main.fragment_browse_market.recyclerViewMarket
import kotlinx.android.synthetic.main.fragment_browse_market.saveSearch
import kotlinx.android.synthetic.main.fragment_browse_market.swipe_to_refresh
import kotlinx.android.synthetic.main.fragment_browse_market.total_result_tv
import kotlinx.android.synthetic.main.fragment_browse_market.tvError
import kotlinx.android.synthetic.main.fragment_homee.textInputLayout11
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody


class SearchCategoryActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener,
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
        setContentView(R.layout.fragment_browse_market)
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        setProductSearchCategoryAdapter()
        setViewClickListeners()

        typeView = intent.getStringExtra("typeView") ?: ""
        if (typeView == "SearchHome") {
            comeFrom = 3
            categoryID = 0
            follow_category.visibility = View.GONE
            laySearch.visibility = View.VISIBLE
        } else {
            categoryName = intent.getStringExtra("CategoryDesc") ?: "CategoryName"
            categoryID = intent.getIntExtra("CategoryID", 0)
            comeFrom = intent.getIntExtra("ComeFrom", 0)
            laySearch.visibility = View.GONE
            follow_category.visibility = View.VISIBLE
        }
        productName = intent.getStringExtra("productName")

        lbl_toolbar_category.text = categoryName
        setupViewModel()
        when (comeFrom) {
            ConstantObjects.search_categoriesDetails -> {
                btnSubCatgeoryFilter.text = getText(R.string.sub_categories)
            }

            ConstantObjects.search_product -> {
                btnSubCatgeoryFilter.text = getText(R.string.Categories)
            }

            ConstantObjects.search_seller -> {
                btnSubCatgeoryFilter.text = getText(R.string.Categories)
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
                progressBar.show()
            else
                progressBar.hide()
        }


        productsListViewModel.isloadingMore.observe(this) {
            if (it)
                progressBarMore.show()
            else
                progressBarMore.hide()
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
                    strSearch = etSearch.text.toString()
                    HelpFunctions.ShowLongToast(getString(R.string.saveDone), this)
                    imgFollow.setImageResource(R.drawable.notification)
                }
            }
        }
        productsListViewModel.searchProductListRespObserver.observe(this) { productListResp ->
            if (productListResp.status_code == 200) {

                if (productListResp.data?.products != null && productListResp.data.products.isNotEmpty()) {

                    val listVip =
                        productListResp.data.products.filter { it.productPosition == ProductPosition.Vip.value }
                    val allListOutVip =
                        productListResp.data.products.filter { it.productPosition != ProductPosition.Vip.value }

                    total_result_tv.text =
                        "" + productListResp.totaRecords + " " + getString(R.string.results)

                    if (listVip.isNotEmpty()) {
                        recyclerMarketFull.visibility = View.VISIBLE
                        productListVip.addAll(listVip)
                        recyclerMarketFull.adapter = productCategoryAdapter
                        productCategoryAdapter.updateAdapter(productListVip)
                    } else {
                        recyclerMarketFull.visibility = View.GONE
                    }

                    recyclerViewMarket.visibility = View.VISIBLE
                    if (clickSearch) {
                        productList.clear()
                        clickSearch = false
                    }
                    productList.addAll(allListOutVip)

                    if (flagList) {
                        recyclerViewMarket.apply {
                            adapter = productRowFullAdapter
                            layoutManager = linerlayout
                        }

                        productRowFullAdapter.updateAdapter(productList)
                    } else {
                        recyclerViewMarket.apply {
                            adapter = productSearchCategoryAdapter
                            layoutManager = gridViewLayoutManager
                        }
                        productSearchCategoryAdapter.updateAdapter(productList)
                    }

                } else {
                    if (productList.isEmpty()) {
                        recyclerViewMarket.visibility = View.GONE
                        recyclerMarketFull.visibility = View.GONE
                        showProductApiError(getString(R.string.noProductsFound))
                    }
                }

                if (!getCategoryForFirstTimeOnlyToUseInFilter) {
                    if (productListResp.data?.categories != null) {
                        categoriesForProductList.clear()
                        categoriesForProductList.addAll(productListResp.data.categories)
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
        tvError.show()
        tvError.text = message
    }

    private fun setViewClickListeners() {
        lbl_toolbar_category.setOnClickListener {
            onBackPressed()
        }

        etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (etSearch.text.trim().toString() == "") {
                        etSearch.error =
                            getString(R.string.enter_the_name_of_the_product_you_want_to_sell)
                    } else {

                        advanceSearch()
                        etSearch.setText("")
                    }
                    return true
                }
                return false
            }

        })
        ivSearch.setOnClickListener {
            if (etSearch.text.toString().trim() != "") {
                advanceSearch()
//                homeViewModel.doSearch(mapOf("productName" to etSearch.text.toString().trim()))
            } else {
                etSearch.error = getString(R.string.want_to_search_for_a_commodity_or_an_auction)
            }
        }
        saveSearch.setOnClickListener {
            if (!HelpFunctions.isUserLoggedIn()) {
                startActivity(Intent(this, SignInActivity::class.java))
            } else {

                if (strSearch != etSearch.text.toString()) {
                    productsListViewModel.saveSearch(etSearch.text.toString())
                } else
                    HelpFunctions.ShowLongToast(getString(R.string.saveDone), this)


            }
        }

        icon_grid.setOnClickListener {
            flagList = false
            icon_grid.setImageResource(R.drawable.ic_icon_grid_active)
            icon_list.setImageResource(R.drawable.icon_list)

            recyclerViewMarket.apply {
                productSearchCategoryAdapter.updateAdapter(productList, isHorizontal = false)

                adapter = productSearchCategoryAdapter
                layoutManager = gridViewLayoutManager
            }
        }
        icon_list.setOnClickListener {
            flagList = true
            icon_list.setImageResource(R.drawable.ic_icon_list_active)
            icon_grid.setImageResource(R.drawable.icon_grid)
            recyclerViewMarket.apply {
                adapter = productRowFullAdapter
                layoutManager = linerlayout
            }

        }
        btnSubCatgeoryFilter.setOnClickListener {
            filterCategoryProductsDialog.show()
            filterCategoryProductsDialog.setCategories(categoriesForProductList)
            filterCategoryProductsDialog.setSelectedTap(FilterCategoryProductsDialog.subCategoryType)

        }
        btnRegion.setOnClickListener {
            filterCategoryProductsDialog.show()
            filterCategoryProductsDialog.setCategories(categoriesForProductList)
            filterCategoryProductsDialog.setSelectedTap(FilterCategoryProductsDialog.regionType)

        }
        btnSpecification.setOnClickListener {
            filterCategoryProductsDialog.show()
            filterCategoryProductsDialog.setCategories(categoriesForProductList)
            filterCategoryProductsDialog.setSelectedTap(FilterCategoryProductsDialog.specificationType)

        }
        follow_category.setOnClickListener {
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
        productName = etSearch.text.toString()
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

        hideSoftKeyboard(etSearch)
    }

    private fun setProductSearchCategoryAdapter() {
        productList = ArrayList()
        productListVip = arrayListOf()
        icon_list.setImageResource(R.drawable.ic_icon_list_active)
        icon_grid.setImageResource(R.drawable.icon_grid)
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
        recyclerViewMarket.addOnScrollListener(endlessRecyclerViewScrollListener)
        recyclerViewMarket.addOnScrollListener(endlessRecyclerViewScrollListener2)
    }

    override fun onRefresh() {
        tvError.hide()
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
        this.regionList = countryList
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
        follow_category.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)

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
        swipe_to_refresh.isRefreshing = false
        productList = arrayListOf()
        when (comeFrom) {
            ConstantObjects.search_categoriesDetails -> {
                follow_category.show()
                if (HelpFunctions.isUserLoggedIn())
                    productsListViewModel.getCategoryFollow()
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
                follow_category.hide()
                lbl_toolbar_category.text = productName
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
        swipe_to_refresh.isRefreshing = false
        productList = arrayListOf()
        when (comeFrom) {
            ConstantObjects.search_categoriesDetails -> {
                follow_category.show()
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
                follow_category.hide()
                etSearch.setText(productName)
                lbl_toolbar_category.text = productName
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