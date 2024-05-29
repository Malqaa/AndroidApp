package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myOrderFragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.malqaa.androidappp.R


import com.malqaa.androidappp.databinding.ItemUserOrderBinding
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.domain.models.orderListResp.OrderItem
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import kotlinx.android.synthetic.main.activity_order_details.tv_request_type

class MyOrdersAdapter(var orderList: List<OrderItem>, var setOnClickListeners:SetOnClickListeners) :
    Adapter<MyOrdersAdapter.SoldOutOrdersViewHolder>() {
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



        if(orderList[position].requestType?.lowercase().equals("FixedPrice".lowercase())){
            holder.viewBinding.tvRequestType.text=context.getString(R.string.fixed_price)
        }else if(orderList[position].requestType?.lowercase().equals("Negotiation".lowercase())){
            holder.viewBinding.tvRequestType.text=context.getString(R.string.Negotiation)
        }else if(orderList[position].requestType?.lowercase().equals("Auction".lowercase())){
            holder.viewBinding.tvRequestType.text=context.getString(R.string.auction)
        }

        holder.viewBinding.orderTimeTv.text = HelpFunctions.getViewFormatForDateTrackWithoutUTC(orderList[position].createdAt,"dd/MM/yyyy HH:mm:ss");
        holder.viewBinding.shipmentsTv.text=orderList[position].providersCount.toString()
        holder.viewBinding.totalOrderTv.text = "${orderList[position].totalOrderAmountAfterDiscount} ${context.getString(R.string.rial)}"

//        when ( orderList[position].orderStatus) {
//            ConstantObjects.WaitingForPayment -> {
//                holder.viewBinding.orderStatusTv.text =context.getString(R.string.WaitingForPayment)
//            }
//            ConstantObjects.Retrieved -> {
//                holder.viewBinding.orderStatusTv.text = context.getString(R.string.order_productsProcessing)
//            }
//            ConstantObjects.InProgress -> {
//                holder.viewBinding.orderStatusTv.text = context.getString(R.string.InProgress)
//            }
//            ConstantObjects.DeliveryInProgress -> {
//                holder.viewBinding.orderStatusTv.text = context.getString(R.string.order_deliveryConfirmation)
//            }
//            ConstantObjects.Canceled -> {
//                holder.viewBinding.orderStatusTv.text = context.getString(R.string.canceled)
//            }
//            else ->{
                holder.viewBinding.orderStatusTv.text = orderList[position].status ?: ""
//            }
//        }

        if(currentOrder){
            holder.viewBinding.completeOrderBtn.hide()
        }else{
            holder.viewBinding.completeOrderBtn.show()
        }
     holder.viewBinding.itemView.setOnClickListener {
         setOnClickListeners.onOrderSelected(position)
     }
        holder.viewBinding.completeOrderBtn.setOnClickListener {
            setOnClickListeners.onOrderSelected(position)
        }
   ConstantObjects
    }

    interface SetOnClickListeners{
        fun onOrderSelected(position: Int)
    }

}