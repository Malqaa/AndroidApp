package com.malka.androidappp.newPhase.presentation.myProducts

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
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.EndlessRecyclerViewScrollListener
import com.malka.androidappp.newPhase.data.helper.HelpFunctions

import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.orderListResp.OrderItem
import com.malka.androidappp.newPhase.domain.models.productResp.Product

import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malka.androidappp.newPhase.presentation.adapterShared.SetOnProductItemListeners
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.presentation.myOrderFragment.adapter.MyOrdersAdapter
import com.malka.androidappp.newPhase.presentation.myProducts.dialog.AddDiscountDialog
import com.malka.androidappp.newPhase.presentation.myProducts.dialog.MyProductSettingDialog
import com.malka.androidappp.newPhase.presentation.myProducts.viewModel.MyProductViewModel
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.ProductDetailsActivity

import kotlinx.android.synthetic.main.fragment_sold_business.*
import kotlinx.android.synthetic.main.fragment_sold_business.progressBar
import kotlinx.android.synthetic.main.fragment_sold_business.swipe_to_refresh
import kotlinx.android.synthetic.main.fragment_sold_business.tvError

import kotlinx.android.synthetic.main.toolbar_main.*


class MyProductsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener,
    SetOnProductItemListeners {

    private var lastPriceDiscount: Float = 0f
    private lateinit var myProductsViewModel: MyProductViewModel
    private lateinit var myPorductForSaleListAdapter: ProductHorizontalAdapter
    private lateinit var productList: ArrayList<Product>
    private var lastUpdateIndex = -1

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
        onRefresh()
    }

    private fun setUpSoldOutAdapter() {
        soldOutOrdersList = ArrayList()
        soldoUtLayOutManager = GridLayoutManager(requireActivity(), 1)
        myOrdersAdapter = MyOrdersAdapter(soldOutOrdersList,object:MyOrdersAdapter.SetOnClickListeners{
            override fun onOrderSelected(position: Int) {

            }

        })
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
        myPorductForSaleListAdapter = ProductHorizontalAdapter(productList, this, 0, false, true)
        for_sale_recycler.apply {
            adapter = myPorductForSaleListAdapter
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
            did_not_sale_rcv.hide()
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
            did_not_sale_rcv.hide()
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
            did_not_sale_rcv.show()
            for_sale_recycler.hide()

            for_sale.setBackgroundResource(R.drawable.edittext_bg)
            for_sale.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            sold_out.setBackgroundResource(R.drawable.edittext_bg)
            sold_out.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            did_not_Sell.setBackgroundResource(R.drawable.round_btn)
            did_not_Sell.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            onRefresh()
        }
    }

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
            if (it.message != null) {
                showProductApiError(it.message!!)
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        myProductsViewModel.forSaleProductRespObserver.observe(this) { productListResp ->
            if (productListResp.status_code == 200) {
                if (productListResp.productList != null && productListResp.productList.isNotEmpty()) {
                    productList.clear()
                    productList.addAll(productListResp.productList)
                    myPorductForSaleListAdapter.notifyDataSetChanged()

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
        myProductsViewModel.addProductToFavObserver.observe(viewLifecycleOwner) {
            if (it.status_code == 200) {
                if (lastUpdateIndex < productList.size) {
                    productList[lastUpdateIndex].isFavourite =
                        !productList[lastUpdateIndex].isFavourite
                    myPorductForSaleListAdapter.notifyItemChanged(lastUpdateIndex)
                    myPorductForSaleListAdapter.notifyDataSetChanged()
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
                if(tapId==1) {
                    productList[lastUpdateIndex].priceDisc = lastPriceDiscount
                    myPorductForSaleListAdapter.notifyItemChanged(lastUpdateIndex)
                }
                HelpFunctions.ShowLongToast(getString(R.string.discountAddedSuccessfully), requireActivity())
            } else {
                if (addDiscountResp.message != null&&addDiscountResp.message != "") {
                    HelpFunctions.ShowLongToast(addDiscountResp.message, requireActivity())
                } else {
                    HelpFunctions.ShowLongToast(
                        requireActivity().getString(R.string.serverError),
                        requireActivity()
                    )
                }

            }
        }
    }

    override fun onRefresh() {
        endlessRecyclerViewScrollListener.resetState()
        swipe_to_refresh.isRefreshing = false
        tvError.hide()
        when (tapId) {
            1 -> {
                productList.clear()
                myPorductForSaleListAdapter.notifyDataSetChanged()
                myProductsViewModel.getForSaleProduct()
            }
            2 -> {
                soldOutOrdersList.clear()
                myOrdersAdapter.notifyDataSetChanged()
                myProductsViewModel.getSoldOutOrders(1)
            }
        }

    }

    private fun showProductApiError(message: String) {
        tvError.show()
        tvError.text = message
    }

    override fun onProductSelect(position: Int, productID: Int, categoryID: Int) {
        startActivity(
            Intent(requireActivity(), ProductDetailsActivity::class.java).apply {
                putExtra(ConstantObjects.productIdKey, productID)
                putExtra(ConstantObjects.isMyProduct, true)
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
        var myProductSettingDialog = MyProductSettingDialog(requireActivity(), object :
            MyProductSettingDialog.SetOnSelectedListeners {
            override fun onAddDiscount() {
                openDiscountDialog(position, productID, categoryID)
            }

            override fun onModifyProduct() {
                HelpFunctions.ShowLongToast("not implemented yet", requireActivity())
            }

            override fun onDeleteProduct() {
                HelpFunctions.ShowLongToast("not implemented yet", requireActivity())
            }

        })
        myProductSettingDialog.show()
    }

    private fun openDiscountDialog(position: Int, productID: Int, categoryID: Int) {
        var addDiscountDialog =
            AddDiscountDialog(requireActivity(),
                productList[position].price,
                requireActivity().supportFragmentManager,
                object : AddDiscountDialog.SetonClickListeners {
                    override fun onAddDiscount(finaldate: String, newPrice: Float) {
                        lastUpdateIndex=position
                        lastPriceDiscount=newPrice
                        myProductsViewModel.addDiscount(productID, newPrice, finaldate)
                    }

                })
        addDiscountDialog.show()
    }


//    fun notUsed(){
//        CommonAPI().getSoldItemsApi(ConstantObjects.logged_userid, requireContext()) {
//            sold_business_recycler.adapter = GenericProductAdapter(it.sellingitems, requireContext())
//            did_not_sale_rcv.adapter = GenericProductAdapter(it.unsolditems, requireContext())
////            sold_out_rcv.adapter = GenericOrderAdapter(it.solditems, requireContext())
//
//            for_sale.setOnClickListener {
//
//
//                did_not_Sell.setBackground(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.edittext_bg
//                    )
//                )
//                sold_out.setBackground(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.edittext_bg
//                    )
//                )
//                for_sale.setBackground(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.round_btn
//                    )
//                )
//
//
//                for_sale.setTextColor(Color.parseColor("#FFFFFF"));
//                did_not_Sell.setTextColor(Color.parseColor("#45495E"));
//                sold_out.setTextColor(Color.parseColor("#45495E"));
//                did_not_sale_rcv.hide()
//                sold_out_rcv.hide()
//                sold_business_recycler.show()
//
//            }
//            for_sale.performClick()
//            did_not_Sell.setOnClickListener {
//
//
//                did_not_Sell.setBackground(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.round_btn
//                    )
//                )
//                sold_out.setBackground(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.edittext_bg
//                    )
//                )
//                for_sale.setBackground(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.edittext_bg
//                    )
//                )
//
//                did_not_Sell.setTextColor(Color.parseColor("#FFFFFF"));
//                sold_out.setTextColor(Color.parseColor("#45495E"));
//                for_sale.setTextColor(Color.parseColor("#45495E"));
//
//                sold_business_recycler.hide()
//                sold_out_rcv.hide()
//                did_not_sale_rcv.show()
//
//            }
//            sold_out.setOnClickListener {
//
//
//                did_not_Sell.setBackground(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.edittext_bg
//                    )
//                )
//                sold_out.setBackground(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.round_btn
//                    )
//                )
//                for_sale.setBackground(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.edittext_bg
//                    )
//                )
//
//
//
//                sold_out.setTextColor(Color.parseColor("#FFFFFF"));
//                did_not_Sell.setTextColor(Color.parseColor("#45495E"));
//                for_sale.setTextColor(Color.parseColor("#45495E"));
//                did_not_sale_rcv.hide()
//                sold_business_recycler.hide()
//                sold_out_rcv.show()
//
//
//            }
//
//        }
//    }


}