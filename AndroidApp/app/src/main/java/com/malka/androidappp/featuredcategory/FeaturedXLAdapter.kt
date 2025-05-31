package com.malka.androidappp.featuredcategory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.activities_main.FeaturedCategory
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.itemselling_cardview2.view.*

class FeaturedXLAdapter (
    val sellxlposts: ArrayList<FeaturedModel>,
    var context: FeaturedCategory
) : RecyclerView.Adapter<FeaturedXLAdapter.FeaturedXLAdapterViewHolder>()
{


    class FeaturedXLAdapterViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        val sellimgg: ImageView = itemview.myimagee_featuredgeneral
        val sellprodnm : TextView = itemview.titlenamee_featuredgeneral
        val sellprodprice : TextView = itemview.pricee_featuredgeneral
        val sellprodbuynow: TextView = itemview.sellxlbuynow

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedXLAdapterViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.itemselling_cardview2,parent,false)
        return FeaturedXLAdapterViewHolder(view)
    }

    override fun getItemCount() = sellxlposts.size

    override fun onBindViewHolder(holder: FeaturedXLAdapterViewHolder, position: Int) {

        sellxlposts[position].sellprodimg?.let { holder.sellimgg.setImageResource(it) }
        holder.sellprodnm.text  = sellxlposts[position].sellprodname
        holder.sellprodprice.text  = sellxlposts[position].sellprice
        holder.sellprodbuynow.text = sellxlposts[position].sellbuynow


    }


}