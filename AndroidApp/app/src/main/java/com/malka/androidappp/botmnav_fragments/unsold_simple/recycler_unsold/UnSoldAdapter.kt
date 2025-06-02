package com.malka.androidappp.botmnav_fragments.unsold_simple.recycler_unsold

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.unsold_simple.UnsoldFragment
import kotlinx.android.synthetic.main.itemselling_cardview.view.*

class UnSoldAdapter (
    val unsoldposts: ArrayList<UnSoldModel>,
    var context: UnsoldFragment
) : RecyclerView.Adapter<UnSoldAdapter.UnSoldAdapterViewHolder>()
{


    class UnSoldAdapterViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        val unsoldimgg: ImageView = itemview.productimg
        val unsoldprodnm : TextView = itemview.textView18
        val unsoldproddate: TextView = itemview.textView16
        val unsoldprodprice : TextView = itemview.textView17
        val unsoldprodbuynow: TextView = itemview.itemsellbuynow

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnSoldAdapterViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.itemselling_cardview,parent,false)
        return UnSoldAdapterViewHolder(view)
    }

    override fun getItemCount() = unsoldposts.size

    override fun onBindViewHolder(holder: UnSoldAdapterViewHolder, position: Int) {

        unsoldposts[position].unsoldprodimg?.let { holder.unsoldimgg.setImageResource(it) }
        holder.unsoldprodnm.text  = unsoldposts[position].unsoldprodname
        holder.unsoldproddate.text = unsoldposts[position].unsoldproddate
        holder.unsoldprodprice.text  = unsoldposts[position].unsoldprice
        holder.unsoldprodbuynow.text = unsoldposts[position].unsoldbuynow


    }


}