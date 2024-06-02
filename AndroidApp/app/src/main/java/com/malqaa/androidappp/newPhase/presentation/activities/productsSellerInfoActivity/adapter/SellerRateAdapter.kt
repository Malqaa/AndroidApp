package com.malqaa.androidappp.newPhase.presentation.activities.productsSellerInfoActivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.domain.models.sellerRateListResp.SellerRateItem
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_product_details_item_2.ivRateSeller

class SellerRateAdapter(
    val context: Context,
    private var sellerRateList: ArrayList<SellerRateItem>
) :
    RecyclerView.Adapter<SellerRateAdapter.SellerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_seller_review, parent, false)
        return SellerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sellerRateList.size
    }

    override fun onBindViewHolder(holder: SellerViewHolder, position: Int) {
        val rate = sellerRateList[position]
        holder.name.text = rate.userName
        holder.comment.text = rate.comment
        holder.rateText.text = rate.rate.toString()
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
        holder.date.text =
            HelpFunctions.getViewFormatForDateTrack(rate.createdAt, "dd/MM/yyyy")
        Glide.with(context).load(rate.imgProfile).error(R.mipmap.ic_launcher_round)
            .into(holder.image)
    }


    class SellerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: CircleImageView = itemView.findViewById(R.id.review_profile_pic)
        val name: TextView = itemView.findViewById(R.id.review_name)
        val comment: TextView = itemView.findViewById(R.id.review_comment)
        val rateText: TextView = itemView.findViewById(R.id.review_rating)
        val ivRateSeller: ImageView = itemView.findViewById(R.id.ivRateSeller)
        val date: TextView = itemView.findViewById(R.id.review_date)
    }
}