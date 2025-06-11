package com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityOrderDetailsBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.data.network.model.BankTransferPaymentStatus
import com.malqaa.androidappp.newPhase.data.network.model.ExtendBankTransferPaymentPeriodRequest
import com.malqaa.androidappp.newPhase.data.network.model.OrderStatus
import com.malqaa.androidappp.newPhase.data.network.model.SellerConfirmOrRejectBankTransferPaymentRequest
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderDetailsByMasterIDData
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderDetailsByMasterIDResp
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderFullInfoDto
import com.malqaa.androidappp.newPhase.domain.models.orderListResp.OrderItem
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.AttachInvoice
import com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails.adapter.CurrentOrderAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.shipmentRateActivity.ShipmentRateActivity
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myOrderFragment.MyOrdersViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.HelpFunctions.Companion.ShowAlert
import com.malqaa.androidappp.newPhase.utils.HelpFunctions.Companion.ShowConfirmWithCommnetAlert
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

        Log.d("test #1", "onCreate: MyOrderDetailsActivity")

        // Initialize view binding
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar
        binding.toolbarMain.toolbarTitle.text = getString(R.string.order_details)
        binding.toolbarMain.backBtn.setOnClickListener { onBackPressed() }

        // Get order details from intent
        orderId = intent.getIntExtra(ConstantObjects.orderNumberKey, 0)
        tapId = intent.getIntExtra(ConstantObjects.orderTypeKey, 1)
        orderItem = intent.getParcelableExtra(ConstantObjects.orderItemKey)
        setOrderDetails(orderItem)

        // Set up swipe-to-refresh
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)

        setOrderDetailsAdapter()
        setupViewModel()
        onRefresh()

        initClickListener()
    }

    private fun initClickListener() {
        // confirm button click listener
        binding.buttonConfirm.setOnClickListener {
            myOrdersViewModel.sellerConfirmOrRejectBankTransferPayment(
                request = SellerConfirmOrRejectBankTransferPaymentRequest(
                    orderId = orderId,
                    confirmed = true
                )
            )
        }

        // reject button click listener
        binding.buttonReject.setOnClickListener {
            ShowConfirmWithCommnetAlert(
                context = this,
                subtitle = getString(R.string.please_enter_the_reason_of_rejection),
                onConfirm = { text ->
                    myOrdersViewModel.sellerConfirmOrRejectBankTransferPayment(
                        request = SellerConfirmOrRejectBankTransferPaymentRequest(
                            orderId = orderId,
                            confirmed = false,
                            comment = text
                        )
                    )
                }
            )
        }

        // extend button click listener
        binding.buttonExtend.setOnClickListener {
            myOrdersViewModel.extendBankTransferPaymentPeriod(
                request = ExtendBankTransferPaymentPeriodRequest(orderId = orderId)
            )
        }

        // upload button click listener
        binding.buttonUpload.setOnClickListener {
            val intent = Intent(this@MyOrderDetailsActivity, AttachInvoice::class.java)
            startActivity(intent)
        }

        // download button click listener
        binding.buttonDownload.setOnClickListener {
            val orderInvoice = orderDetailsByMasterIDResp
                .orderDetailsByMasterIDData
                ?.orderFullInfoDtoList
                ?.getOrNull(index = 0)
                ?.orderInvoice

            if (orderInvoice != null) {
                downloadFile(context = this, orderInvoice)
            }
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
                    if (it.message == "InvalidConfirmationCode") {
                        ShowAlert(
                            context = this,
                            alertTitle = "",
                            alertMessage = getString(R.string.the_entered_code_is_incorrect),
                            icon = R.drawable.warning
                        )
                    } else {
                        showApiError(it.message!!)
                    }
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
            if (resp.statusCode == 200) {
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

        // Reset all views first
        resetAllViewsFirst()

        val orderFullInfoDto = orderDetailsByMasterIDResp
            .orderDetailsByMasterIDData
            ?.orderFullInfoDtoList
            ?.getOrNull(0)

        val orderInvoice = orderFullInfoDto?.orderInvoice
        orderInvoice?.let { imageUrl ->
            Glide.with(this)
                .load(imageUrl)
                .error(R.drawable.error_image)
                .into(binding.imagePaymentInvoiceAttached)

            binding.imagePaymentInvoiceAttached.setOnClickListener {
                showImagePreviewDialog(imageUrl = imageUrl)
            }
        }

        val bankTransferPaymentStatus = orderFullInfoDto?.bankTransferPaymentStatus
        val orderStatus = orderFullInfoDto?.orderStatus
        val status = bankTransferPaymentStatus?.let { BankTransferPaymentStatus.valueOf(it) }

        if (status == BankTransferPaymentStatus.Pending || status == BankTransferPaymentStatus.Rejected) {
            binding.linearPaymentMethod.visibility = View.VISIBLE
            binding.cardUploadBankTransferDocument.visibility = View.VISIBLE
            binding.textUploadBankTransferDocument.text =
                getString(R.string.waiting_for_buyer_for_upload_the_bank_transfer_document)
            binding.textUploadBankTransferDocument.setTextColor(Color.GREEN)
        } else if (status == BankTransferPaymentStatus.UploadPeriodExpired) {
            binding.linearPaymentMethod.visibility = View.VISIBLE
            binding.cardUploadBankTransferDocument.visibility = View.VISIBLE
            binding.buttonExtend.visibility = View.VISIBLE
            binding.textUploadBankTransferDocument.text =
                getString(R.string.the_buyer_has_not_uploaded_the_bank_transfer_receipt_yet_do_you_want_to_extend_the_waiting_period_for_another_3_days)
            binding.textUploadBankTransferDocument.setTextColor(Color.RED)
            binding.buttonUpload.visibility = View.GONE
        } else if (status == BankTransferPaymentStatus.Uploaded) {
            binding.cardPaymentInvoiceAttached.visibility = View.VISIBLE
            binding.linearConfirmConfirm.visibility = View.VISIBLE
        } else if (status == BankTransferPaymentStatus.Confirmed) {
            binding.cardPaymentInvoiceAttached.visibility = View.VISIBLE
            binding.buttonDownload.visibility = View.VISIBLE
        }

        // Additional logic based on order status and bank account presence
        val orderProduct = orderFullInfoDto?.orderProductFullInfoDto?.getOrNull(index = 0)
        val hasBankAccounts = orderProduct?.productBankAccountsDto?.isNotEmpty() == true

        if (
            orderStatus == OrderStatus.WaitingForPayment.value &&
            (status == BankTransferPaymentStatus.Pending || status == BankTransferPaymentStatus.Rejected)
        ) {
            if (hasBankAccounts) {
                binding.cardUploadBankTransferDocument.visibility = View.VISIBLE
                binding.buttonUpload.visibility = View.VISIBLE
                binding.buttonExtend.visibility = View.GONE
            }
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
        myOrdersViewModel.changeOrderStatus(
            orderId = orderFullInfoDtoList[position].orderId,
            orderStatus = ConstantObjects.Canceled
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        myOrdersViewModel.closeAllCall()
    }

    private fun showImagePreviewDialog(imageUrl: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_image_preview)

        val imageView = dialog.findViewById<ImageView>(R.id.imagePreview)

        Glide.with(this)
            .load(imageUrl)
            .error(R.drawable.error_image)
            .into(imageView)

        imageView.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.show()
    }

    /**
     * Reset all views first
     */
    fun resetAllViewsFirst() {
        binding.linearPaymentMethod.visibility = View.GONE
        binding.cardUploadBankTransferDocument.visibility = View.GONE
        binding.textUploadBankTransferDocument.text = getString(R.string.upload_bank_transfer_document)
        binding.textUploadBankTransferDocument.setTextColor(Color.BLACK)
        binding.buttonExtend.visibility = View.GONE
        binding.buttonUpload.visibility = View.GONE
        binding.cardPaymentInvoiceAttached.visibility = View.GONE
        binding.linearConfirmConfirm.visibility = View.GONE
        binding.buttonDownload.visibility = View.GONE
    }
}
