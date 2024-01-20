package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemReviewProductBinding
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.domain.models.ratingResp.RateReviewItem

class ReviewProductAdapter(var smallRatesList: ArrayList<RateReviewItem>) :
    RecyclerView.Adapter<ReviewProductAdapter.ReviewViewHolder>() {
    lateinit var context: Context

    class ReviewViewHolder(var viewBinding: ItemReviewProductBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        context = parent.context
        return ReviewViewHolder(
                ItemReviewProductBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = smallRatesList.size

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.viewBinding.tvDate.text= HelpFunctions.getViewFormatForDateTrack(smallRatesList[position].createdAt)
        holder.viewBinding.reviewNameTv.text=smallRatesList[position].userName
        holder.viewBinding.commentTv.text=smallRatesList[position].comment
        when(smallRatesList[position].rate){
            3f->{
                holder.viewBinding.ratingBar.setImageResource(R.drawable.happyface_color)
            }
            2f->{
                holder.viewBinding.ratingBar.setImageResource(R.drawable.smileface_color)
            }
            1f->{
                holder.viewBinding.ratingBar.setImageResource(R.drawable.sadcolor_gray)
            }
        }
        Glide.with(context).load(smallRatesList[position].image).error(R.mipmap.malqa_iconn_round).into( holder.viewBinding.ivProfile)
        if(position==smallRatesList.size-1){
            holder.viewBinding.line.hide()
        }else{
            holder.viewBinding.line.show()
        }
    }
}