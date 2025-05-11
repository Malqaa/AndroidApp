package com.malqaa.androidappp.newPhase.presentation.fragments.watchlistFragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.FragmentWatchlistBinding
import com.malqaa.androidappp.newPhase.data.network.service.SetOnProductItemListeners
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.ProductDetailsActivity
import com.malqaa.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WatchlistFragment : Fragment(R.layout.fragment_watchlist),
    SwipeRefreshLayout.OnRefreshListener, SetOnProductItemListeners {

    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = _binding!!

    private var wishListViewModel: WishListViewModel? = null
    private var wishListAdapter: ProductHorizontalAdapter? = null
    private var productList: ArrayList<Product>? = null
    private var lastUpdateIndex = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentWatchlistBinding.bind(view) // Initialize View Binding

        binding.toolbarMain.toolbarTitle.text = getString(R.string.favorite)
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)
        setAdapterForWhisList()
        setClickListeners()
        setupViewModel()
    }

    private fun setAdapterForWhisList() {
        productList = ArrayList()
        wishListAdapter = ProductHorizontalAdapter(productList ?: arrayListOf(), this, 0, false)
        binding.favRcv.apply {
            adapter = wishListAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun setupViewModel() {
        wishListViewModel = ViewModelProvider(this).get(WishListViewModel::class.java)
        wishListViewModel!!.isLoading.observe(this) {
            if (it)
                binding.progressBar.show()
            else {

                HelpFunctions.dismissProgressBar()
                binding.progressBar.hide()
            }
        }
        wishListViewModel!!.isNetworkFail.observe(this) {
            if (it) {
                showProductApiError(getString(R.string.connectionError))
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        wishListViewModel!!.errorResponseObserver.observe(this) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), requireActivity())
            } else {
                if (it.message != null) {
                    showProductApiError(it.message!!)
                } else {
                    showProductApiError(getString(R.string.serverError))
                }

            }
        }
        wishListViewModel!!.wishListRespObserver.observe(this) { productListResp ->
            if (productListResp.status_code == 200) {
                if (productListResp.productList != null && productListResp.productList.isNotEmpty()) {
                    productList?.clear()
                    productList?.addAll(productListResp.productList)
                    lifecycleScope.launch(Dispatchers.IO) {
//                        productList.filter { !it.isFavourite }.forEach { it.isFavourite = true}

                        for (product in productList ?: arrayListOf()) {
                            product.isFavourite = true
                        }
                        withContext(Dispatchers.Main) {
                            wishListAdapter?.notifyDataSetChanged()
                        }
                    }

                } else {
                    showProductApiError(getString(R.string.noProductsAdded))
                }
            }
        }
        wishListViewModel!!.isNetworkFailProductToFav.observe(viewLifecycleOwner) {
            if (it) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.connectionError),
                    requireActivity()
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    requireActivity()
                )
            }

        }
        wishListViewModel!!.errorResponseObserverProductToFav.observe(viewLifecycleOwner) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), requireActivity())
            } else {
                if (it.message != null && it.message != "") {
                    HelpFunctions.ShowLongToast(
                        it.message!!,
                        requireActivity()
                    )
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.serverError),
                        requireActivity()
                    )
                }
            }
        }
        wishListViewModel!!.addProductToFavObserver.observe(viewLifecycleOwner) {
            if (it.status_code == 200) {
                if (lastUpdateIndex < (productList ?: arrayListOf()).size) {
                    productList?.removeAt(lastUpdateIndex)
                    wishListAdapter?.notifyItemRemoved(lastUpdateIndex)
                    wishListAdapter?.notifyDataSetChanged();
                    if (productList?.isEmpty() == true) {
                        showProductApiError(getString(R.string.noProductsAdded))
                    }
                }
            }
        }
    }

    override fun onRefresh() {
        binding.swipeToRefresh.isRefreshing = false
        binding.tvError.hide()
        wishListViewModel?.getWishListProduct()
    }

    override fun onResume() {
        super.onResume()
        onRefresh()
    }


    private fun showProductApiError(message: String) {
        binding.tvError.show()
        binding.tvError.text = message
    }

    private fun setClickListeners() {
        binding.toolbarMain.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onProductSelect(position: Int,productID:Int,categoryID:Int,userId:String,providerId:String,businessAccountId:String) {
        startActivity(
            Intent(requireActivity(), ProductDetailsActivity::class.java).apply {
                putExtra(ConstantObjects.productIdKey, productID)
                putExtra("Template", "")
            })
    }

    override fun onAddProductToFav(position: Int, productID: Int, categoryID: Int) {
        if (HelpFunctions.isUserLoggedIn()) {
            lastUpdateIndex = position
            wishListViewModel?.addProductToFav(productID)

        } else {
            requireActivity().startActivity(
                Intent(
                    context,
                    SignInActivity::class.java
                ).apply {})
        }
    }

    override fun onShowMoreSetting(position: Int, productID: Int, categoryID: Int) {

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

    private fun goToProductDetails(productId: Int) {
        productDetailsLauncher.launch(Intent(context, ProductDetailsActivity::class.java).apply {
            putExtra(ConstantObjects.productIdKey, productId)
            putExtra("Template", "")
        })
    }

    private fun refreshFavProductStatus(productId: Int, productFavStatusKey: Boolean) {
        /***for similer product*/
        lifecycleScope.launch(Dispatchers.IO) {
            var selectedSimilerProduct: Product? = null
            for (product in productList ?: arrayListOf()) {
                if (product.id == productId) {
                    product.isFavourite = productFavStatusKey
                    selectedSimilerProduct = product
                    break
                }
            }
            withContext(Dispatchers.Main) {
                /**update similer product*/
                selectedSimilerProduct?.let { product ->
                    (productList ?: arrayListOf()).removeAt(productList?.indexOf(product)!!)
                    wishListAdapter?.notifyItemRemoved(productList?.indexOf(product)!!)
                    wishListAdapter?.notifyDataSetChanged();
                    if (productList?.isEmpty() == true) {
                        showProductApiError(getString(R.string.noProductsAdded))
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        wishListViewModel?.closeAllCall()
        wishListViewModel?.baseCancel()
        wishListAdapter?.onDestroyHandler()
        wishListViewModel = null
        wishListAdapter = null
        productList = null
    }

}
