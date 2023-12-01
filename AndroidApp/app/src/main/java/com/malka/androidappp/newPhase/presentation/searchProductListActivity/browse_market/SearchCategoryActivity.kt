package com.malka.androidappp.newPhase.presentation.searchProductListActivity.browse_market

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.EndlessRecyclerViewScrollListener
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.domain.enums.ProductPosition
import com.malka.androidappp.newPhase.domain.models.ErrorResponse
import com.malka.androidappp.newPhase.domain.models.categoryFollowResp.AddFollowObj
import com.malka.androidappp.newPhase.domain.models.productResp.CategoriesSearchItem
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malka.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malka.androidappp.newPhase.presentation.adapterShared.SetOnProductItemListeners
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.ProductDetailsActivity
import com.malka.androidappp.newPhase.presentation.searchProductListActivity.CategoryProductViewModel
import com.malka.androidappp.newPhase.presentation.searchProductListActivity.ProductRowFullAdapter
import com.malka.androidappp.newPhase.presentation.searchProductListActivity.browse_market.filterDialog.FilterCategoryProductsDialog
import kotlinx.android.synthetic.main.fragment_browse_market.btnRegion
import kotlinx.android.synthetic.main.fragment_browse_market.btnSpecification
import kotlinx.android.synthetic.main.fragment_browse_market.btnSubCatgeoryFilter
import kotlinx.android.synthetic.main.fragment_browse_market.fbButtonBack
import kotlinx.android.synthetic.main.fragment_browse_market.follow_category
import kotlinx.android.synthetic.main.fragment_browse_market.icon_grid
import kotlinx.android.synthetic.main.fragment_browse_market.icon_list
import kotlinx.android.synthetic.main.fragment_browse_market.lbl_toolbar_category
import kotlinx.android.synthetic.main.fragment_browse_market.progressBar
import kotlinx.android.synthetic.main.fragment_browse_market.progressBarMore
import kotlinx.android.synthetic.main.fragment_browse_market.recyclerMarketFull
import kotlinx.android.synthetic.main.fragment_browse_market.recyclerViewMarket
import kotlinx.android.synthetic.main.fragment_browse_market.swipe_to_refresh
import kotlinx.android.synthetic.main.fragment_browse_market.tvError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchCategoryActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener,
    SetOnProductItemListeners, FilterCategoryProductsDialog.SetOnClickListeners {

    var categoryID: Int = 0
    var categoryName =""
    var countryList: List<Int> = ArrayList()
    var regionList: List<Int> = ArrayList()
    var neighoodList: List<Int> = ArrayList()
    var subCategoryList: List<Int> = ArrayList()
    var specificationList: List<String> = ArrayList()
    var startPrice: Float = 0f
    var endProce: Float = 0f
    var comeFrom = 1
    private var flagList = true

    lateinit var productSearchCategoryAdapter: ProductHorizontalAdapter
    lateinit var productCategoryAdapter: ProductHorizontalAdapter
    lateinit var productRowFullAdapter: ProductRowFullAdapter

    lateinit var gridViewLayoutManager: GridLayoutManager
    lateinit var linerlayout: LinearLayoutManager
    lateinit var filterCategoryProductsDialog: FilterCategoryProductsDialog
    private lateinit var productsListViewModel: CategoryProductViewModel
    private lateinit var productList: ArrayList<Product>
    private lateinit var productListVip: ArrayList<Product>
    private var lastUpdateIndex = -1
    var categoryQuery = "mainCatId="
    var pageCountQuery = "PageRowsCount=10"
    var pageIndexQuery = "pageIndex="
    var langQuery = "lang="
    var queryString = ""
    var productName: String? = null
    var added_product_id_to_fav = 0
    var isLoading = false

    lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    lateinit var endlessRecyclerViewScrollListener2: EndlessRecyclerViewScrollListener
    var isFollowCategory: Boolean = false
    var categoriesForProductList: ArrayList<CategoriesSearchItem> = ArrayList()
    var getCategoryForFirstTimeOnlyToUseInFilter = false
    var page=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_browse_market)
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        setProductSearchCategoryAdapter()
        setViewClickListeners()

        categoryName = intent.getStringExtra("CategoryDesc")?:"CategoryName"
        categoryID = intent.getIntExtra("CategoryID", 0)
        comeFrom = intent.getIntExtra("ComeFrom", 0)
        productName = intent.getStringExtra("productName")
        lbl_toolbar_category.text=categoryName
        println("hhhh " + categoryID)
        filterCategoryProductsDialog = FilterCategoryProductsDialog(
            this,
            FilterCategoryProductsDialog.subCategoryType,
            categoryID,
            this,
            comeFrom
        )
        filterCategoryProductsDialog.setCategories(categoriesForProductList)
        setupViewModel()
