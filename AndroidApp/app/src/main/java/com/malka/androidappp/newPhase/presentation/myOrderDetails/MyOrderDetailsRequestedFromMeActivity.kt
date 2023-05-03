package com.malka.androidappp.newPhase.presentation.myOrderDetails

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.orderDetails.OrderDetailsData
import com.malka.androidappp.newPhase.domain.models.orderDetails.OrderDetailsResp
import com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderProductFullInfoDto
import com.malka.androidappp.newPhase.domain.models.orderListResp.OrderItem
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.presentation.myOrderDetails.adapter.OrderProductAdapter
import com.malka.androidappp.newPhase.presentation.myOrderFragment.MyOrdersViewModel
import kotlinx.android.synthetic.main.activity_my_order_details_requested_from_me.*
import kotlinx.android.synthetic.main.toolbar_main.*

class MyOrderDetailsRequestedFromMeActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {
    lateinit var myOrdersViewModel: MyOrdersViewModel
    lateinit var currentOrderAdapter: OrderProductAdapter
    var orderDetailsResp: OrderDetailsResp? = null
    lateinit var productsList: ArrayList<OrderProductFullInfoDto>
    var orderId: Int = 0
    var orderItem: OrderItem? = null

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

        back_btn.setOnClickListener {
            onBackPressed()
        }

    }

    private fun setOrderDetails(orderItem: OrderItem?) {
        orderItem?.let {
//            if (tapId == 1) {
//                order_number_tv.text = "#${orderItem.orderMasterId}"
//            } else {
                order_number_tv.text = "#${orderItem.orderId}"
          //  }
            tv_request_type.text = orderItem.requestType
            order_time_tv.text = HelpFunctions.getViewFormatForDateTrack(orderItem.createdAt)
            shipments_tv.text = orderItem.providersCount.toString()
            total_order_tv.text =
                "${orderItem.totalOrderAmountAfterDiscount} ${getString(R.string.rial)}"
            order_status_tv.text = orderItem.status ?: ""
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
            if (it.message != null) {
                showApiError(it.message!!)
            } else {
                showApiError(getString(R.string.serverError))
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
    }

    private fun setOrderData(orderDetailsData: OrderDetailsData?) {
        orderDetailsData?.let {
            order_number_tv.text = it.orderId.toString()
            if (it.totalOrderPrice != null) {
                total_tv.text =
                    orderDetailsData.totalOrderPrice!!.toString()
            }
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

}