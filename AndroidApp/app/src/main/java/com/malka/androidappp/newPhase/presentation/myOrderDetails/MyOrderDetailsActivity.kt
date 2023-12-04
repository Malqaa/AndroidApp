package com.malka.androidappp.newPhase.presentation.myOrderDetails

import android.content.Intent
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
import com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderDetailsByMasterIDData
import com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderDetailsByMasterIDResp
import com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderFullInfoDto
import com.malka.androidappp.newPhase.domain.models.orderListResp.OrderItem
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.presentation.myOrderDetails.adapter.CurrentOrderAdapter
import com.malka.androidappp.newPhase.presentation.accountFragment.myOrderFragment.MyOrdersViewModel
import com.malka.androidappp.newPhase.presentation.shipmentRateActivity.ShipmentRateActivity
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.toolbar_main.*

class MyOrderDetailsActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener,
    CurrentOrderAdapter.SetOnClickListeners {

    lateinit var myOrdersViewModel: MyOrdersViewModel
    lateinit var currentOrderAdapter: CurrentOrderAdapter
    lateinit var orderDetailsByMasterIDResp: OrderDetailsByMasterIDResp
    lateinit var orderFullInfoDtoList: ArrayList<OrderFullInfoDto>
    var orderId: Int = 0
    var orderItem: OrderItem? = null

    //  tap id 1 ,2,3
    private var tapId: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        toolbar_title.text = getString(R.string.order_details)
        orderId = intent.getIntExtra(ConstantObjects.orderNumberKey, 0)
        tapId = intent.getIntExtra(ConstantObjects.orderTypeKey, 1)
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
            //  if (tapId == 1) {
            order_number_tv.text = "#${orderItem.orderMasterId}"
//            } else {
//                order_number_tv.text = "#${orderItem.orderId}"
//            }
            tv_request_type.text = orderItem.requestType
            order_time_tv.text = HelpFunctions.getViewFormatForDateTrack(orderItem.createdAt)
            shipments_tv.text = orderItem.providersCount.toString()
            total_order_tv.text =
                "${orderItem.totalOrderAmountAfterDiscount} ${getString(R.string.rial)}"


        }

    }

    private fun setOrderDetailsAdapter() {
        orderFullInfoDtoList = ArrayList<OrderFullInfoDto>()
        currentOrderAdapter = CurrentOrderAdapter(orderFullInfoDtoList, this)
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
        myOrdersViewModel.isNetworkFailCancel.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }

        }
        myOrdersViewModel.errorResponseCancelObserver.observe(this) {
            if (it.message != null) {
                HelpFunctions.ShowLongToast(it.message!!, this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }

        }

        myOrdersViewModel.errorResponseCancelObserver.observe(this) {
            if (it.message != null) {
                HelpFunctions.ShowLongToast(it.message!!, this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }

        }
        myOrdersViewModel.currentOrderByMusterIdRespObserver.observe(this) { resp ->
            if (resp.status_code == 200) {
                mainContainer.show()
                orderDetailsByMasterIDResp = resp

                order_status_tv.text = resp.orderDetailsByMasterIDData?.status.toString()

                setCurrentOrderData(orderDetailsByMasterIDResp.orderDetailsByMasterIDData)
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

    private fun setCurrentOrderData(orderDetailsByMasterIDData: OrderDetailsByMasterIDData?) {
        orderDetailsByMasterIDData?.let {
            order_number_tv.text = it.orderMasterId.toString()
            if (it.totalOrderMasterAmountAfterDiscount != null) {
                total_tv.text =
                    orderDetailsByMasterIDData.totalOrderMasterAmountAfterDiscount!!.toString()
            }

            if (it.totalOrderMasterAmountBeforDiscount != null) {
                subtotal_tv.text =
                    orderDetailsByMasterIDData.totalOrderMasterAmountBeforDiscount.toString()
                total_tv.text =
                    orderDetailsByMasterIDData.totalOrderMasterAmountBeforDiscount.toString()
            }
            if (it.totalOrderMasterAmountAfterDiscount != null && it.totalOrderMasterAmountBeforDiscount != null) {
                discount_tv.text =
                    (it.totalOrderMasterAmountBeforDiscount - orderDetailsByMasterIDData.totalOrderMasterAmountAfterDiscount!!).toString()
            }

        }

        orderDetailsByMasterIDData?.orderFullInfoDtoList?.let {
            orderFullInfoDtoList.clear()
            orderFullInfoDtoList.addAll(it)
            currentOrderAdapter.notifyDataSetChanged()
        }
        if (orderFullInfoDtoList.isNotEmpty()) {
            tvClientAddress.text = orderFullInfoDtoList[0].shippingAddress ?: ""
            tvClientPhone.text = orderFullInfoDtoList[0].phoneNumber ?: ""
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
        //if (tapId == 1) {
        myOrdersViewModel.getCurrentOrderDetailsByMasterID(orderId)
//        } else {
//
//        }
    }

    override fun onAddRateToShipmentSelected(position: Int) {

        startActivity(Intent(this, ShipmentRateActivity::class.java).apply {
            putExtra(
                ConstantObjects.shipmentOrderDataKey,
                orderDetailsByMasterIDResp.orderDetailsByMasterIDData?.orderFullInfoDtoList?.get(
                    position
                )
            )
            putExtra(
                ConstantObjects.orderMasterIdKey,
                orderDetailsByMasterIDResp.orderDetailsByMasterIDData?.orderFullInfoDtoList?.get(
                    position
                )?.orderId
            )
        })
    }

    override fun onCancelOrder(position: Int) {
        myOrdersViewModel.cancelOrder(
            orderFullInfoDtoList[position].orderId,
            ConstantObjects.Canceled
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        myOrdersViewModel.closeAllCall()
    }

}


