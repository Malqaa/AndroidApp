package com.malka.androidappp.newPhase.presentation.searchActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malka.androidappp.newPhase.presentation.adapterShared.SetOnProductItemListeners
import com.malka.androidappp.newPhase.presentation.homeScreen.viewModel.HomeViewModel
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.ProductDetailsActivity
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : BaseActivity(),
    SetOnProductItemListeners {

    private lateinit var homeViewModel: HomeViewModel
    private var searchQuery: String? = null
    private lateinit var viewManagerProduct: GridLayoutManager

    //    private lateinit var searchAdapter:SearchProductAdapter
    private lateinit var searchAdapter: ProductHorizontalAdapter
    private lateinit var productArrayList: ArrayList<Product>

    var status_product_added_to_fav_from = 0
    var added_product_id_to_fav = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchQuery = intent.getStringExtra(ConstantObjects.searchQueryKey)
        setupSaerchListAdapter()
        setupSearchViewModel()
        if (searchQuery != null) {
            homeViewModel.doSearch(mapOf("productName" to searchQuery!!))
        }
        setClickListeners()

    }

    private fun setClickListeners() {
        fbButtonBack.setOnClickListener {
            onBackPressed()
        }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                homeViewModel.doSearch(mapOf("productName" to p0.toString()!!))
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        ivSearch.setOnClickListener {
            if (etSearch.text.toString().trim() != "") {
                homeViewModel.doSearch(mapOf("productName" to etSearch.text.toString().trim()))
            } else {
                etSearch.error = getString(R.string.want_to_search_for_a_commodity_or_an_auction)
            }
        }
    }

    private fun setupSaerchListAdapter() {
        productArrayList = ArrayList()
        viewManagerProduct = GridLayoutManager(this, 2)
        searchAdapter = ProductHorizontalAdapter(productArrayList, this, 0, false)
        recyclerProduct.apply {
            layoutManager = viewManagerProduct
            adapter = searchAdapter
        }
    }

    private fun setupSearchViewModel() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.isLoading.observe(this, Observer {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        })
        homeViewModel.isNetworkFail.observe(this, Observer {
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

        })
        homeViewModel.errorResponseObserver.observe(this, Observer {
            if (it.message != null) {
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

        })
        homeViewModel.searchObserver.observe(this) { searchResp ->
            if (searchResp != null) {
                if (searchResp.status_code == 200) {
                    val list: ArrayList<Product> = Gson().fromJson(
                        Gson().toJson(searchResp.data),
                        object : TypeToken<ArrayList<Product>>() {}.type
                    )
                    productArrayList.clear()
                    productArrayList.addAll(list)
                    searchAdapter.notifyDataSetChanged()
                }
            }

        }
        homeViewModel.addProductToFavObserver.observe(this) {
            if (it.status_code == 200) {
                lifecycleScope.launch(Dispatchers.IO) {
                    var selectedSimilerProduct: Product? = null
                    for (product in productArrayList) {
                        if (product.id == added_product_id_to_fav) {
                            product.isFavourite = !product.isFavourite
                            selectedSimilerProduct = product
                            break
                        }
                    }
                    withContext(Dispatchers.Main) {
                        /**update similer product*/
                        selectedSimilerProduct?.let { product ->
                            searchAdapter.notifyItemChanged(
                                productArrayList.indexOf(
                                    product
                                )
                            )
                        }
                    }
                }
            }
        }
    }


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
            putExtra(ConstantObjects.productIdKey, productArrayList[position].id)
            putExtra("Template", "")
        })
    }

    override fun onAddProductToFav(position: Int, productID: Int, categoryID: Int) {
        if (HelpFunctions.isUserLoggedIn()) {

            added_product_id_to_fav = productArrayList[position].id
            homeViewModel.addProductToFav(productArrayList[position].id)

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

    private fun refreshFavProductStatus(productId: Int, productFavStatusKey: Boolean) {
        /***for similer product*/
        lifecycleScope.launch(Dispatchers.IO) {
            var selectedSimilerProduct: Product? = null
            for (product in productArrayList) {
                if (product.id == productId) {
                    product.isFavourite = productFavStatusKey
                    selectedSimilerProduct = product
                    break
                }
            }
            withContext(Dispatchers.Main) {
                /**update similer product*/
                selectedSimilerProduct?.let { product ->
                    searchAdapter.notifyItemChanged(
                        productArrayList.indexOf(
                            product
                        )
                    )
                }
            }
        }

    }

}