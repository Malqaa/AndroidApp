package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myProducts

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.utils.helper.*
import com.malqaa.androidappp.newPhase.domain.models.orderListResp.OrderItem
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myOrderFragment.adapter.MyOrdersAdapter
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myProducts.dialog.AddDiscountDialog
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myProducts.dialog.AddProductBidOffersDialog
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myProducts.dialog.BidPersonsDialog
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myProducts.dialog.MyProductSettingDialog
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myProducts.viewModel.MyProductViewModel
import com.malqaa.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malqaa.androidappp.newPhase.data.network.service.SetOnProductItemListeners
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.ConfirmationAddProductActivity
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails.MyOrderDetailsRequestedFromMeActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.MyProductDetailsActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.EndlessRecyclerViewScrollListener
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import kotlinx.android.synthetic.main.fragment_sold_business.*
import kotlinx.android.synthetic.main.toolbar_main.*


class MyProductsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener,
    SetOnProductItemListeners {

    private var lastPriceDiscount: Float = 0f
    private lateinit var myProductsViewModel: MyProductViewModel
    private lateinit var myProductForSaleListAdapter: ProductHorizontalAdapter
    private lateinit var productList: ArrayList<Product>
    private var lastUpdateIndex = -1
    private lateinit var expireHoursList: ArrayList<Float>

    //====
    lateinit var soldOutOrdersList: ArrayList<OrderItem>
    lateinit var myOrdersAdapter: MyOrdersAdapter
    lateinit var soldoUtLayOutManager: GridLayoutManager

    //  tap id 1 ,2,3
    private var tapId: Int = 1
    lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sold_business, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_title.text = getString(R.string.MyProducts)
        setupViewModel()
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        setViewClickListeners()
        setAdapterForSaleAdapter()
        setUpSoldOutAdapter()

        sold_out_rcv.hide()
        did_not_sale_rcv.hide()
        for_sale_recycler.show()
    }

    override fun onResume() {
        super.onResume()
        onRefresh()
        ConstantObjects.isModify=false
        ConstantObjects.isRepost=false
    }

    private fun setUpSoldOutAdapter() {
        soldOutOrdersList = ArrayList()
        soldoUtLayOutManager = GridLayoutManager(requireActivity(), 1)
        myOrdersAdapter =
            MyOrdersAdapter(soldOutOrdersList, object : MyOrdersAdapter.SetOnClickListeners {
                override fun onOrderSelected(position: Int) {
                    startActivity(
                        Intent(
                            requireActivity(),
                            MyOrderDetailsRequestedFromMeActivity::class.java
                        ).apply {
                            putExtra(ConstantObjects.orderItemKey, soldOutOrdersList[position])
                            putExtra(
                                ConstantObjects.orderNumberKey,
                                soldOutOrdersList[position].orderId
                            )
                        })
                }

            })
        myOrdersAdapter.currentOrder = false
        sold_out_rcv.apply {
            adapter = myOrdersAdapter
            layoutManager = soldoUtLayOutManager
        }
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(soldoUtLayOutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    if (tapId == 2) {
                        myProductsViewModel.getSoldOutOrders(page)
                    }
                }
            }
        sold_out_rcv.addOnScrollListener(endlessRecyclerViewScrollListener)
    }

    private fun setAdapterForSaleAdapter() {
        productList = ArrayList()
        myProductForSaleListAdapter = ProductHorizontalAdapter(productList, this, 0, false, true)
        for_sale_recycler.apply {
            adapter = myProductForSaleListAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }


    private fun setViewClickListeners() {
        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        for_sale.setOnClickListener {
            tapId = 1
            sold_out_rcv.hide()
            //   did_not_sale_rcv.hide()
            for_sale_recycler.show()
            for_sale.setBackgroundResource(R.drawable.round_btn)
            for_sale.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            sold_out.setBackgroundResource(R.drawable.edittext_bg)
            sold_out.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            did_not_Sell.setBackgroundResource(R.drawable.edittext_bg)
            did_not_Sell.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            onRefresh()
        }
        sold_out.setOnClickListener {
            tapId = 2
            sold_out_rcv.show()
            //  did_not_sale_rcv.hide()
            for_sale_recycler.hide()

            for_sale.setBackgroundResource(R.drawable.edittext_bg)
            for_sale.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            sold_out.setBackgroundResource(R.drawable.round_btn)
            sold_out.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            did_not_Sell.setBackgroundResource(R.drawable.edittext_bg)
            did_not_Sell.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            onRefresh()
        }
        did_not_Sell.setOnClickListener {
            tapId = 3
            sold_out_rcv.hide()
            //   did_not_sale_rcv.show()
            for_sale_recycler.show()

            for_sale.setBackgroundResource(R.drawable.edittext_bg)
            for_sale.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            sold_out.setBackgroundResource(R.drawable.edittext_bg)
            sold_out.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            did_not_Sell.setBackgroundResource(R.drawable.round_btn)
            did_not_Sell.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            onRefresh()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupViewModel() {
        myProductsViewModel = ViewModelProvider(this).get(MyProductViewModel::class.java)
        myProductsViewModel.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
        }
        myProductsViewModel.loadingAddDiscountDialog.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(requireActivity())
            else
                HelpFunctions.dismissProgressBar()
        }
        myProductsViewModel.isloadingMore.observe(this) {
            if (it)
                progressBarMore.show()
            else
                progressBarMore.hide()
        }
        myProductsViewModel.isNetworkFail.observe(this) {
            if (it) {
                showProductApiError(getString(R.string.connectionError))
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        myProductsViewModel.errorResponseObserver.observe(this) {
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
        myProductsViewModel.forSaleProductRespObserver.observe(this) { productListResp ->
            if (productListResp.status_code == 200) {
                if (!productListResp.productList.isNullOrEmpty()) {
                    productList.clear()
                    productList.addAll(productListResp.productList)
                    myProductForSaleListAdapter.notifyDataSetChanged()

                } else {
                    showProductApiError(getString(R.string.noProductsAdded))
                }
            }
        }
        myProductsViewModel.notForSaleProductRespObserver.observe(this) { productListResp ->
            if (productListResp.status_code == 200) {
                if (!productListResp.productList.isNullOrEmpty()) {
                    productList.clear()
                    productList.addAll(productListResp.productList)
                    myProductForSaleListAdapter.notifyDataSetChanged()

                } else {
                    showProductApiError(getString(R.string.noProductsAdded))
                }
            }
        }
        myProductsViewModel.isNetworkFailProductToFav.observe(viewLifecycleOwner) {
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
        myProductsViewModel.errorResponseObserverProductToFav.observe(viewLifecycleOwner) {
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
        myProductsViewModel.addProductToFavObserver.observe(viewLifecycleOwner) {
            if (it.status_code == 200) {
                if (lastUpdateIndex < productList.size) {
                    productList[lastUpdateIndex].isFavourite =
                        !productList[lastUpdateIndex].isFavourite
                    myProductForSaleListAdapter.notifyItemChanged(lastUpdateIndex)
                    myProductForSaleListAdapter.notifyDataSetChanged()
                }
            }
        }
        myProductsViewModel.soldOutOrdersRespObserver.observe(viewLifecycleOwner) { orderListResp ->
            if (orderListResp.status_code == 200) {
                orderListResp.orderList?.let {
                    soldOutOrdersList.addAll(it)
                    myOrdersAdapter.notifyDataSetChanged()
                }
            }
        }
        myProductsViewModel.addDiscountObserver.observe(viewLifecycleOwner) { addDiscountResp ->
            if (addDiscountResp.status_code == 200) {
                if (tapId == 1) {
                    productList[lastUpdateIndex].priceDisc = lastPriceDiscount
                    myProductForSaleListAdapter.notifyItemChanged(lastUpdateIndex)
                }
                HelpFunctions.ShowLongToast(
                    getString(R.string.discountAddedSuccessfully),
                    requireActivity()
                )
            } else {
                if (addDiscountResp.message != null && addDiscountResp.message != "") {
                    HelpFunctions.ShowLongToast(addDiscountResp.message, requireActivity())
                } else {
                    HelpFunctions.ShowLongToast(
                        requireActivity().getString(R.string.serverError),
                        requireActivity()
                    )
                }

            }
        }


        myProductsViewModel.repostProductObserver.observe(viewLifecycleOwner) {
            HelpFunctions.ShowLongToast(getString(R.string.repostTheProduct), requireActivity())
            if (tapId == 3)
                myProductsViewModel.getForDidNotSaleProducts()

        }

        myProductsViewModel.removeProductObserver.observe(viewLifecycleOwner) {
            HelpFunctions.ShowLongToast(
                getString(R.string.removeProductSuccessfully),
                requireActivity()
            )
            if (tapId == 1)
                myProductsViewModel.getForSaleProduct()
            else {
                myProductsViewModel.getForDidNotSaleProducts()
            }
        }

        myProductsViewModel.configurationDataObserver.observe(viewLifecycleOwner) {
            expireHoursList = arrayListOf()
            val result: List<Float>? = it?.configValue?.split(",")?.map { it.trim().toFloat() }
            result?.let { it1 ->
                expireHoursList.addAll(it1)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onRefresh() {
        endlessRecyclerViewScrollListener.resetState()
        swipe_to_refresh.isRefreshing = false
        tvError.hide()
        when (tapId) {
            1 -> {
                productList.clear()
                myProductForSaleListAdapter.notifyDataSetChanged()
                myProductsViewModel.getForSaleProduct()
            }

            2 -> {
                soldOutOrdersList.clear()
                myOrdersAdapter.notifyDataSetChanged()
                myProductsViewModel.getSoldOutOrders(1)
            }

            3 -> {
                productList.clear()
                myProductForSaleListAdapter.notifyDataSetChanged()
                myProductsViewModel.getForDidNotSaleProducts()
            }
        }

    }

    private fun showProductApiError(message: String) {
        tvError.show()
        tvError.text = message
    }

    override fun onProductSelect(position: Int, productID: Int, categoryID: Int) {
        startActivity(
            Intent(requireActivity(), MyProductDetailsActivity::class.java).apply {
                putExtra(ConstantObjects.productIdKey, productID)
                putExtra(ConstantObjects.isMyProduct, true)
                if (tapId == 1)
                    putExtra("isMyProductForSale", true)
                else if (tapId == 3)
                    putExtra("isMyProductForSale", false)
            })
    }

    override fun onAddProductToFav(position: Int, productID: Int, categoryID: Int) {
        if (HelpFunctions.isUserLoggedIn()) {
            lastUpdateIndex = position
            myProductsViewModel.addProductToFav(productID)

        } else {
            requireActivity().startActivity(
                Intent(
                    context,
                    SignInActivity::class.java
                ).apply {})
        }
    }

    override fun onShowMoreSetting(position: Int, productID: Int, categoryID: Int) {
        val productForeSale = tapId == 1

        val fixedPriceEnabled = productList[0].isFixedPriceEnabled
        val myProductSettingDialog =
            MyProductSettingDialog(
                fixedPriceEnabled = fixedPriceEnabled,
                productList[position].isAuctionEnabled,
                productList[position].auctionClosingTime ?: "",
                requireActivity(),
                productForeSale,
                object :
                    MyProductSettingDialog.SetOnSelectedListeners {
                    override fun onAddDiscount() {
                        openDiscountDialog(position, productID, categoryID)
                    }

                    override fun onModifyProduct() {
                        ConstantObjects.isModify=true
                        ConstantObjects.isRepost=false
                        startActivity(Intent(context, ConfirmationAddProductActivity::class.java).apply {
                            putExtra("productID",productID)
                            putExtra("whereCome","repost")
                            putExtra(ConstantObjects.isEditKey, true)

                        })
                    }

                    override fun onDeleteProduct() {
                        myProductsViewModel.removeProduct(productID)
                    }

                    override fun onSendOfferProductToBidPersons() {
                        myProductsViewModel.getExpireHours()
                        openAddOfferDailog(productID)
                    }

                    override fun onRepostProduct() {
                        ConstantObjects.isRepost=true
                        ConstantObjects.isModify=false
                        startActivity(Intent(context, ConfirmationAddProductActivity::class.java).apply {
                            putExtra("productID",productID)
                            putExtra("whereCome","repost")
                            putExtra(ConstantObjects.isEditKey, true)
                        })
//                        myProductsViewModel.repostProduct(productID)
                    }

                })
        myProductSettingDialog.show()
    }


    private fun openAddOfferDailog(productID: Int) {
        val bidPersonsDialog = BidPersonsDialog("",
            requireActivity(),
            productID,
            object : BidPersonsDialog.SetOnAddBidOffersListeners {
                override fun onAddOpenBidOfferDailog(bidsList: List<String>) {
                    openAddProductOffers(bidsList, productID)
                }

                override fun onOpenAuctionDialog() {

                }

            })
        bidPersonsDialog.show()
    }

    private fun openAddProductOffers(bidsList: List<String>, productID: Int) {
        val addProductBidOffersDialog = AddProductBidOffersDialog(
            expireHoursList,
            requireActivity(),
            productID,
            bidsList,
            object : AddProductBidOffersDialog.SetClickListeners {
                override fun setOnSuccessListeners() {
                    HelpFunctions.ShowLongToast(getString(R.string.offerSent), context)
                }

            })
        addProductBidOffersDialog.show()
    }

    private fun openDiscountDialog(position: Int, productID: Int, categoryID: Int) {
        val addDiscountDialog =
            AddDiscountDialog(
                requireActivity(),
                productList[position].price,
                requireActivity().supportFragmentManager,
                object : AddDiscountDialog.SetonClickListeners {
                    override fun onAddDiscount(finaldate: String, newPrice: Float) {
                        lastUpdateIndex = position
                        lastPriceDiscount = newPrice
                        if(newPrice<productList[position].price && (newPrice>1)){
                            myProductsViewModel.addDiscount(productID, newPrice, finaldate)
                        }else{
                            HelpFunctions.ShowLongToast(getString(R.string.notAbleToApplyDiscount), context)
                        }


                    }

                })
        addDiscountDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        myProductsViewModel.closeAllCall()
        myProductsViewModel.baseCancel()
        myProductForSaleListAdapter.onDestroyHandler()
    }


}