package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.productsLostFragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.AccountViewModel
import com.malqaa.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malqaa.androidappp.newPhase.data.network.service.SetOnProductItemListeners
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.ProductDetailsActivity
import kotlinx.android.synthetic.main.fragment_lost.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProductsLostFragment : Fragment(R.layout.fragment_lost), SetOnProductItemListeners,
    SwipeRefreshLayout.OnRefreshListener {
    private var accountViewModel: AccountViewModel?=null
    private lateinit var productAdapter: ProductHorizontalAdapter
    private lateinit var productList: ArrayList<Product>
    private var addProductIdToFav = -1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_title.text = getString(R.string.Loser)
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        setAdapterForSaleAdapter()
        setUpViewModel()
        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        onRefresh()
    }

    private fun setAdapterForSaleAdapter() {
        productList = ArrayList()
        productAdapter = ProductHorizontalAdapter(productList, this, 0, false, false)
        rvProduct.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun setUpViewModel() {
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        accountViewModel!!.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
        }
        accountViewModel!!.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), requireActivity())
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
            }

        }
        accountViewModel!!.addProductToFavObserver.observe(this) {
            if (it.status_code == 200) {
                lifecycleScope.launch(Dispatchers.IO) {
                    var selectedSimilerProduct: Product? = null
                    for (product in productList) {
                        if (product.id == addProductIdToFav) {
                            product.isFavourite = !product.isFavourite
                            selectedSimilerProduct = product
                            break
                        }
                    }
                    withContext(Dispatchers.Main) {
                        /**update similer product*/
                        /**update similer product*/
                        /**update similer product*/

                        /**update similer product*/
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
        accountViewModel!!.errorResponseObserver.observe(this) {
            if (it.message != null) {
                HelpFunctions.ShowLongToast(it.message!!, requireActivity())
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
            }
        }
        accountViewModel!!.productListObserver.observe(this) { it ->
            if (it.status_code == 200) {
                productList.clear()
                it.productList?.let { it ->
                    productList.addAll(it)
                    productAdapter.notifyDataSetChanged()
                    if (productList.isEmpty()) {
                        tvError.show()
                    } else {
                        tvError.hide()
                    }
                }
            } else {
                tvError.show()
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
        productDetailsLauncher.launch(
            Intent(
                requireActivity(),
                ProductDetailsActivity::class.java
            ).apply {
                putExtra(ConstantObjects.productIdKey, productList[position].id)
                putExtra("Template", "")
            })
    }

    override fun onAddProductToFav(position: Int, productID: Int, categoryID: Int) {
        if (HelpFunctions.isUserLoggedIn()) {

            addProductIdToFav = productList[position].id
            accountViewModel!!.addProductToFav(productList[position].id)

        } else {
            startActivity(
                Intent(
                    requireActivity(),
                    SignInActivity::class.java
                ).apply {})
        }
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
                    productAdapter.notifyItemChanged(
                        productList.indexOf(
                            product
                        )
                    )
                }
            }
        }

    }

    override fun onShowMoreSetting(position: Int, productID: Int, categoryID: Int) {

    }

    override fun onRefresh() {
        swipe_to_refresh.isRefreshing = false
        tvError.hide()
        productList.clear()
        productAdapter.notifyDataSetChanged()
        accountViewModel!!.grtLostProducts()
    }

    override fun onDestroy() {
        super.onDestroy()
        accountViewModel?.closeAllCall()
        accountViewModel?.baseCancel()
    }

}