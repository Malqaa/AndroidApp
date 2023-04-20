package com.malka.androidappp.newPhase.presentation.myOrderFragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.malka.androidappp.R


import com.malka.androidappp.databinding.ItemUserOrderBinding
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.orderListResp.OrderItem

class SoldOutOrdersAdapter(var orderList: List<OrderItem>) :
    Adapter<SoldOutOrdersAdapter.SoldOutOrdersViewHolder>() {
    var currentOrder=true
    class SoldOutOrdersViewHolder(var viewBinding: ItemUserOrderBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoldOutOrdersViewHolder {
        context = parent.context
        return SoldOutOrdersViewHolder(
            ItemUserOrderBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = orderList.size

    override fun onBindViewHolder(holder: SoldOutOrdersViewHolder, position: Int) {
        if(currentOrder) {
            holder.viewBinding.orderNumberTv.text = "#${orderList[position].orderMasterId}"
        }else{
            holder.viewBinding.orderNumberTv.text = "#${orderList[position].orderId}"
        }
        holder.viewBinding.tvRequestType.text=orderList[position].requestType
        holder.viewBinding.orderTimeTv.text = HelpFunctions.getViewFormatForDateTrack(orderList[position].createdAt)
        holder.viewBinding.shipmentsTv.text=orderList[position].providersCount.toString()
        holder.viewBinding.totalOrderTv.text = "${orderList[position].totalOrderAmountAfterDiscount} ${context.getString(R.string.rial)}"
        holder.viewBinding.orderStatusTv.text = orderList[position].status ?: ""
        if(currentOrder){
            holder.viewBinding.completeOrderBtn.show()
        }else{
            holder.viewBinding.completeOrderBtn.hide()
        }


    }

}