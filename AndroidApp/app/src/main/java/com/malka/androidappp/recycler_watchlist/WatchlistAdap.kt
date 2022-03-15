package com.malka.androidappp.recycler_watchlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.watchlist_fragment.WatchlistFragment
import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.servicemodels.watchlist.watchlistproperties
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_item.view.*


class WatchlistAdap(
    val watchlistposts: List<watchlistproperties>,
    var context: WatchlistFragment
) : RecyclerView.Adapter<WatchlistAdap.WatchlistAdapViewHolder>() {
    var onItemClick: ((watchlistproperties) -> Unit)? = null

    inner class WatchlistAdapViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val watchlistimgg: ImageView = itemview.productimg
        val watchlistprodnm: TextView = itemview.titlenamee
        val watchlistprodprice: TextView = itemview.product_price
        val watchlistprodbuynow: TextView = itemview.LowestPrice
//        val imgbtn_watchlist_remove: ImageButton = itemview.imgbtn_watchlist_remove

        init {
            itemview.setOnClickListener {
                onItemClick?.invoke(watchlistposts[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistAdapViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return WatchlistAdapViewHolder(view)
    }

    override fun getItemCount() = watchlistposts.size

    override fun onBindViewHolder(holder: WatchlistAdapViewHolder, position: Int) {




    }
}