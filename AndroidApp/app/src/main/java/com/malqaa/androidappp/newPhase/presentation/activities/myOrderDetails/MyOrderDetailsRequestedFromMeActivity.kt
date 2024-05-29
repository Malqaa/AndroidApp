package com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.domain.models.orderDetails.OrderDetailsData
import com.malqaa.androidappp.newPhase.domain.models.orderDetails.OrderDetailsResp
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderProductFullInfoDto
import com.malqaa.androidappp.newPhase.domain.models.orderListResp.OrderItem
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails.adapter.OrderProductAdapter
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myOrderFragment.MyOrdersViewModel
import com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails.dialogs.OrderStatusDialog
import kotlinx.android.synthetic.main.activity_my_order_details_requested_from_me.*
import kotlinx.android.synthetic.main.activity_order_details.tv_request_type
import kotlinx.android.synthetic.main.toolbar_main.*

class MyOrderDetailsRequestedFromMeActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {
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
        setContentView(R.layout.activity_my_order_details_requested_from_me)
        toolbar_title.text = getString(R.string.order_details)
        orderId = intent.getIntExtra(ConstantObjects.orderNumberKey, 0)
        //tapId = intent.getIntExtra(ConstantObjects.orderTypeKey, 1)
        orderItem = intent.getParcelableExtra(ConstantObjects.orderItemKey)
        setOrderDetails(orderItem)
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        setOrderDetailsAdapter()
        setupViewModel()
        onRefresh()
        setUpViewClickListeners()


    }

    private fun setUpViewClickListeners() {
        back_btn.setOnClickListener {
            onBackPressed()
        }
        btnChangeOrderStatus.setOnClickListener {
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

        btnRateBuyer.setOnClickListener {

            startActivity(Intent(this, AddRateBuyerActivity::class.java).apply {
                putExtra("orderId",orderDetailsResp?.orderDetails?.orderId)
                putExtra("clientId",orderDetailsResp?.orderDetails?.clientId)
            })
        }
    }

    private fun setOrderDetails(orderItem: OrderItem?) {
        orderItem?.let {
//            if (tapId == 1) {
//                order_number_tv.text = "#${orderItem.orderMasterId}"
//            } else {
            order_number_tv.text = "#${orderItem.orderId}"
            //  }

            if(orderItem.requestType?.lowercase().equals("FixedPrice".lowercase())){
                tv_request_type.text=getString(R.string.fixed_price)
            }else if(orderItem.requestType?.lowercase().equals("Negotiation".lowercase())){
                tv_request_type.text=getString(R.string.Negotiation)
            }else if(orderItem.requestType?.lowercase().equals("Auction".lowercase())){
                tv_request_type.text=getString(R.string.auction)
            }
            order_time_tv.text = HelpFunctions.getViewFormatForDateTrack(orderItem.createdAt,"dd/MM/yyyy HH:mm:ss")
            shipments_tv.text = orderItem.providersCount.toString()
            total_order_tv.text =
                "${orderItem.totalOrderAmountAfterDiscount} ${getString(R.string.rial)}"
//            when (orderItem.orderStatus) {
//                ConstantObjects.WaitingForPayment -> {
//                    order_status_tv.text = getString(R.string.WaitingForPayment)
//                }
//                ConstantObjects.Retrieved -> {
//                    order_status_tv.text = getString(R.string.Retrieved)
//                }
//                ConstantObjects.InProgress -> {
//                    order_status_tv.text = getString(R.string.InProgress)
//                }
//                ConstantObjects.DeliveryInProgress -> {
//                    order_status_tv.text = getString(R.string.DeliveryInProgress)
//                }
//            }


        }

    }

    private fun setOrderDetailsAdapter() {
        productsList = ArrayList<OrderProductFullInfoDto>()
        currentOrderAdapter = OrderProductAdapter(productsList)
        rvCurentOrder.apply {
            adapter = currentOrderAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    private fun setupViewModel() {
        myOrdersViewModel = ViewModelProvider(this).get(MyOrdersViewModel::class.java)
        myOrdersViewModel.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
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
                mainContainer.show()
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
            order_number_tv.text = it.orderId.toString()
//            if (it.totalOrderPrice != null) {
//                total_tv.text =
//                    orderDetailsData.totalOrderPrice!!.toString()
//            }

            total_tv.text =
                "${orderDetailsResp?.orderDetails?.totalOrderAmountAfterDiscount} ${getString(R.string.rial)}"
            shipments_tv.text = "${orderDetailsResp?.orderDetails?.shippingCount}"
            subtotal_tv.text =
                "${orderDetailsResp?.orderDetails?.totalOrderAmountBeforDiscount} ${getString(R.string.rial)}"
            if (orderDetailsResp?.orderDetails?.orderStatus == ConstantObjects.Delivered) {
                btnChangeOrderStatus.hide()
                btnRateBuyer.show()
            }else{
                btnRateBuyer.hide()
            }


            order_status_tv.text = orderDetailsData.status

//            when (orderDetailsData.orderStatus) {
//                ConstantObjects.WaitingForPayment -> {
//                    order_status_tv.text = getString(R.string.WaitingForPayment)
//                }
//                ConstantObjects.Retrieved -> {
//                    order_status_tv.text = getString(R.string.order_productsProcessing)
//                }
//                ConstantObjects.InProgress -> {
//                    order_status_tv.text = getString(R.string.order_deliveryPhase)
//                }
//                ConstantObjects.DeliveryInProgress -> {
//                    order_status_tv.text = getString(R.string.order_deliveryConfirmation)
//                }
//            }

//            if (it.totalOrderMasterAmountBeforDiscount != null) {
//                subtotal_tv.text =
//                    orderDetailsByMasterIDData.totalOrderMasterAmountBeforDiscount!!.toString()
//                total_tv.text =
//                    orderDetailsByMasterIDData.totalOrderMasterAmountBeforDiscount!!.toString()
//            }
//            if (it.totalOrderMasterAmountAfterDiscount != null && it.totalOrderMasterAmountBeforDiscount != null) {
//                discount_tv.text =
//                    (it.totalOrderMasterAmountBeforDiscount - orderDetailsByMasterIDData.totalOrderMasterAmountAfterDiscount!!).toString()
//            }
            tvClientAddress.text = it.shippingAddress ?: ""
            tvClientPhone.text = it.phoneNumber ?: ""
        }

        orderDetailsData?.orderProductFullInfoDto?.let {
            productsList.clear()
            productsList.addAll(it)
            currentOrderAdapter.notifyDataSetChanged()
        }

    }

    private fun showApiError(messaage: String) {
        tvError.text = messaage
        tvError.show()
        mainContainer.hide()
    }

    override fun onRefresh() {
        swipe_to_refresh.isRefreshing = false
        tvError.hide()
        mainContainer.hide()
        myOrdersViewModel.getSoldOutOrderDetailsByOrderId(orderId)
//        if (tapId == 1) {
//            myOrdersViewModel.getCurrentOrderDetailsByMasterID(orderId)
//        } else {
//            myOrdersViewModel.getSoldOutOrderDetailsByOrderId(orderId)
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myOrdersViewModel.closeAllCall()
    }
}