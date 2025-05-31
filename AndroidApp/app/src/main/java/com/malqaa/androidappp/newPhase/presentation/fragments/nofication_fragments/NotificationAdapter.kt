package com.malqaa.androidappp.newPhase.presentation.fragments.nofication_fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.databinding.NotificationFragmentDesignBinding
import com.malqaa.androidappp.newPhase.domain.models.NotifyOut


class NotificationAdapter(private var itemList: ArrayList<NotifyOut>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationViewHolder {
        return NotificationViewHolder(
            NotificationFragmentDesignBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = itemList.size


    override fun onBindViewHolder(
        holder: NotificationViewHolder,
        position: Int
    ) {
        holder.viewBinding.title.text = itemList[position].title
        holder.viewBinding.txtBody.text = itemList[position].body
    }

    fun updateAdapter(itemList: ArrayList<NotifyOut>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    class NotificationViewHolder(var viewBinding: NotificationFragmentDesignBinding) :
        RecyclerView.ViewHolder(viewBinding.root)
}
