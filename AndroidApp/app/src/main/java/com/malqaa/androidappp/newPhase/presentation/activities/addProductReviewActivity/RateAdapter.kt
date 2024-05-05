package com.malqaa.androidappp.newPhase.presentation.activities.addProductReviewActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.domain.models.ratingResp.RateReviewItem
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_product_details_item_2.ivRateSeller

class RateAdapter(val context: Context, var rateData: List<RateReviewItem>):
    RecyclerView.Adapter<RateAdapter.RateViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.item_seller_review,parent,false)
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
       // holder.date.text = HelpFunctions.getViewFormatForDateTrack(rate.createdAt)
        Glide.with(context).load(rate.image).error(R.mipmap.malqa_iconn_round).into(holder.image)
        when (rate.rate) {
            3f -> {
                holder.ivRateSeller.setImageResource(R.drawable.happyface_color)
            }

            2f -> {
                holder.ivRateSeller.setImageResource(R.drawable.smileface_color)
            }

            1f -> {
                holder.ivRateSeller.setImageResource(R.drawable.sadcolor_gray)
            }

            else -> {
                holder.ivRateSeller.setImageResource(R.drawable.smileface_color)
            }
        }
    }



    class RateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<CircleImageView>(R.id.review_profile_pic)
        val name = itemView.findViewById<TextView>(R.id.review_name)
        val comment = itemView.findViewById<TextView>(R.id.review_comment)
        val ratetext = itemView.findViewById<TextView>(R.id.review_rating)
        val ivRateSeller = itemView.findViewById<ImageView>(R.id.ivRateSeller)

       // val date = itemView.findViewById<TextView>(R.id.review_date)
    }
}