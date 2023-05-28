package com.malka.androidappp.newPhase.presentation.accountFragment.myWalletFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.malka.androidappp.databinding.ItemWalletRecentOperationsBinding
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.domain.models.walletDetailsResp.WalletTransactionsDetails

class RecentOperationsAdapter(var walletTransactionslist: ArrayList<WalletTransactionsDetails>) :
    Adapter<RecentOperationsAdapter.RecentOperationViewHolder>() {
    class RecentOperationViewHolder(var viewBinding: ItemWalletRecentOperationsBinding) :
        ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentOperationViewHolder {
        return RecentOperationViewHolder(
            ItemWalletRecentOperationsBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int = walletTransactionslist.size

    override fun onBindViewHolder(holder: RecentOperationViewHolder, position: Int) {
        holder.viewBinding.tvAmount.text= walletTransactionslist[position].transactionAmount.toString()
        holder.viewBinding.tvDescription.text =
            walletTransactionslist[position].transactionSource ?: ""
        holder.viewBinding.tvDate.text=HelpFunctions.getViewFormatForDateTrack(walletTransactionslist[position].transactionDate)
    }
}