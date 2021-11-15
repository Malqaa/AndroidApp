package com.malka.androidappp.recycler_browsecat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.browse_market.BrowseMarketFragment
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.constants.ApiConstants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.browsemarket_carditems.view.*


class BrowseMarketAdapter(
    val marketposts: ArrayList<BrowseMarketModel>,
    var context: BrowseMarketFragment
) : RecyclerView.Adapter<BrowseMarketAdapter.BrowseMarketAdapterViewHolder>() {

    var onItemClick: ((BrowseMarketModel) -> Unit)? = null

    inner class BrowseMarketAdapterViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val marketimgg: ImageView = itemview.marketimgg
        val marketwatchimg: ImageView = itemview.marketwatchlist
        val marketprodnm: TextView = itemview.text1st
        val marketproddess: TextView = itemview.textv2nd
        val marketresrvepricetxt: TextView = itemview.text3rd
        val marketresrvepricee: TextView = itemview.amount2
        val marketbuynowtxt: TextView = itemview.buynowww
        val marketbuynowpricee: TextView = itemview.amount

        init {
            itemview.setOnClickListener {
                onItemClick?.invoke(marketposts[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BrowseMarketAdapterViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.browsemarket_carditems, parent, false)
        return BrowseMarketAdapterViewHolder(view)
    }

    override fun getItemCount() = marketposts.size

    override fun onBindViewHolder(holder: BrowseMarketAdapterViewHolder, position: Int) {

        /*if (marketposts[position].marketprodimg != null && marketposts[position].marketprodimg.size > 0)
            Picasso.get()
                .load(ApiConstants.IMAGE_URL + marketposts[position].marketprodimg[0])
                .into(holder.marketimgg) else holder.marketimgg.setImageResource(R.drawable.cam)*/

        if (marketposts[position].marketprodimg != null && marketposts[position].marketprodimg.trim().length > 0)
            Picasso.get()
                .load(ApiConstants.IMAGE_URL + marketposts[position].marketprodimg)
                .into(holder.marketimgg) else holder.marketimgg.setImageResource(R.drawable.cam)
        holder.marketwatchimg.setImageResource(R.drawable.watchlist)
        //If already added, then call delete API

        holder.marketwatchimg.setOnClickListener(View.OnClickListener {
            if (HelpFunctions.IsUserLoggedIn()) {
                marketposts[position].ItemInWatchlist =
                    HelpFunctions.AdAlreadyAddedToWatchList(marketposts[position].advid)
                if (marketposts[position].ItemInWatchlist) {
                    HelpFunctions.DeleteAdFromWatchlist(
                        marketposts[position].advid,
                        context = context
                    )
                } else {

                    watchListPopup(holder.marketwatchimg,marketposts[position].advid)

//                    HelpFunctions.InsertAdToWatchlist(
//                        marketposts[position].advid,
//                        0,
//                        context = context
//                    )
                }
            } else {
                HelpFunctions.ShowAlert(
                    context!!.requireContext(),
                    "Information",
                    "Please Log In"
                );
            }
        })
        holder.marketprodnm.text = marketposts[position].marketprodname
        holder.marketproddess.text = marketposts[position].marketproddes
        holder.marketresrvepricetxt.text = marketposts[position].marketresrvepricetext
        holder.marketresrvepricee.text = marketposts[position].marketresrveprice
        holder.marketbuynowtxt.text = marketposts[position].marketbuynowtext
        holder.marketbuynowpricee.text = marketposts[position].marketbuynowprice
    }

    private fun watchListPopup(view: View, AdvId: String) {
        val popupMenu = PopupMenu(context.requireContext(), view)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_dont_email -> {
                    HelpFunctions.InsertAdToWatchlist(
                        AdvId, 0,
                        context = context
                    )
                    true
                }
                R.id.menu_email_everyday -> {
                    HelpFunctions.InsertAdToWatchlist(
                        AdvId, 1,
                        context = context
                    )
                    true
                }
                R.id.menu_email_3day -> {
                    HelpFunctions.InsertAdToWatchlist(
                        AdvId, 3,
                        context = context
                    )
                    true
                }
                R.id.menu_email_once_a_week -> {
                    HelpFunctions.InsertAdToWatchlist(
                        AdvId, 7,
                        context = context
                    )
                    true
                }
                else -> false
            }
        }

        popupMenu.inflate(R.menu.menu_watchlist)

        try {
            val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldMPopup.isAccessible = true
            val mPopup = fieldMPopup.get(popupMenu)
            mPopup.javaClass
                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(mPopup, true)
        } catch (e: Exception) {
            Log.e("Main", "Error showing menu icons.", e)
        } finally {
            popupMenu.show()
        }
    }
}