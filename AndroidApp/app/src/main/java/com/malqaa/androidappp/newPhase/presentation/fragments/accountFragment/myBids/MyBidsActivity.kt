package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myBids

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityMyBidsBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.data.network.service.SetOnProductItemListeners
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.domain.models.winningBidsResponse.BidModel
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.ProductDetailsActivity
import com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.CategoryProductViewModel
import com.malqaa.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malqaa.androidappp.newPhase.presentation.adapterShared.WinningBidsAdapter
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyBidsActivity : BaseActivity<ActivityMyBidsBinding>(), SwipeRefreshLayout.OnRefreshListener,
    SetOnProductItemListeners {
    lateinit var productAdapter: ProductHorizontalAdapter
    lateinit var winningBidsAdapter: WinningBidsAdapter
    private lateinit var productList: ArrayList<Product>
    private lateinit var winningBidsList: ArrayList<BidModel>

    var added_product_id_to_fav = 0
    var lastUpdateIndex: Int = -1
    private lateinit var productsListViewModel: CategoryProductViewModel
    lateinit var gridViewLayoutManager: GridLayoutManager
    private var tapId: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityMyBidsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = getString(R.string.my_bids)
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)
        setProductSearchCategoryAdapter()
        setVeiwClickListeners()
        setupViewModel()
        onRefresh()
        binding.rvProduct.show()
        binding.rvWinningBids.hide()
    }

    private fun setVeiwClickListeners() {
        binding.toolbarMain.backBtn.setOnClickListener {
            finish()
        }
        binding.activeBids.setOnClickListener {
            tapId = 1
            binding.activeBids.background = ContextCompat.getDrawable(
                this,
                R.drawable.round_btn
            )
            binding.activeBids.setTextColor(Color.parseColor("#FFFFFF"))
            binding.winningBids.setTextColor(Color.parseColor("#45495E"))
            binding.winningBids.background = null
            binding.rvWinningBids.hide()
            binding.rvProduct.show()
            onRefresh()
        }
        binding.winningBids.setOnClickListener {
            tapId = 2
            binding.winningBids.background = ContextCompat.getDrawable(
                this,
                R.drawable.round_btn
            )
            binding.winningBids.setTextColor(Color.parseColor("#FFFFFF"))
            binding.activeBids.setTextColor(Color.parseColor("#45495E"))
            binding.activeBids.background = null
            binding.rvWinningBids.show()
            binding.rvProduct.hide()
            onRefresh()
        }
    }

    private fun setProductSearchCategoryAdapter() {
        productList = ArrayList()
        productAdapter = ProductHorizontalAdapter(productList, this, 0, false, false)
        productAdapter.setIsGridLayout(true)
        binding.rvProduct.apply {
            gridViewLayoutManager = GridLayoutManager(this@MyBidsActivity, 2)
            adapter = productAdapter
            layoutManager = gridViewLayoutManager
        }
        winningBidsList = ArrayList()
        winningBidsAdapter = WinningBidsAdapter(bidModel = winningBidsList,this,true)
        binding.rvWinningBids.apply {
            adapter = winningBidsAdapter
            layoutManager = LinearLayoutManager(this@MyBidsActivity)
        }
    }

    private fun setupViewModel() {
        productsListViewModel = ViewModelProvider(this).get(CategoryProductViewModel::class.java)
        productsListViewModel.isLoading.observe(this) {
            if (it)
                binding.progressBar.show()
            else
                binding.progressBar.hide()
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
        productsListViewModel.winningRespObserver.observe(this) { winningBidsResp ->
            if (winningBidsResp.status_code == 200) {
                if (!winningBidsResp.bidModel.isNullOrEmpty()) {
                    winningBidsList.clear()
                    winningBidsList.addAll(winningBidsResp.bidModel)
                    winningBidsAdapter.notifyDataSetChanged()
                } else {
                    if (winningBidsList.isEmpty())
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
        binding.tvError.show()
        binding.tvError.text = message
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onRefresh() {
        binding.swipeToRefresh.isRefreshing = false
        binding.tvError.hide()
        when(tapId){
            1->{
                productList.clear()
                productAdapter.notifyDataSetChanged()
                productsListViewModel.getMyBids()

            }
            2->{
                winningBidsList.clear()
                winningBidsAdapter.notifyDataSetChanged()
                productsListViewModel.getMyWinningBids()
            }

        }

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

    override fun onProductSelect(position: Int,productID:Int,categoryID:Int,userId:String,providerId:String,businessAccountId:String) {
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