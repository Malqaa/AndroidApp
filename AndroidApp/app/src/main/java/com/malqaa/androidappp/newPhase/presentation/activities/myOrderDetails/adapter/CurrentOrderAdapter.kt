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
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderFullInfoDto
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderProductFullInfoDto
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.AttachInvoice


class CurrentOrderAdapter(
    var orderFullInfoDto: List<OrderFullInfoDto>,
    var setOnClickListeners: SetOnClickListeners
) :
    RecyclerView.Adapter<CurrentOrderAdapter.OrderDetailsViewHOlder>() {
    class OrderDetailsViewHOlder(var viewBinding: ItemShipmentInOrderBinding) :
        ViewHolder(viewBinding.root)

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
        holder.viewBinding.shipmentNoTv.text =
            "${context.getString(R.string.shipment_no_1)} ${orderFullInfoDto[position].orderId}"
        holder.viewBinding.tvSellerName.text = "(${orderFullInfoDto[position].providerName ?: ""})"
        holder.viewBinding.tvShippingStatus.text = orderFullInfoDto[position].status ?: ""
//        when (orderFullInfoDto[position].orderStatus) {
//            ConstantObjects.WaitingForPayment -> {
//                holder.viewBinding.tvShippingStatus.text =
//                    context.getString(R.string.WaitingForPayment)
//            }
//
//            ConstantObjects.Retrieved -> {
//                holder.viewBinding.tvShippingStatus.text =
//                    context.getString(R.string.order_productsProcessing)
//            }
//
//            ConstantObjects.InProgress -> {
//                holder.viewBinding.tvShippingStatus.text =
//                    context.getString(R.string.order_deliveryPhase)
//            }
//
//            ConstantObjects.DeliveryInProgress -> {
//                holder.viewBinding.tvShippingStatus.text =
//                    context.getString(R.string.order_deliveryConfirmation)
//            }
//        }

        for (item in orderFullInfoDto[position].orderProductFullInfoDto!!) {

            val customDropDownAdapter =
                CustomDropDownAdapter(context, item.productBankAccountsDto ?: arrayListOf())
            holder.viewBinding.spPaymentType.adapter = customDropDownAdapter
        }


        holder.viewBinding.tvOldTotalPrice.text =
            orderFullInfoDto[position].totalOrderPrice.toString()
        setadapter(holder.viewBinding.rvCart, orderFullInfoDto[position].orderProductFullInfoDto)
        holder.viewBinding.btnRateShipment.setOnClickListener {
            setOnClickListeners.onAddRateToShipmentSelected(position)
        }


        holder.viewBinding.btnCancel.setOnClickListener {
            if ((orderFullInfoDto[position].orderStatus == ConstantObjects.InProgress) || (orderFullInfoDto[position].orderStatus == ConstantObjects.WaitingForReview || (orderFullInfoDto[position].orderStatus == ConstantObjects.WaitingForPayment))) {
                setOnClickListeners.onCancelOrder(position)
            }
        }
//        if (orderFullInfoDto[position].orderStatus == 6 || orderFullInfoDto[position].orderStatus == 5) {
//            holder.viewBinding.btnCancel.setBackgroundResource(R.drawable.edittext_bg)
//            holder.viewBinding.btnCancel.setTextColor(ContextCompat.getColor(context, R.color.gray))
//            holder.viewBinding.btnCancel.text = context.getString(R.string.Canceled)
//        } else {
//            holder.viewBinding.btnCancel.text = context.getString(R.string.Cancel)
//            holder.viewBinding.btnCancel.setBackgroundResource(R.drawable.round_btn)
//            holder.viewBinding.btnCancel.setTextColor(
//                ContextCompat.getColor(
//                    context,
//                    R.color.white
//                )
//            )
//        }

        holder.viewBinding.confirmBankTr.setOnClickListener {
            val intent = Intent(context, AttachInvoice::class.java)
            intent.putExtra("orderId", orderFullInfoDto[position].orderId)
            context.startActivity(intent)
        }

        holder.viewBinding.confirmBank.hide()
        holder.viewBinding.laySelectBank.hide()
        holder.viewBinding.tvBank.hide()
        if(!orderFullInfoDto[position].orderProductFullInfoDto.isNullOrEmpty()){
            holder.viewBinding.tvTypePayment.text=
                orderFullInfoDto[position].orderProductFullInfoDto?.get(0)?.paymentOption
        }

        if (orderFullInfoDto[position].orderStatus == ConstantObjects.WaitingForPayment) {

            if (orderFullInfoDto[position].orderSaleType == "Auction" || (orderFullInfoDto[position].orderSaleType == "Negotiation")) {
                holder.viewBinding.btnCancel.hide()
            } else {
                holder.viewBinding.btnCancel.show()
                holder.viewBinding.laySelectBank.show()
            }

        } else if (orderFullInfoDto[position].orderStatus == ConstantObjects.WaitingForReview || orderFullInfoDto[position].orderStatus == ConstantObjects.InProgress || (orderFullInfoDto[position].orderStatus == ConstantObjects.DeliveryInProgress) ) {
            if (orderFullInfoDto[position].paymentTypeId == 2) {
                holder.viewBinding.tvBank.show()
                holder.viewBinding.confirmBank.show()
            }else
                holder.viewBinding.tvTypePayment.show()

            holder.viewBinding.btnCancel.show()
        } else if (orderFullInfoDto[position].orderStatus == ConstantObjects.Delivered) {
            holder.viewBinding.btnCancel.hide()
            holder.viewBinding.finishOrderStatusContainer.show()
            if (orderFullInfoDto[position].paymentTypeId == 2) {
                holder.viewBinding.tvBank.show()
                holder.viewBinding.confirmBank.show()
            }else
            holder.viewBinding.tvTypePayment.show()


        } else {
            if (orderFullInfoDto[position].orderStatus == ConstantObjects.Canceled) {
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
        orderProductFullInfoDto?.let {
            dataList.addAll(it)
        }
        var orderProductAdapter = OrderProductAdapter(dataList)
        rvCart.apply {
            adapter = orderProductAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            isNestedScrollingEnabled = false
        }
    }

    interface SetOnClickListeners {
        fun onAddRateToShipmentSelected(position: Int)
        fun onCancelOrder(position: Int)
    }
}