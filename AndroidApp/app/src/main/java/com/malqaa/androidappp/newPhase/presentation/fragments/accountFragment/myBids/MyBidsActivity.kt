package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myBids

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malqaa.androidappp.newPhase.data.network.service.SetOnProductItemListeners
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.ProductDetailsActivity
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.CategoryProductViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import kotlinx.android.synthetic.main.activity_my_bids.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyBidsActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener,
    SetOnProductItemListeners {
    lateinit var productAdapter: ProductHorizontalAdapter
    private lateinit var productList: ArrayList<Product>
    var added_product_id_to_fav = 0
    var lastUpdateIndex: Int = -1
    private lateinit var productsListViewModel: CategoryProductViewModel
    lateinit var gridViewLayoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bids)
        toolbar_title.text = getString(R.string.my_bids)
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        setProductSearchCategoryAdapter()
        setVeiwClickListeners()
        setupViewModel()
        onRefresh()
    }

    private fun setVeiwClickListeners() {
        back_btn.setOnClickListener {
            finish()
        }
    }

    private fun setProductSearchCategoryAdapter() {
        productList = ArrayList()
        gridViewLayoutManager = GridLayoutManager(this, 2)
        productAdapter = ProductHorizontalAdapter(productList, this, 0, false, false)
        rvProduct.apply {
            adapter = productAdapter
            layoutManager = gridViewLayoutManager
        }
    }

    private fun setupViewModel() {
        productsListViewModel = ViewModelProvider(this).get(CategoryProductViewModel::class.java)
        productsListViewModel.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
        }

        productsListViewModel.isNetworkFail.observe(this) {
            if (it) {
                showProductApiError(getString(R.string.connectionError))
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        productsListViewModel.errorResponseObserver.observe(this) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
                if (it.message != null) {
                    showProductApiError(it.message!!)
                } else {
                    showProductApiError(getString(R.string.serverError))
                }
            }

        }
        productsListViewModel.productListRespObserver.observe(this) { productListResp ->
            if (productListResp.status_code == 200) {
                if (!productListResp.productList.isNullOrEmpty()) {
                    productList.clear()
                    productList.addAll(productListResp.productList)
                    productAdapter.notifyDataSetChanged()
                } else {
                    if (productList.isEmpty())
                        showProductApiError(getString(R.string.noProductsAdded))
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
                        selectedSimilerProduct?.let { product ->
                            productAdapter.notifyItemChanged(
                                productList.indexOf(
                                    product
                                )
                            )
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

    override fun onRefresh() {
        swipe_to_refresh.isRefreshing = false
        productList.clear()
        productAdapter.notifyDataSetChanged()
        productsListViewModel.getMyBids()
    }

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
                selectedSimilerProduct?.let { product ->
                    productAdapter.notifyItemChanged(
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

    override fun onDestroy() {
        super.onDestroy()
        productAdapter.onDestroyHandler()
        productsListViewModel.closeAllCall()
        productsListViewModel.baseCancel()
    }
}