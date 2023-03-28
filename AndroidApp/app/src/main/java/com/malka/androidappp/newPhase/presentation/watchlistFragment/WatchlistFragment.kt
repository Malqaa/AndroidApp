package com.malka.androidappp.newPhase.presentation.watchlistFragment

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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.productResp.Product

import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects

import com.malka.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malka.androidappp.newPhase.presentation.adapterShared.SetOnProductItemListeners

import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.ProductDetailsActivity

import kotlinx.android.synthetic.main.fragment_watchlist.*
import kotlinx.android.synthetic.main.fragment_watchlist.swipe_to_refresh
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class WatchlistFragment : Fragment(R.layout.fragment_watchlist),
    SwipeRefreshLayout.OnRefreshListener, SetOnProductItemListeners {


    private lateinit var wishListViewModel: WishListViewModel
    private lateinit var wishListAdapter:ProductHorizontalAdapter
    private lateinit var productList:ArrayList<Product>
    private var lastUpdateIndex=-1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_title.text = getString(R.string.favorite)

        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        setAdapterForWhisList()
        setClickListeners()
        setupViewModel()


//        if(!EventBus.getDefault().isRegistered(this)){
//            EventBus.getDefault().register(this)
//        }

//        fav_rcv!!.adapter = GenericProductAdapter(ConstantObjects.userwatchlist,requireContext())
//
//        if (!ConstantObjects.userwatchlist.isEmpty()) {
//            fav_rcv!!.adapter!!.notifyDataSetChanged()
//        } else {
//            HelpFunctions.ShowLongToast(
//                getString(R.string.NoRecordFound),
//                this@WatchlistFragment.context
//            )
//        }
    }

    private fun setAdapterForWhisList() {
        productList= ArrayList()
        wishListAdapter=ProductHorizontalAdapter(productList,this,0,false)
        fav_rcv.apply {
            adapter=wishListAdapter
            layoutManager=GridLayoutManager(requireActivity(),2)
        }
    }

    private fun setupViewModel() {
        wishListViewModel = ViewModelProvider(this).get(WishListViewModel::class.java)
        wishListViewModel.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
        }
        wishListViewModel.isNetworkFail.observe(this) {
            if (it) {
                showProductApiError(getString(R.string.connectionError))
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        wishListViewModel.errorResponseObserver.observe(this) {
            if (it.message != null) {
                showProductApiError(it.message)
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        wishListViewModel.wishListRespObserver.observe(this){productListResp->
            if(productListResp.status_code==200){
                if(productListResp.productList!=null&&productListResp.productList.isNotEmpty()){
                    productList.clear()
                    productList.addAll(productListResp.productList)
                    lifecycleScope.launch (Dispatchers.IO){
                        for(product in productList){
                            product.isFavourite=true
                        }
                        withContext(Dispatchers.Main){
                            wishListAdapter.notifyDataSetChanged()
                        }
                    }

                }else{
                    showProductApiError(getString(R.string.noProductsAdded))
                }
            }
        }
        wishListViewModel.isNetworkFailProductToFav.observe(viewLifecycleOwner) {
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
        wishListViewModel.errorResponseObserverProductToFav.observe(viewLifecycleOwner) {
            if (it.message != null && it.message != "") {
                HelpFunctions.ShowLongToast(
                    it.message,
                    requireActivity()
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    requireActivity()
                )
            }

        }
        wishListViewModel.addProductToFavObserver.observe(viewLifecycleOwner) {
            if (it.status_code == 200) {
               if(lastUpdateIndex<productList.size){
                   productList.removeAt(lastUpdateIndex)
                   wishListAdapter.notifyItemRemoved(lastUpdateIndex)
                   wishListAdapter.notifyDataSetChanged();
                   if(productList.isEmpty()){
                       showProductApiError(getString(R.string.noProductsAdded))
                   }
               }
            }
        }
        onRefresh()
    }

    override fun onRefresh() {
        swipe_to_refresh.isRefreshing = false
        tvError.hide()
        wishListViewModel.getWishListProduct()
    }

    private fun showProductApiError(message: String) {
        tvError.show()
        tvError.text = message
    }

    private fun setClickListeners() {
        back_btn.setOnClickListener {
            findNavController().popBackStack()

        }
    }

    override fun onProductSelect(position: Int, productID: Int, categoryID: Int) {
        startActivity(
            Intent(requireActivity(), ProductDetailsActivity::class.java).apply {
                putExtra(ConstantObjects.productIdKey, productID)
                putExtra("Template", "")
            })
    }

    override fun onAddProductToFav(position: Int, productID: Int, categoryID: Int) {
        if (HelpFunctions.isUserLoggedIn()) {
            lastUpdateIndex = position
            wishListViewModel.addProductToFav(productID)

        } else {
            requireActivity().startActivity(
                Intent(
                    context,
                    SignInActivity::class.java
                ).apply {})
        }
    }
    /**open activity product detials functions**/
    val productDetailsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val productId:Int= result.data?.getIntExtra(ConstantObjects.productIdKey,0) ?: 0
                val productFavStatusKey:Boolean= result.data?.getBooleanExtra(ConstantObjects.productFavStatusKey,false) ?:false
                refreshFavProductStatus(productId,productFavStatusKey)
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
                    productList.removeAt(productList.indexOf(product))
                    wishListAdapter.notifyItemRemoved(productList.indexOf(product))
                    wishListAdapter.notifyDataSetChanged();
                    if(productList.isEmpty()){
                        showProductApiError(getString(R.string.noProductsAdded))
                    }
                }
            }
        }

    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onMessageEvent(event: WatchList?) {
//        fav_rcv!!.adapter!!.notifyDataSetChanged()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        EventBus.getDefault().unregister(this)
//
//    }
}
