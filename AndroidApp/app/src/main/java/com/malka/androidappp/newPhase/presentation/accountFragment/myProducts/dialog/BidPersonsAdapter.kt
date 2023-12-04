package com.malka.androidappp.newPhase.presentation.accountFragment.myProducts.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.malka.androidappp.R
import com.malka.androidappp.databinding.ItemBidPersonBinding
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.domain.models.bidPersonsResp.BidPersonData

class BidPersonsAdapter(
    var bidPersonsDataList: ArrayList<BidPersonData>,
    var setOnViewClickListeners: SetOnViewClickListeners,
    var fromProductDetails: Boolean
) :
    Adapter<BidPersonsAdapter.BidPersonsViewHolder>() {
    lateinit var context: Context

    class BidPersonsViewHolder(var viewBinding: ItemBidPersonBinding) : ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BidPersonsViewHolder {
        context = parent.context
        return BidPersonsViewHolder(
            ItemBidPersonBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = bidPersonsDataList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BidPersonsViewHolder, position: Int) {

     if(bidPersonsDataList[position].userId==ConstantObjects.logged_userid){
         holder.viewBinding.tvName.text = context.getString(R.string.you)

     }else{
         holder.viewBinding.tvName.text = bidPersonsDataList[position].userName ?: ""

     }
        Extension.loadThumbnail(
            context,
            bidPersonsDataList[position].userImage?:"",
            holder.viewBinding.tvImage,
            holder.viewBinding.loader
        )
        holder.viewBinding.tvOfferedPrice.text =
            "${bidPersonsDataList[position].bidPrice.toString()} ${
                context.getString(
                    R.string.SAR
                )
            }"
        holder.viewBinding.tvDate.text =HelpFunctions.getViewFormatForDateTrack(bidPersonsDataList[position].createdAt)

     if(bidPersonsDataList[position].isSelected){
         holder.viewBinding.ivCheck.setImageResource(R.drawable.checkbox_selected)
     }else{
         holder.viewBinding.ivCheck.setImageResource(R.drawable.checkbox_un_selected)
     }
        holder.viewBinding.ivCheck.setOnClickListener {
            setOnViewClickListeners.setOnBidSelect(position)
        }

        if(fromProductDetails){
            holder.viewBinding.ivCheck.hide()
        }else{
            holder.viewBinding.ivCheck.show()
        }
    }
    interface SetOnViewClickListeners{
        fun setOnBidSelect(position:Int)
    }
}