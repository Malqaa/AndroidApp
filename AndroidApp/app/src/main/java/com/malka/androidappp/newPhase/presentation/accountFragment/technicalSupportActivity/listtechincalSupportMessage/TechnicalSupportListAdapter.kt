package com.malka.androidappp.newPhase.presentation.accountFragment.technicalSupportActivity.listtechincalSupportMessage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.malka.androidappp.databinding.ItemTechnicalMessageBinding
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.domain.models.contauctUsMessage.TechnicalSupportMessageDetails

class TechnicalSupportListAdapter(private var technicalSupportMessageList:List<TechnicalSupportMessageDetails>, private var setonClickListeners:SetonClickListeners) :Adapter<TechnicalSupportListAdapter.TechnicalSupportListViewHolder>() {
    class TechnicalSupportListViewHolder(var viewBinding:ItemTechnicalMessageBinding):ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TechnicalSupportListViewHolder {
      return TechnicalSupportListViewHolder(
          ItemTechnicalMessageBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int =technicalSupportMessageList.size

    override fun onBindViewHolder(holder: TechnicalSupportListViewHolder, position: Int) {
        holder.viewBinding.tvProblemTitle.text=technicalSupportMessageList[position].problemTitle?:""
        holder.viewBinding.tvDate.text=HelpFunctions.getViewFormatForDateTrack(technicalSupportMessageList[position].createdAt)
       holder.viewBinding.tvMessageDetails.text=technicalSupportMessageList[position].meassageDetails?:""
        holder.viewBinding.ivEdit.setOnClickListener {
            setonClickListeners.onEditClickListener(position)
        }
    }
    interface SetonClickListeners{
        fun onEditClickListener(position: Int)
    }
}