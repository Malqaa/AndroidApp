package com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.malka.androidappp.R
import com.malka.androidappp.databinding.ItemReviewProductBinding
import com.malka.androidappp.newPhase.data.helper.Extension
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.ratingResp.RateReviewItem

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
        holder.viewBinding.tvDate.text=HelpFunctions.getViewFormatForDateTrack(smallRatesList[position].createdAt)
        holder.viewBinding.reviewNameTv.text=smallRatesList[position].userName
        holder.viewBinding.commentTv.text=smallRatesList[position].comment
        holder.viewBinding.ratingBar.rating=smallRatesList[position].rate
        Glide.with(context).load(smallRatesList[position].image).error(R.mipmap.malqa_iconn_round).into( holder.viewBinding.ivProfile)
        if(position==smallRatesList.size-1){
            holder.viewBinding.line.hide()
        }else{
            holder.viewBinding.line.show()
        }
    }
}