//        queryString =
//            "${categoryQuery}${categoryID}&${pageCountQuery}&${langQuery}${ConstantObjects.currentLanguage}&${pageIndexQuery}${1}"
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
        onRefresh()
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
        productsListViewModel.searchProductListRespObserver.observe(this) { productListResp ->
            if (productListResp.status_code == 200) {

                if (productListResp.data?.products != null && productListResp.data.products.isNotEmpty()) {

                    val listVip =
                        productListResp.data.products.filter { it.productPosition == ProductPosition.Vip.value }
                    val allListOutVip =
                        productListResp.data.products.filter { it.productPosition != ProductPosition.Vip.value }


                    if (listVip.isNotEmpty()) {
                        productListVip.addAll(listVip)
                        recyclerMarketFull.adapter =productCategoryAdapter
                        productCategoryAdapter.updateAdapter(productListVip)
                    }

                    productList.addAll(allListOutVip)

                    if(flagList){
                        recyclerViewMarket.apply {
                            adapter = productRowFullAdapter
                            layoutManager = linerlayout
                        }
                        productRowFullAdapter.updateAdapter(productList)
                    }else{
                        recyclerViewMarket.apply {
                            adapter = productSearchCategoryAdapter
                            layoutManager = gridViewLayoutManager
                        }
                        productSearchCategoryAdapter.updateAdapter(productList)
                    }

                } else {
                    if (productList.isEmpty())
                        showProductApiError(getString(R.string.noProductsFound))
                }

                if (!getCategoryForFirstTimeOnlyToUseInFilter) {
                    if (productListResp.data?.categories != null) {
                        categoriesForProductList.clear()
                        categoriesForProductList.addAll(productListResp.data?.categories)
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
//                if (lastUpdateIndex < productList.size) {
//                    productList[lastUpdateIndex].isFavourite =
//                        !productList[lastUpdateIndex].isFavourite
//                    productSearchCategoryAdapter.notifyItemChanged(lastUpdateIndex)
//                    productSearchCategoryAdapter.notifyDataSetChanged()
//                }
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

    }



    private fun showProductApiError(message: String) {
        tvError.show()
        tvError.text = message
    }

    private fun setViewClickListeners() {
        lbl_toolbar_category.setOnClickListener {
            onBackPressed()
        }

        icon_grid.setOnClickListener {
            //browadptxl!!.updateLayout(true)
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
            //browadptxl!!.updateLayout(false)
            flagList = true
            icon_list.setImageResource(R.drawable.ic_icon_list_active)
            icon_grid.setImageResource(R.drawable.icon_grid)
            recyclerViewMarket.apply {
                adapter = productRowFullAdapter
                layoutManager = linerlayout
            }

        }
        btnSubCatgeoryFilter.setOnClickListener {
            //GetSubCategoryByMainCategory(CategoryID)

            filterCategoryProductsDialog.show()
            filterCategoryProductsDialog.setCategories(categoriesForProductList)
            filterCategoryProductsDialog.setSelectedTap(FilterCategoryProductsDialog.subCategoryType)

        }
        btnRegion.setOnClickListener {
            //GetSubCategoryByMainCategory(CategoryID)
            // var filterCategoryProductsDialog = FilterCategoryProductsDialog(this,FilterCategoryProductsDialog.regionType,categoryID)

            filterCategoryProductsDialog.show()
            filterCategoryProductsDialog.setCategories(categoriesForProductList)
            filterCategoryProductsDialog.setSelectedTap(FilterCategoryProductsDialog.regionType)

        }
        btnSpecification.setOnClickListener {
            //GetSubCategoryByMainCategory(CategoryID)
            // var filterCategoryProductsDialog = FilterCategoryProductsDialog(this,FilterCategoryProductsDialog.specificationType,categoryID)
            filterCategoryProductsDialog.show()
            filterCategoryProductsDialog.setCategories(categoriesForProductList)
            filterCategoryProductsDialog.setSelectedTap(FilterCategoryProductsDialog.specificationType)

        }
        follow_category.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                if (isFollowCategory) {
                    RemoveFollow()
                } else {
                    FollowCategoryAPI()
                }
            } else {
                goToSignInActivity()
            }
        }

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

//        recyclerViewMarket.apply {
//            adapter = productSearchCategoryAdapter
//        }

        productRowFullAdapter = ProductRowFullAdapter(arrayListOf(),0,this)
//
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(linerlayout) {
                override fun onLoadMore(_page: Int, totalItemsCount: Int, view: RecyclerView) {
                    page++
                    println("hhh page liner  " + page)
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
                    println("hhh page liner  " + page)
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

    /**open activity product detials functions**/
    val productDetailsLauncher =
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
        onRefresh()
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
        onRefresh()
        //  productsListViewModel.searchForProduct(categoryID, ConstantObjects.currentLanguage, 1,countryList,regionList,neighoodList,subCategoryList,specificationList,startPrice,endProce)

    }


    /***********
     * ***********
     * *********
     * *********
     * ******
     * *******/
    private fun FollowCategoryAPI() {
        HelpFunctions.startProgressBar(this)
        val malqa: MalqaApiService = getRetrofitBuilder()
        val call = malqa.addFollow(
            AddFollowObj(arrayListOf(categoryID))
        )
        // println("hhhh "+arrayListOf(categoryID))
        call.enqueue(object : Callback<GeneralResponse?> {
            override fun onFailure(call: Call<GeneralResponse?>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
            }

            override fun onResponse(
                call: Call<GeneralResponse?>,
                response: Response<GeneralResponse?>
            ) {
                if (response.isSuccessful) {
                    println("tttt add " + Gson().toJson(response.body()))
                    if (response.body() != null) {
//                        HelpFunctions.ShowLongToast(
//                            getString(R.string.follow_catgeory),
//                            this@SearchCategoryActivity
//                        )
                        isFollowCategory = true
                        checkFollowIcon(true)
                    }
                } else {
                    val errResponse: ErrorResponse? = getErrorResponse(response.errorBody())
                    if (errResponse?.message == "Categories already exists") {
                        isFollowCategory = true
                        checkFollowIcon(true)
                    }
                }
                HelpFunctions.dismissProgressBar()
            }
        })
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

    private fun RemoveFollow() {
        HelpFunctions.startProgressBar(this)
        val malqa: MalqaApiService = getRetrofitBuilder()
        val call = malqa.removeFollow(categoryID)
        call.enqueue(object : Callback<GeneralResponse?> {
            override fun onFailure(call: Call<GeneralResponse?>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
            }

            override fun onResponse(
                call: Call<GeneralResponse?>,
                response: Response<GeneralResponse?>
            ) {
                println("tttt remove " + Gson().toJson(response.body()))
                if (response.isSuccessful) {
                    println("tttt remove " + Gson().toJson(response.body()))
                    if (response.body() != null) {
//                        HelpFunctions.ShowLongToast(
//                            getString(R.string.un_follow_catgeory),
//                            this@SearchCategoryActivity
//                        )
                        isFollowCategory = false
                        checkFollowIcon(false)

                    }

                }
                HelpFunctions.dismissProgressBar()
            }
        })
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
        // follow_category.setCompoundDrawables(img, null, null, null)
        follow_category.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)

    }


//


    // Add Search to favorites
//    fun addSearchQueryFav(searchQuery: String) {
//        try {
//
//            val malqaa: MalqaApiService = getRetrofitBuilder()
//
//            val call: Call<ModelAddSearchFav> = malqaa.addSearchFav(
//                ModelAddSearchFav(
//                    loggedInUserId = ConstantObjects.logged_userid,
//                    searchQuery = searchQuery
//                )
//            )
//
//            call.enqueue(object : Callback<ModelAddSearchFav> {
//                override fun onResponse(
//                    call: Call<ModelAddSearchFav>, response: Response<ModelAddSearchFav>
//                ) {
//
//                }
//
//                override fun onFailure(call: Call<ModelAddSearchFav>, t: Throwable) {
//                    t.message?.let { HelpFunctions.ShowLongToast(it, this@SearchCategoryActivity) }
//                }
//            })
//        } catch (ex: Exception) {
//            HelpFunctions.ReportError(ex)
//
//        }
//    }


}