package com.malka.androidappp.newPhase.presentation.myOrderDetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.malka.androidappp.R
import com.malka.androidappp.databinding.ItemShipmentInOrderBinding
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderFullInfoDto
import com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderProductFullInfoDto
import kotlinx.android.synthetic.main.activity_my_order_details_requested_from_me.*
import kotlinx.android.synthetic.main.fragment_my_orders.*


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
//        holder.viewBinding.tvShippingStatus.text = orderFullInfoDto[position].status ?: ""
        when ( orderFullInfoDto[position].orderStatus) {
            ConstantObjects.orderStatus_provider_new -> {
                holder.viewBinding.tvShippingStatus.text =context.getString(R.string.order_new)
            }
            ConstantObjects.orderStatus_provider_inProgress -> {
                holder.viewBinding.tvShippingStatus.text = context.getString(R.string.order_productsProcessing)
            }
            ConstantObjects.orderStatus_provider_inDelivery -> {
                holder.viewBinding.tvShippingStatus.text = context.getString(R.string.order_deliveryPhase)
            }
            ConstantObjects.orderStatus_provider_finished -> {
                holder.viewBinding.tvShippingStatus.text = context.getString(R.string.order_deliveryConfirmation)
            }
        }

        holder.viewBinding.tvPaymentType.text = orderFullInfoDto[position].payType ?: ""
        holder.viewBinding.tvOldTotalPrice.text =
            orderFullInfoDto[position].totalOrderPrice.toString()
        setadapter(holder.viewBinding.rvCart, orderFullInfoDto[position].orderProductFullInfoDto)
        holder.viewBinding.btnRateShipment.setOnClickListener {
            setOnClickListeners.onAddRateToShipmentSelected(position)
        }
        holder.viewBinding.btnCancel.setOnClickListener {
            if (orderFullInfoDto[position].orderStatus != 6) {
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
        holder.viewBinding.btnCancel.hide()
        holder.viewBinding.finishOrderStatusContainer.hide()
        if (orderFullInfoDto[position].orderStatus == 5) {
            holder.viewBinding.btnCancel.hide()
            holder.viewBinding.finishOrderStatusContainer.show()
        } else {
            holder.viewBinding.btnCancel.show()
            if (orderFullInfoDto[position].orderStatus == 6) {
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
      //  holder.viewBinding.finishOrderStatusContainer.show()

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