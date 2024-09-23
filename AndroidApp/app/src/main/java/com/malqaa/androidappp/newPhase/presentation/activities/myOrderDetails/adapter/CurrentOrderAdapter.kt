package com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemShipmentInOrderBinding
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderFullInfoDto
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderProductFullInfoDto
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.AttachInvoice
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show

class CurrentOrderAdapter(
    private var orderFullInfoDto: List<OrderFullInfoDto>,
    private var setOnClickListeners: SetOnClickListeners
) : RecyclerView.Adapter<CurrentOrderAdapter.OrderDetailsViewHOlder>() {
    class OrderDetailsViewHOlder(
        var viewBinding: ItemShipmentInOrderBinding
    ) : ViewHolder(viewBinding.root)

    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHOlder {
        this.context = parent.context
        return OrderDetailsViewHOlder(
            ItemShipmentInOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return orderFullInfoDto.size
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHOlder, position: Int) {
        val orderFullInfo = orderFullInfoDto[position]

        holder.viewBinding.shipmentNoTv.text = "${context.getString(R.string.shipment_no_1)} ${orderFullInfo.orderId}"
        holder.viewBinding.tvSellerName.text = "(${orderFullInfo.providerName ?: ""})"
        holder.viewBinding.tvShippingStatus.text = orderFullInfo.status ?: ""

        for (item in orderFullInfo.orderProductFullInfoDto!!) {
            val customDropDownAdapter =
                CustomDropDownAdapter(context, item.productBankAccountsDto ?: arrayListOf())
            holder.viewBinding.spPaymentType.adapter = customDropDownAdapter
        }

        holder.viewBinding.tvOldTotalPrice.text =
            orderFullInfo.totalOrderPrice.toString()
        setadapter(holder.viewBinding.rvCart, orderFullInfo.orderProductFullInfoDto)

        holder.viewBinding.btnDownloadInvoice.setOnClickListener {
            setOnClickListeners.onDownloadInvoiceSelected(position)
        }

        holder.viewBinding.btnRateShipment.setOnClickListener {
            setOnClickListeners.onAddRateToShipmentSelected(position)
        }

        holder.viewBinding.btnCancel.setOnClickListener {
            if ((orderFullInfo.orderStatus == ConstantObjects.InProgress) || (orderFullInfo.orderStatus == ConstantObjects.WaitingForReview || (orderFullInfo.orderStatus == ConstantObjects.WaitingForPayment))) {
                setOnClickListeners.onCancelOrder(position)
            }
        }

        holder.viewBinding.confirmBankTr.setOnClickListener {
            val intent = Intent(context, AttachInvoice::class.java)
            intent.putExtra("orderId", orderFullInfo.orderId)
            context.startActivity(intent)
        }

        holder.viewBinding.confirmBank.hide()
        holder.viewBinding.laySelectBank.hide()
        holder.viewBinding.tvBank.hide()
        if (!orderFullInfo.orderProductFullInfoDto.isNullOrEmpty()) {
            holder.viewBinding.tvTypePayment.text =
                orderFullInfo.orderProductFullInfoDto[0].paymentOption
        }

        if (orderFullInfo.orderStatus == ConstantObjects.WaitingForPayment) {

            if (orderFullInfo.orderSaleType == "Auction" || (orderFullInfo.orderSaleType == "Negotiation")) {
                holder.viewBinding.btnCancel.hide()
            } else {
                holder.viewBinding.btnCancel.show()
                holder.viewBinding.laySelectBank.show()
            }

        } else if (orderFullInfo.orderStatus == ConstantObjects.WaitingForReview || orderFullInfo.orderStatus == ConstantObjects.InProgress || (orderFullInfo.orderStatus == ConstantObjects.DeliveryInProgress)) {
            if (orderFullInfo.paymentTypeId == 2) {
                holder.viewBinding.tvBank.show()
                holder.viewBinding.confirmBank.show()
            } else {
                holder.viewBinding.tvTypePayment.show()
            }

            holder.viewBinding.btnCancel.show()
        } else if (orderFullInfo.orderStatus == ConstantObjects.Delivered) {
            holder.viewBinding.btnCancel.hide()
            holder.viewBinding.finishOrderStatusContainer.show()

            val orderInvoice = orderFullInfo.orderInvoice
            if (orderInvoice.isNotEmpty()) {
                holder.viewBinding.btnDownloadInvoice.isEnabled = true
                holder.viewBinding.btnDownloadInvoice.setBackgroundResource(R.drawable.round_black)
            } else {
                holder.viewBinding.btnDownloadInvoice.isEnabled = false
                holder.viewBinding.btnDownloadInvoice.setBackgroundResource(R.drawable.background_curve_slider_gray)
            }

            if (orderFullInfo.paymentTypeId == 2) {
                holder.viewBinding.tvBank.show()
                holder.viewBinding.confirmBank.show()
            } else {
                holder.viewBinding.tvTypePayment.show()
            }

        } else {
            if (orderFullInfo.orderStatus == ConstantObjects.Canceled) {
                holder.viewBinding.btnCancel.setBackgroundResource(R.drawable.edittext_bg)
                holder.viewBinding.btnCancel.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.gray
                    )
                )
                holder.viewBinding.btnCancel.text = context.getString(R.string.Canceled)
            } else {
                holder.viewBinding.btnCancel.text = context.getString(R.string.cancelAshipmentOrder)
                holder.viewBinding.btnCancel.setBackgroundResource(R.drawable.round_btn)
                holder.viewBinding.btnCancel.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
            }
        }
    }

    private fun setadapter(
        rvCart: RecyclerView,
        orderProductFullInfoDto: List<OrderProductFullInfoDto>?
    ) {
        var dataList = ArrayList<OrderProductFullInfoDto>()
        orderProductFullInfoDto?.let { dataList.addAll(it) }
        var orderProductAdapter = OrderProductAdapter(dataList)
        rvCart.apply {
            adapter = orderProductAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            isNestedScrollingEnabled = false
        }
    }

    interface SetOnClickListeners {
        fun onDownloadInvoiceSelected(position: Int)
        fun onAddRateToShipmentSelected(position: Int)
        fun onCancelOrder(position: Int)
    }
}