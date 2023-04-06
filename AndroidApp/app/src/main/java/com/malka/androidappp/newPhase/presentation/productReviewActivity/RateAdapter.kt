package com.malka.androidappp.newPhase.presentation.productReviewActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.domain.models.ratingResp.RateResponse
import com.malka.androidappp.newPhase.domain.models.ratingResp.RateReviewItem
import de.hdodenhof.circleimageview.CircleImageView

class RateAdapter(val context: Context, var rateData: List<RateReviewItem>):
    RecyclerView.Adapter<RateAdapter.RateViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.product_review_design,parent,false)
        return RateViewHolder(view)
    }
    override fun getItemCount(): Int {
        return rateData.size
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        val rate = rateData[position]
        holder.name.text = rate.userName
        holder.comment.text = rate.comment
        holder.ratetext.text = rate.rate.toString()
        holder.date.text = HelpFunctions.getViewFormatForDateTrack(rate.createdAt)
        Glide.with(context).load(rate.image).error(R.mipmap.malqa_iconn_round).into(holder.image)
    }



    class RateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<CircleImageView>(R.id.review_profile_pic)
        val name = itemView.findViewById<TextView>(R.id.review_name)
        val comment = itemView.findViewById<TextView>(R.id.review_comment)
        val ratetext = itemView.findViewById<TextView>(R.id.review_rating)
        val date = itemView.findViewById<TextView>(R.id.review_date)
    }
}