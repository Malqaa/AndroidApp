package com.malka.androidappp.activities_main.product_detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.malka.androidappp.R

class RateAdapter(val context: Context, val rateData: List<RateResponse.RateReview>): RecyclerView.Adapter<RateAdapter.RateViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.product_review_design,parent,false)
        return RateViewHolder(view)
    }
    override fun getItemCount(): Int {
        return rateData.size
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        val Rate = rateData[position]
        holder.name.text = Rate.userName
        holder.comment.text = Rate.comment
        holder.ratetext.text = Rate.rate
        holder.date.text = Rate.createdAt
        Glide.with(context).load(holder.image).into(holder.image)
    }



    class RateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val image = itemView.findViewById<ImageView>(R.id.review_profile_pic)
        val name = itemView.findViewById<TextView>(R.id.review_name)
        val comment = itemView.findViewById<TextView>(R.id.review_comment)
        val ratetext = itemView.findViewById<TextView>(R.id.review_rating)
        val date = itemView.findViewById<TextView>(R.id.review_date)
    }
}