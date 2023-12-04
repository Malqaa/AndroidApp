package com.malka.androidappp.newPhase.presentation.accountFragment.productsLostFragment

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
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.presentation.accountFragment.AccountViewModel
import com.malka.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malka.androidappp.newPhase.presentation.adapterShared.SetOnProductItemListeners
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.ProductDetailsActivity
import kotlinx.android.synthetic.main.fragment_lost.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProductsLostFragment : Fragment(R.layout.fragment_lost), SetOnProductItemListeners,
    SwipeRefreshLayout.OnRefreshListener {
    private lateinit var accountViewModel: AccountViewModel
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
        accountViewModel.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
        }
        accountViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), requireActivity())
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
            }

        }
        accountViewModel.addProductToFavObserver.observe(this) {
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
        accountViewModel.errorResponseObserver.observe(this) {
            if (it.message != null) {
                HelpFunctions.ShowLongToast(it.message!!, requireActivity())
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
            }
        }
        accountViewModel.productListObserver.observe(this) { it ->
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
            accountViewModel.addProductToFav(productList[position].id)

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
        accountViewModel.grtLostProducts()
    }

    override fun onDestroy() {
        super.onDestroy()
        accountViewModel.closeAllCall()
    }

}