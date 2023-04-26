package com.malka.androidappp.newPhase.presentation.productsSellerInfoActivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.domain.models.sellerRateListResp.SellerRateItem
import de.hdodenhof.circleimageview.CircleImageView

class SellerRateAdapter(val context: Context, var sellerRateList: ArrayList<SellerRateItem>):
    RecyclerView.Adapter<SellerRateAdapter.SellerViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_seller_review,parent,false)
        return SellerViewHolder(view)
    }
    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: SellerViewHolder, position: Int) {
//        val rate = sellerRateList[position]
//        holder.name.text = rate.userName
//        holder.comment.text = rate.comment
//        holder.ratetext.text = rate.rate.toString()
//        holder.date.text = HelpFunctions.getViewFormatForDateTrack(rate.createdAt)
//        Glide.with(context).load(rate.image).error(R.mipmap.malqa_iconn_round).into(holder.image)
    }



    class SellerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<CircleImageView>(R.id.review_profile_pic)
        val name = itemView.findViewById<TextView>(R.id.review_name)
        val comment = itemView.findViewById<TextView>(R.id.review_comment)
        val ratetext = itemView.findViewById<TextView>(R.id.review_rating)
        val date = itemView.findViewById<TextView>(R.id.review_date)
    }
}