package com.malka.androidappp.newPhase.presentation.searchActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
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

    private var strSearch = ""
    private var productName: String? = null
    private var added_product_id_to_fav = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchQuery = intent.getStringExtra(ConstantObjects.searchQueryKey)

        productName = intent.getStringExtra("productName")

        setupSearchListAdapter()
        setupSearchViewModel()
        if (searchQuery != null) {
            homeViewModel.doSearch(mapOf("productName" to searchQuery!!))
        }
        setClickListeners()

        etSearch.setText(productName)
        homeViewModel.doSearch(mapOf("productName" to productName.toString().trim()))


    }

    private fun setClickListeners() {
        fbButtonBack.setOnClickListener {
            onBackPressed()
        }

        saveSearch.setOnClickListener {
            if (!HelpFunctions.isUserLoggedIn()) {
                startActivity(Intent(this, SignInActivity::class.java))
            } else {

                if (strSearch != etSearch.text.toString()) {
                    homeViewModel.saveSearch(etSearch.text.toString())
                } else
                    HelpFunctions.ShowLongToast(getString(R.string.saveDone), this)


            }
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                homeViewModel.doSearch(mapOf("productName" to p0.toString()))
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

    private fun setupSearchListAdapter() {
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

        homeViewModel.saveSearchObserver.observe(this) {
            if (it != null) {
                if (it.status_code == 200) {
                    strSearch = etSearch.text.toString()
                    HelpFunctions.ShowLongToast(getString(R.string.saveDone), this)
                    imgFollow.setImageResource(R.drawable.notification)
                }
            }
        }
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
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
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
            }

        })
        homeViewModel.searchObserver.observe(this) { searchResp ->
            if (searchResp != null) {
                if (searchResp.status_code == 200) {
                    recyclerProduct.visibility = View.VISIBLE
                    tvNoResult.visibility = View.GONE
                    val list: ArrayList<Product> = Gson().fromJson(
                        Gson().toJson(searchResp.data),
                        object : TypeToken<ArrayList<Product>>() {}.type
                    )
                    productArrayList.clear()
                    productArrayList.addAll(list)
                    searchAdapter.notifyDataSetChanged()
                } else {
                    productArrayList.clear()
                    recyclerProduct.visibility = View.GONE
                    tvNoResult.visibility = View.VISIBLE
                }
            } else {
                productArrayList.clear()
                recyclerProduct.visibility = View.GONE
                tvNoResult.visibility = View.VISIBLE
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

    override fun onDestroy() {
        super.onDestroy()
        homeViewModel.closeAllCall()
    }

}