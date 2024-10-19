package com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityMyOrderDetailsRequestedFromMeBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.orderDetails.OrderDetailsData
import com.malqaa.androidappp.newPhase.domain.models.orderDetails.OrderDetailsResp
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderProductFullInfoDto
import com.malqaa.androidappp.newPhase.domain.models.orderListResp.OrderItem
import com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails.adapter.OrderProductAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails.dialogs.OrderStatusDialog
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myOrderFragment.MyOrdersViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show

class MyOrderDetailsRequestedFromMeActivity :
    BaseActivity<ActivityMyOrderDetailsRequestedFromMeBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    lateinit var myOrdersViewModel: MyOrdersViewModel
    lateinit var currentOrderAdapter: OrderProductAdapter
    var orderDetailsResp: OrderDetailsResp? = null
    lateinit var productsList: ArrayList<OrderProductFullInfoDto>
    var orderId: Int = 0
    var orderItem: OrderItem? = null
    lateinit var orderStatusDialog: OrderStatusDialog

    //  tap id 1 ,2,3
    private var tapId: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityMyOrderDetailsRequestedFromMeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = getString(R.string.order_details)
        orderId = intent.getIntExtra(ConstantObjects.orderNumberKey, 0)

        orderItem = intent.getParcelableExtra(ConstantObjects.orderItemKey)
        setOrderDetails(orderItem)
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)
        setOrderDetailsAdapter()
        setupViewModel()
        onRefresh()
        setUpViewClickListeners()
    }

    private fun setUpViewClickListeners() {
        binding.toolbarMain.toolbarTitle.setOnClickListener {
            onBackPressed()
        }
        binding.btnChangeOrderStatus.setOnClickListener {
            var orderStatusDialog =
                OrderStatusDialog(
                    this,
                    orderDetailsResp?.orderDetails?.orderStatus ?: 0,
                    object : OrderStatusDialog.SetOnSelectOrderStatus {
                        override fun onSelectOrderStatus(orderStatus: Int) {
                            myOrdersViewModel.cancelOrder(orderId, orderStatus)

                        }

                    })
            orderStatusDialog.show()
        }

        binding.btnRateBuyer.setOnClickListener {

            startActivity(Intent(this, AddRateBuyerActivity::class.java).apply {
                putExtra("orderId", orderDetailsResp?.orderDetails?.orderId)
                putExtra("clientId", orderDetailsResp?.orderDetails?.clientId)
            })
        }
    }

    private fun setOrderDetails(orderItem: OrderItem?) {
        orderItem?.let {
            binding.orderNumberTv.text = "#${orderItem.orderId}"

            if (orderItem.requestType?.lowercase().equals("FixedPrice".lowercase())) {
                binding.tvRequestType.text = getString(R.string.fixed_price)
            } else if (orderItem.requestType?.lowercase().equals("Negotiation".lowercase())) {
                binding.tvRequestType.text = getString(R.string.Negotiation)
            } else if (orderItem.requestType?.lowercase().equals("Auction".lowercase())) {
                binding.tvRequestType.text = getString(R.string.auction)
            }
            binding.orderTimeTv.text =
                HelpFunctions.getViewFormatForDateTrack(orderItem.createdAt, "dd/MM/yyyy HH:mm:ss")
            binding.shipmentsTv.text = orderItem.providersCount.toString()
            binding.totalOrderTv.text =
                "${orderItem.totalOrderAmountAfterDiscount} ${getString(R.string.rial)}"
        }

    }

    private fun setOrderDetailsAdapter() {
        productsList = ArrayList<OrderProductFullInfoDto>()
        currentOrderAdapter = OrderProductAdapter(productsList)
        binding.rvCurentOrder.apply {
            adapter = currentOrderAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    private fun setupViewModel() {
        myOrdersViewModel = ViewModelProvider(this).get(MyOrdersViewModel::class.java)
        myOrdersViewModel.isLoading.observe(this) {
            if (it)
                binding.progressBar.show()
            else
                binding.progressBar.hide()
        }
        myOrdersViewModel.isNetworkFail.observe(this) {
            if (it) {
                showApiError(getString(R.string.connectionError))
            } else {
                showApiError(getString(R.string.serverError))
            }

        }
        myOrdersViewModel.errorResponseObserver.observe(this) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
                if (it.message != null) {
                    showApiError(it.message!!)
                } else {
                    showApiError(getString(R.string.serverError))
                }
            }
        }

        myOrdersViewModel.soldOutOrderDetailsByOrderIdRespObserver.observe(this) { resp ->
            if (resp.status_code == 200) {
                binding.mainContainer.show()
                orderDetailsResp = resp
                setOrderData(orderDetailsResp?.orderDetails)

            } else {
                if (resp.message != null) {
                    showApiError(resp.message)
                } else {
                    showApiError(getString(R.string.serverError))
                }
            }
        }

        myOrdersViewModel.changeOrderRespObserver.observe(this) { resp ->
            if (resp.status_code == 200) {
                onRefresh()
                if (resp.message != null) {
                    HelpFunctions.ShowLongToast(resp.message, this)
                }
            } else {
                if (resp.message != null) {
                    HelpFunctions.ShowLongToast(resp.message, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }
        }
    }

    private fun setOrderData(orderDetailsData: OrderDetailsData?) {
        orderDetailsData?.let {
            binding.orderNumberTv.text = it.orderId.toString()

            binding.totalTv.text =
                "${orderDetailsResp?.orderDetails?.totalOrderAmountAfterDiscount} ${getString(R.string.rial)}"
            binding.shipmentsTv.text = "${orderDetailsResp?.orderDetails?.shippingCount}"
            binding.subtotalTv.text =
                "${orderDetailsResp?.orderDetails?.totalOrderAmountBeforDiscount} ${getString(R.string.rial)}"
            if (orderDetailsResp?.orderDetails?.orderStatus == ConstantObjects.Delivered) {
                binding.btnChangeOrderStatus.hide()
                binding.btnRateBuyer.show()
            } else {
                binding.btnRateBuyer.hide()
            }

            binding.orderStatusTv.text = orderDetailsData.status
            binding.tvClientAddress.text = it.shippingAddress ?: ""
            binding.tvClientPhone.text = it.phoneNumber ?: ""
        }

        orderDetailsData?.orderProductFullInfoDto?.let {
            productsList.clear()
            productsList.addAll(it)
            currentOrderAdapter.notifyDataSetChanged()
        }

    }

    private fun showApiError(messaage: String) {
        binding.tvError.text = messaage
        binding.tvError.show()
        binding.mainContainer.hide()
    }

    override fun onRefresh() {
        binding.swipeToRefresh.isRefreshing = false
        binding.tvError.hide()
        binding.mainContainer.hide()
        myOrdersViewModel.getSoldOutOrderDetailsByOrderId(orderId)
    }

    override fun onDestroy() {
        super.onDestroy()
        myOrdersViewModel.closeAllCall()
    }
}