package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myWalletFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.domain.models.walletDetailsResp.WalletPendingOrders

class PurchaseOrderAdapter (
    private val orders: List<WalletPendingOrders>
) : RecyclerView.Adapter<PurchaseOrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderText: TextView = view.findViewById(R.id.orderText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_purchase_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.orderText.text = "● ${order.transactionAmount} S.R - Purchase Order No. ${order.orderId}"
    }

    override fun getItemCount(): Int = orders.size
}