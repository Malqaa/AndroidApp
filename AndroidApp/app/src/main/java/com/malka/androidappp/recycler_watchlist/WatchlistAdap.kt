package com.malka.androidappp.recycler_watchlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.watchlist_fragment.WatchlistFragment
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.recycler_browsecat.BrowseMarketModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.itemselling_cardview.view.*
import kotlinx.android.synthetic.main.itemselling_cardview.view.itemsellbuynow
import kotlinx.android.synthetic.main.itemselling_cardview.view.productimg
import kotlinx.android.synthetic.main.itemselling_cardview.view.textView17
import kotlinx.android.synthetic.main.itemselling_cardview.view.textView18
import kotlinx.android.synthetic.main.itemselling_watchlist.view.*


class WatchlistAdap(
    val watchlistposts: ArrayList<WatchlistModel>,
    var context: WatchlistFragment
) : RecyclerView.Adapter<WatchlistAdap.WatchlistAdapViewHolder>() {
    var onItemClick: ((WatchlistModel) -> Unit)? = null

    inner class WatchlistAdapViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val watchlistimgg: ImageView = itemview.productimg
        val watchlistprodnm: TextView = itemview.textView18
        val watchlistprodprice: TextView = itemview.textView17
        val watchlistprodbuynow: TextView = itemview.itemsellbuynow
        val imgbtn_watchlist_remove: ImageButton = itemview.imgbtn_watchlist_remove

        init {
            itemview.setOnClickListener {
                onItemClick?.invoke(watchlistposts[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistAdapViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemselling_watchlist, parent, false)
        return WatchlistAdapViewHolder(view)
    }

    override fun getItemCount() = watchlistposts.size

    override fun onBindViewHolder(holder: WatchlistAdapViewHolder, position: Int) {

        if (watchlistposts[position].watchlistprodimg != null && watchlistposts[position].watchlistprodimg!!.trim().length > 0)
            Picasso.get()
                .load(ApiConstants.IMAGE_URL + watchlistposts[position].watchlistprodimg)
                .into(holder.watchlistimgg) else holder.watchlistimgg.setImageResource(R.drawable.cam)
        if (watchlistposts[position].watchlistprodimg != null && watchlistposts[position].watchlistprodimg!!.trim().length > 0)
            Picasso.get()
                .load(ApiConstants.IMAGE_URL + watchlistposts[position].watchlistprodimg)
                .into(holder.watchlistimgg) else holder.watchlistimgg.setImageResource(R.drawable.watchlist)
        holder.watchlistprodnm.text = watchlistposts[position].watchlistprodname
        holder.watchlistprodprice.text = watchlistposts[position].watchlistprice
        holder.watchlistprodbuynow.text = watchlistposts[position].watchlistbuynow
        holder.imgbtn_watchlist_remove.setOnClickListener() {
            HelpFunctions.DeleteAdFromWatchlist(
                watchlistposts[position].watchlistadvid!!,
                context
            );
        }
    }
}