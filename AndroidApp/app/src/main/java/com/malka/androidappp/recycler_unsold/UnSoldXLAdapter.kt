package com.malka.androidappp.recycler_unsold

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.UnsoldFragment
import kotlinx.android.synthetic.main.itemselling_cardview2.view.*

class UnSoldXLAdapter(
    val unsoldxlposts: ArrayList<UnSoldModel>,
    var context: UnsoldFragment
) : RecyclerView.Adapter<UnSoldXLAdapter.UnSoldXLAdapterViewHolder>()
{


    class UnSoldXLAdapterViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        val unsoldimgg: ImageView = itemview.myimagee_featuredgeneral
        val unsoldprodnm : TextView = itemview.titlenamee_featuredgeneral
        val unsoldprodprice : TextView = itemview.pricee_featuredgeneral
        val unsoldprodbuynow: TextView = itemview.sellxlbuynow

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnSoldXLAdapterViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.itemselling_cardview2,parent,false)
        return UnSoldXLAdapterViewHolder(view)
    }

    override fun getItemCount() = unsoldxlposts.size

    override fun onBindViewHolder(holder: UnSoldXLAdapterViewHolder, position: Int) {

        unsoldxlposts[position].unsoldprodimg?.let { holder.unsoldimgg.setImageResource(it) }
        holder.unsoldprodnm.text  = unsoldxlposts[position].unsoldprodname
        holder.unsoldprodprice.text  = unsoldxlposts[position].unsoldprice
        holder.unsoldprodbuynow.text = unsoldxlposts[position].unsoldbuynow


    }


}