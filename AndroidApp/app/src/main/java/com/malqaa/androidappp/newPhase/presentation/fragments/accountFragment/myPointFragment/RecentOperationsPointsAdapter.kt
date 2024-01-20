package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myPointFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.databinding.ItemWalletRecentOperationsBinding
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.domain.models.userPointsDataResp.PointsTransactionsItem


class RecentOperationsPointsAdapter (var  pointsTransactionslist: List<PointsTransactionsItem>) :
    RecyclerView.Adapter<RecentOperationsPointsAdapter.RecentOperationViewHolder>() {
    class RecentOperationViewHolder(var viewBinding: ItemWalletRecentOperationsBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentOperationViewHolder {
        return RecentOperationViewHolder(
            ItemWalletRecentOperationsBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int = pointsTransactionslist.size

    override fun onBindViewHolder(holder: RecentOperationViewHolder, position: Int) {
        holder.viewBinding.tvAmount.text= pointsTransactionslist[position].transactionAmount.toString()
        holder.viewBinding.tvDescription.text =
            pointsTransactionslist[position].transactionSource ?: ""
        holder.viewBinding.tvDate.text= HelpFunctions.getViewFormatForDateTrack(pointsTransactionslist[position].transactionDate)
    }
}