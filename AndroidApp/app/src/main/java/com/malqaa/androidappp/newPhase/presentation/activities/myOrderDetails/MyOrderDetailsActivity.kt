package com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityOrderDetailsBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderDetailsByMasterIDData
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderDetailsByMasterIDResp
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderFullInfoDto
import com.malqaa.androidappp.newPhase.domain.models.orderListResp.OrderItem
import com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails.adapter.CurrentOrderAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.shipmentRateActivity.ShipmentRateActivity
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myOrderFragment.MyOrdersViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.downloadFile
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show

class MyOrderDetailsActivity : BaseActivity<ActivityOrderDetailsBinding>(),
    SwipeRefreshLayout.OnRefreshListener,
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

        // Initialize view binding
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = getString(R.string.order_details)
        orderId = intent.getIntExtra(ConstantObjects.orderNumberKey, 0)
        tapId = intent.getIntExtra(ConstantObjects.orderTypeKey, 1)
        orderItem = intent.getParcelableExtra(ConstantObjects.orderItemKey)
        setOrderDetails(orderItem)
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)
        setOrderDetailsAdapter()
        setupViewModel()
        onRefresh()
        binding.toolbarMain.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setOrderDetails(orderItem: OrderItem?) {
        orderItem?.let {
            binding.orderNumberTv.text = "#${orderItem.orderMasterId}"

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
        orderFullInfoDtoList = ArrayList<OrderFullInfoDto>()
        currentOrderAdapter = CurrentOrderAdapter(orderFullInfoDtoList, this)
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
                binding.mainContainer.show()
                orderDetailsByMasterIDResp = resp
                binding.orderStatusTv.text = resp.orderDetailsByMasterIDData?.status.toString()

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
            binding.orderNumberTv.text = it.orderMasterId.toString()
            if (it.totalOrderMasterAmountAfterDiscount != null) {
                binding.totalTv.text =
                    orderDetailsByMasterIDData.totalOrderMasterAmountAfterDiscount!!.toString()
            }

            if (it.totalOrderMasterAmountBeforDiscount != null) {
                binding.subtotalTv.text =
                    orderDetailsByMasterIDData.totalOrderMasterAmountBeforDiscount.toString()
                binding.totalTv.text =
                    orderDetailsByMasterIDData.totalOrderMasterAmountBeforDiscount.toString()
            }
            if (it.totalOrderMasterAmountAfterDiscount != null && it.totalOrderMasterAmountBeforDiscount != null) {
                binding.discountTv.text =
                    (it.totalOrderMasterAmountBeforDiscount - orderDetailsByMasterIDData.totalOrderMasterAmountAfterDiscount!!).toString()
            }

        }

        orderDetailsByMasterIDData?.orderFullInfoDtoList?.let {
            orderFullInfoDtoList.clear()
            orderFullInfoDtoList.addAll(it)
            currentOrderAdapter.notifyDataSetChanged()
        }
        if (orderFullInfoDtoList.isNotEmpty()) {
            binding.tvClientAddress.text = orderFullInfoDtoList[0].shippingAddress ?: ""
            binding.tvClientPhone.text = orderFullInfoDtoList[0].phoneNumber ?: ""
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
        myOrdersViewModel.getCurrentOrderDetailsByMasterID(orderId)
    }

    override fun onDownloadInvoiceSelected(position: Int) {
        val orderInvoice =
            orderDetailsByMasterIDResp.orderDetailsByMasterIDData?.orderFullInfoDtoList?.get(
                position
            )?.orderInvoice
        if (orderInvoice != null) {
            downloadFile(context = this, orderInvoice)
        }
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


