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
import com.malqaa.androidappp.databinding.FragmentSoldBusinessBinding
import com.malqaa.androidappp.newPhase.data.network.service.SetOnProductItemListeners
import com.malqaa.androidappp.newPhase.domain.models.orderListResp.OrderItem
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.confirmationAddProduct.ConfirmationAddProductActivity
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails.MyOrderDetailsRequestedFromMeActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.MyProductDetailsActivity
import com.malqaa.androidappp.newPhase.presentation.adapterShared.DidNotSaleAdapter
import com.malqaa.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myOrderFragment.adapter.MyOrdersAdapter
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myProducts.dialog.AddDiscountDialog
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myProducts.dialog.AddProductBidOffersDialog
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myProducts.dialog.BidPersonsDialog
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myProducts.dialog.MyProductSettingDialog
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myProducts.viewModel.MyProductViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.EndlessRecyclerViewScrollListener
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

class MyProductsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener,
    SetOnProductItemListeners {

    private lateinit var binding: FragmentSoldBusinessBinding

    private var lastPriceDiscount: Float = 0f
    private lateinit var myProductsViewModel: MyProductViewModel
    private lateinit var myProductForSaleListAdapter: ProductHorizontalAdapter
    private lateinit var myProductForNotSaleListAdapter: DidNotSaleAdapter
    private lateinit var archivedListAdapter: DidNotSaleAdapter
    private lateinit var productList: ArrayList<Product>
    private lateinit var notForSaleList: ArrayList<Product>
    private lateinit var archivedList: ArrayList<Product>

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
        binding = FragmentSoldBusinessBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.toolbarTitle.text = getString(R.string.MyProducts)
        setupViewModel()
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)
        setViewClickListeners()
        setAdapterForSaleAdapter()
        setUpSoldOutAdapter()
        setAdapterForNotSaleAdapter()
        setAdapterForArchivedAdapter()
        binding.soldOutRcv.hide()
        binding.didNotSaleRcv.hide()
        binding.archivedRcv.hide()
        binding.forSaleRecycler.show()
    }

    override fun onResume() {
        super.onResume()
        onRefresh()
        ConstantObjects.isModify = false
        ConstantObjects.isRepost = false
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
        binding.soldOutRcv.apply {
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
        binding.soldOutRcv.addOnScrollListener(endlessRecyclerViewScrollListener)
    }

    private fun setAdapterForSaleAdapter() {
        productList = ArrayList()
        myProductForSaleListAdapter = ProductHorizontalAdapter(productList, this, 0, false, true)
        binding.forSaleRecycler.apply {
            myProductForSaleListAdapter.setIsGridLayout(true)
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = myProductForSaleListAdapter
        }
    }

    private fun setAdapterForNotSaleAdapter() {
        notForSaleList = ArrayList()
        myProductForNotSaleListAdapter = DidNotSaleAdapter(notForSaleList, this, 0, false,
            isMyProduct = true
        )
        binding.didNotSaleRcv.apply {
            adapter = myProductForNotSaleListAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun setAdapterForArchivedAdapter() {
        archivedList = ArrayList()
        archivedListAdapter = DidNotSaleAdapter(archivedList, this, 0, false,
            isMyProduct = true
        )
        binding.archivedRcv.apply {
            adapter = archivedListAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }


    private fun setViewClickListeners() {
        binding.toolbar.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.forSale.setOnClickListener {
            tapId = 1
            binding.soldOutRcv.hide()
            binding.didNotSaleRcv.hide()
            binding.forSaleRecycler.show()
            binding.archivedRcv.hide()
            binding.forSale.setBackgroundResource(R.drawable.round_btn)
            binding.forSale.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            binding.soldOut.setBackgroundResource(R.drawable.edittext_bg)
            binding.soldOut.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            binding.didNotSell.setBackgroundResource(R.drawable.edittext_bg)
            binding.didNotSell.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            binding.archived.setBackgroundResource(R.drawable.edittext_bg)
            binding.archived.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            onRefresh()
        }
        binding.soldOut.setOnClickListener {
            tapId = 2
            binding.soldOutRcv.show()
            binding.forSaleRecycler.hide()
            binding.didNotSaleRcv.hide()
            binding.archivedRcv.hide()
            binding.forSale.setBackgroundResource(R.drawable.edittext_bg)
            binding.forSale.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            binding.soldOut.setBackgroundResource(R.drawable.round_btn)
            binding.soldOut.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            binding.didNotSell.setBackgroundResource(R.drawable.edittext_bg)
            binding.didNotSell.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            binding.archived.setBackgroundResource(R.drawable.edittext_bg)
            binding.archived.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            onRefresh()
        }
        binding.didNotSell.setOnClickListener {
            tapId = 3
            binding.soldOutRcv.hide()
            binding.didNotSaleRcv.show()
            binding.forSaleRecycler.hide()
            binding.archivedRcv.hide()
            binding.forSale.setBackgroundResource(R.drawable.edittext_bg)
            binding.forSale.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            binding.soldOut.setBackgroundResource(R.drawable.edittext_bg)
            binding.soldOut.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            binding.archived.setBackgroundResource(R.drawable.edittext_bg)
            binding.archived.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            binding.didNotSell.setBackgroundResource(R.drawable.round_btn)
            binding.didNotSell.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.white
                )
            )
            onRefresh()
        }
        binding.archived.setOnClickListener {
            tapId = 4
            binding.soldOutRcv.hide()
            binding.didNotSaleRcv.hide()
            binding.forSaleRecycler.hide()
            binding.archivedRcv.show()
            binding.forSale.setBackgroundResource(R.drawable.edittext_bg)
            binding.forSale.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            binding.soldOut.setBackgroundResource(R.drawable.edittext_bg)
            binding.soldOut.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            binding.didNotSell.setBackgroundResource(R.drawable.edittext_bg)
            binding.didNotSell.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            binding.archived.setBackgroundResource(R.drawable.round_btn)
            binding.archived.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.white
                )
            )
            onRefresh()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupViewModel() {
        myProductsViewModel = ViewModelProvider(this).get(MyProductViewModel::class.java)
        myProductsViewModel.isLoading.observe(this) {
            if (it)
                binding.progressBar.show()
            else
                binding.progressBar.hide()
        }
        myProductsViewModel.loadingAddDiscountDialog.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(requireActivity())
            else
                HelpFunctions.dismissProgressBar()
        }
        myProductsViewModel.isloadingMore.observe(this) {
            if (it)
                binding.progressBarMore.show()
            else
                binding.progressBarMore.hide()
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
                    notForSaleList.clear()
                    notForSaleList.addAll(productListResp.productList)
                    myProductForNotSaleListAdapter.notifyDataSetChanged()

                } else {
                    showProductApiError(getString(R.string.noProductsAdded))
                }
            }
        }
        myProductsViewModel.archivedProductRespObserver.observe(this) { productListResp ->
            if (productListResp.status_code == 200) {
                if (!productListResp.productList.isNullOrEmpty()) {
                    archivedList.clear()
                    archivedList.addAll(productListResp.productList)
                    archivedListAdapter.notifyDataSetChanged()

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
        binding.swipeToRefresh.isRefreshing = false
        binding.tvError.hide()
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
                notForSaleList.clear()
                myProductForNotSaleListAdapter.notifyDataSetChanged()
                myProductsViewModel.getForDidNotSaleProducts()
            }
            4 -> {
                archivedList.clear()
                archivedListAdapter.notifyDataSetChanged()
                myProductsViewModel.getForArchivedProducts()
            }
        }

    }

    private fun showProductApiError(message: String) {
        binding.tvError.show()
        binding.tvError.text = message
    }
    override fun onProductSelect(
        position: Int,productID: Int,categoryID: Int,userId: String,
        providerId: String,businessAccountId: String) {
        startActivity(
            Intent(requireActivity(), MyProductDetailsActivity::class.java).apply {
                putExtra(ConstantObjects.productIdKey, productID)
                putExtra(ConstantObjects.isMyProduct, true)
                putExtra(ConstantObjects.logged_userid, userId)
                putExtra("providerIdKey", providerId)
                putExtra("businessAccountIdKey", businessAccountId)

                if (tapId == 1)
                    putExtra("isMyProductForSale", true)
                else if (tapId == 3||tapId == 4)
                    putExtra("isMyProductForSale", false)
            })    }

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
                        ConstantObjects.isModify = true
                        ConstantObjects.isRepost = false
                        startActivity(
                            Intent(
                                context,
                                ConfirmationAddProductActivity::class.java
                            ).apply {
                                putExtra("productID", productID)
                                putExtra("whereCome", "repost")
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
                        ConstantObjects.isRepost = true
                        ConstantObjects.isModify = false
                        startActivity(
                            Intent(
                                context,
                                ConfirmationAddProductActivity::class.java
                            ).apply {
                                putExtra("productID", productID)
                                putExtra("whereCome", "repost")
                                putExtra(ConstantObjects.isEditKey, true)
                            })
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
                        if (newPrice < productList[position].price && (newPrice > 1)) {
                            myProductsViewModel.addDiscount(productID, newPrice, finaldate)
                        } else {
                            HelpFunctions.ShowLongToast(
                                getString(R.string.notAbleToApplyDiscount),
                                context
                            )
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