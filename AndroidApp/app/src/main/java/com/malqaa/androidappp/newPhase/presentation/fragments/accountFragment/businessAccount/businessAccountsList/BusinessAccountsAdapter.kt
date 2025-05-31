package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.businessAccount.businessAccountsList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemSwitchAccountDesignBinding
import com.malqaa.androidappp.newPhase.domain.models.bussinessAccountsListResp.BusinessAccountDetials
import com.malqaa.androidappp.newPhase.utils.PicassoSingleton.getPicassoInstance

class BusinessAccountsAdapter(
    var dataList: List<BusinessAccountDetials>,
    var setOnBusinessAccountSelected: SetOnBusinessAccountSelected
) :
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
        val details: BusinessAccountDetials = dataList[position]
        if (details.businessAccountImage != null && details.businessAccountImage != "")
            getPicassoInstance().load(details.businessAccountImage)
                .error(R.drawable.profileicon_bottomnav)
                .placeholder(R.drawable.profileicon_bottomnav)
                .into(holder.viewBinding.reviewProfilePic)
        holder.viewBinding.accountType.text = context.getString(R.string.business_account)
        holder.viewBinding.businessName.text = details.businessAccountName ?: ""
        holder.viewBinding.reviewRating.setOnClickListener {
            setOnBusinessAccountSelected.setOnSwitchAccount(position)
        }
    }

    interface SetOnBusinessAccountSelected {
        fun setOnBusinessAccountSelected(position: Int)
        fun setOnSwitchAccount(position: Int)
    }
}
