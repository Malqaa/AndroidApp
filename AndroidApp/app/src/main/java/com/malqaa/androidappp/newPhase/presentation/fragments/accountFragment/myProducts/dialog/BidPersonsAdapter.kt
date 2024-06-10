package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myProducts.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemBidPersonBinding
import com.malqaa.androidappp.newPhase.utils.helper.*
import com.malqaa.androidappp.newPhase.domain.models.bidPersonsResp.BidPersonData
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.Extension
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

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

     if(bidPersonsDataList[position].userId== ConstantObjects.logged_userid){
         holder.viewBinding.tvName.text = context.getString(R.string.you)

     }else{
         holder.viewBinding.tvName.text = bidPersonsDataList[position].userName ?: ""

     }
        Extension.loadImgGlide(
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
        holder.viewBinding.tvDate.text = HelpFunctions.getViewFormatForDateTrack(bidPersonsDataList[position].createdAt,"dd/MM/yyyy")

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