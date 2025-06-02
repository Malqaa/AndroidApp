package com.malka.androidappp.featuredcategory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.activities_main.FeaturedCategory
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.itemselling_cardview.view.*

class FeaturedAdapter(
    val sellposts: ArrayList<FeaturedModel>,
    var context: FeaturedCategory
) : RecyclerView.Adapter<FeaturedAdapter.FeaturedAdapterViewHolder>()
{


    class FeaturedAdapterViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        val sellimgg: ImageView = itemview.productimg
        val sellprodnm : TextView = itemview.textView18
        val sellproddate: TextView = itemview.textView16
        val sellprodprice : TextView = itemview.textView17
        val sellprodbuynow: TextView = itemview.itemsellbuynow

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedAdapterViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.itemselling_cardview,parent,false)
        return FeaturedAdapterViewHolder(view)
    }

    override fun getItemCount() = sellposts.size

    override fun onBindViewHolder(holder: FeaturedAdapterViewHolder, position: Int) {

        sellposts[position].sellprodimg?.let { holder.sellimgg.setImageResource(it) }
        holder.sellprodnm.text  = sellposts[position].sellprodname
        holder.sellproddate.text = sellposts[position].sellproddate
        holder.sellprodprice.text  = sellposts[position].sellprice
        holder.sellprodbuynow.text = sellposts[position].sellbuynow


    }


}