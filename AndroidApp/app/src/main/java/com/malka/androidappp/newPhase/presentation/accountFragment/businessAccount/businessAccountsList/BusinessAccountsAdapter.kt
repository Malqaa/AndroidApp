package com.malka.androidappp.newPhase.presentation.accountFragment.businessAccount.businessAccountsList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.malka.androidappp.R
import com.malka.androidappp.databinding.ItemSwitchAccountDesignBinding
import com.malka.androidappp.newPhase.domain.models.bussinessAccountsListResp.businessAccountDetails
import com.squareup.picasso.Picasso

class BusinessAccountsAdapter(var dataList: List<businessAccountDetails>,var setOnBusinessAccountSelected:SetOnBusinessAccountSelected) :
    RecyclerView.Adapter<BusinessAccountsAdapter.BusinessAccountViewHolder>() {
    lateinit var context: Context

    class BusinessAccountViewHolder(var viewBinding: ItemSwitchAccountDesignBinding) :
        ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessAccountViewHolder {
        context = parent.context
        return BusinessAccountViewHolder(
            ItemSwitchAccountDesignBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: BusinessAccountViewHolder, position: Int) {
        Picasso.get().load( dataList[position].businessAccountImage)
            .error(R.drawable.profileicon_bottomnav)
            .placeholder(R.drawable.profileicon_bottomnav)
            .into(  holder.viewBinding.reviewProfilePic)
        holder.viewBinding.accountType.text = context.getString(R.string.business_account)
        holder.viewBinding.businessName.text = dataList[position].businessAccountName?:""
    }

    interface SetOnBusinessAccountSelected{
        fun setOnBusinessAccountSelected(position: Int)
        fun setOnSwitchAccount(position: Int)
    }
}