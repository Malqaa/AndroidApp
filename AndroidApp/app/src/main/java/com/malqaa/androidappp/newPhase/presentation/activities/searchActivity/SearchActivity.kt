package com.malqaa.androidappp.newPhase.presentation.activities.searchActivity

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
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivitySearchBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.data.network.service.SetOnProductItemListeners
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.domain.models.productResp.SearchProductList
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.ProductDetailsActivity
import com.malqaa.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.viewModel.HomeViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.EndlessRecyclerViewScrollListener
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : BaseActivity<ActivitySearchBinding>(),
    SetOnProductItemListeners {

    private lateinit var homeViewModel: HomeViewModel
    private var searchQuery: String? = null
    private lateinit var viewManagerProduct: GridLayoutManager
    private lateinit var searchAdapter: ProductHorizontalAdapter
    private lateinit var productArrayList: ArrayList<Product>

    private var strSearch = ""
    var page = 0
    private var productName: String? = null
    private var added_product_id_to_fav = 0
    lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchQuery = intent.getStringExtra(ConstantObjects.searchQueryKey)

        productName = intent.getStringExtra("productName")

        setupSearchListAdapter()
        setupSearchViewModel()

        setClickListeners()

        binding.etSearch.setText(productName)
    }


    fun searchAdvance(page: Int) {
//         mainCatId=0&Screen=3&productName=1272&SellerBusinessAccountId=&lang=en&pageIndex=0&PageRowsCount=12
        homeViewModel.searchForProduct(
            0,
            ConstantObjects.currentLanguage,
            page,
            null,
            null,
            null,
            null,
            null,
            0f,
            0f,
            productName,
            3
        )
    }

    private fun setClickListeners() {
        binding.fbButtonBack.setOnClickListener {
            onBackPressed()
        }

        binding.saveSearch.setOnClickListener {
            if (!HelpFunctions.isUserLoggedIn()) {
                startActivity(Intent(this, SignInActivity::class.java))
            } else {

                if (strSearch != binding.etSearch.text.toString()) {
                    homeViewModel.saveSearch(binding.etSearch.text.toString())
                } else
                    HelpFunctions.ShowLongToast(getString(R.string.saveDone), this)


            }
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                productName = p0.toString()
                searchAdvance(1)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        binding.ivSearch.setOnClickListener {
            if (binding.etSearch.text.toString().trim() != "") {
                productName = binding.etSearch.text.toString()
                searchAdvance(1)
            } else {
                binding.etSearch.error =
                    getString(R.string.want_to_search_for_a_commodity_or_an_auction)
            }
        }
    }

    private fun setupSearchListAdapter() {
        productArrayList = ArrayList()
        viewManagerProduct = GridLayoutManager(this, 2)
        searchAdapter = ProductHorizontalAdapter(productArrayList, this, 0, false)
        binding.recyclerProduct.apply {
            layoutManager = viewManagerProduct
            adapter = searchAdapter
        }

        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(viewManagerProduct) {
                override fun onLoadMore(_page: Int, totalItemsCount: Int, view: RecyclerView) {
                    page++
                    searchAdvance(page)
                }
            }
        binding.recyclerProduct.addOnScrollListener(endlessRecyclerViewScrollListener)
    }

    private fun setupSearchViewModel() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.saveSearchObserver.observe(this) {
            if (it != null) {
                if (it.status_code == 200) {
                    strSearch = binding.etSearch.text.toString()
                    HelpFunctions.ShowLongToast(getString(R.string.saveDone), this)
                    binding.imgFollow.setImageResource(R.drawable.notification)
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

        homeViewModel.searchProductListRespObserver.observe(this) { searchResp ->
            println("hhhh searchProdu" + Gson().toJson(searchResp))
            if (searchResp != null) {
                if (searchResp.status_code == 200) {
                    val list: SearchProductList = Gson().fromJson(
                        Gson().toJson(searchResp.data),
                        object : TypeToken<SearchProductList>() {}.type
                    )
                    binding.totalResultTxt.text =
                        "" + searchResp.totaRecords + " " + getString(R.string.results)
                    productArrayList.clear()
                    productArrayList.addAll(list.products!!)
                    searchAdapter.notifyDataSetChanged()
                } else {
                    productArrayList.clear()
                    binding.recyclerProduct.visibility = View.GONE
                    binding.tvNoResult.visibility = View.VISIBLE
                }
            }

        }
        homeViewModel.searchObserver.observe(this) { searchResp ->
            if (searchResp != null) {
                if (searchResp.status_code == 200) {
                    binding.recyclerProduct.visibility = View.VISIBLE
                    binding.tvNoResult.visibility = View.GONE
                    val list: ArrayList<Product> = Gson().fromJson(
                        Gson().toJson(searchResp.data),
                        object : TypeToken<ArrayList<Product>>() {}.type
                    )
                    productArrayList.clear()
                    productArrayList.addAll(list)
                    searchAdapter.notifyDataSetChanged()
                } else {
                    productArrayList.clear()
                    binding.recyclerProduct.visibility = View.GONE
                    binding.tvNoResult.visibility = View.VISIBLE
                }
            } else {
                productArrayList.clear()
                binding.recyclerProduct.visibility = View.GONE
                binding.tvNoResult.visibility = View.VISIBLE
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

    override fun onProductSelect(position: Int,productID:Int,categoryID:Int,userId:String,providerId:String,businessAccountId:String) {
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
        homeViewModel.baseCancel()
        searchAdapter.onDestroyHandler()
    }

}