package com.malka.androidappp.newPhase.presentation.myOrderDetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.malka.androidappp.R
import com.malka.androidappp.databinding.ItemShipmentInOrderBinding
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderFullInfoDto
import com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderProductFullInfoDto

class CurrentOrderAdapter(var orderFullInfoDto: List<OrderFullInfoDto>,var setOnClickListeners:SetOnClickListeners) :
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
        holder.viewBinding.tvPaymentType.text=orderFullInfoDto[position].payType?:""
        holder.viewBinding.tvOldTotalPrice.text=orderFullInfoDto[position].totalOrderPrice.toString()
        setadapter( holder.viewBinding.rvCart,orderFullInfoDto[position].orderProductFullInfoDto)
       holder.viewBinding.btnRateShipment.setOnClickListener {
           setOnClickListeners.onAddRateToShipmentSelected(position)
       }

    }

    private fun setadapter(
        rvCart: RecyclerView,
        orderProductFullInfoDto: List<OrderProductFullInfoDto>?
    ) {
        var dataList=ArrayList<OrderProductFullInfoDto>()
        orderProductFullInfoDto?.let {
            dataList.addAll(it)
        }
        var orderProductAdapter = OrderProductAdapter(dataList)
        rvCart.apply {
            adapter = orderProductAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            isNestedScrollingEnabled=false
        }
    }
    
    interface SetOnClickListeners{
        fun onAddRateToShipmentSelected(position: Int)
    }
}