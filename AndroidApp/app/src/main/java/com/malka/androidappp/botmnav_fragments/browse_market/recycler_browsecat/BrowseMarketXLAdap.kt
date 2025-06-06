package com.malka.androidappp.recycler_browsecat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.browse_market.BrowseMarketFragment
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.recycler_browsecat.ImageAdapter
import kotlinx.android.synthetic.main.browsecat_gridview.view.*


class BrowseMarketXLAdap(
    val marketposts: ArrayList<BrowseMarketModel>,
    var context: BrowseMarketFragment
) : RecyclerView.Adapter<BrowseMarketXLAdap.BrowseMarketXLAdapViewHolder>() {

    var onItemClick: ((BrowseMarketModel) -> Unit)? = null

    inner class BrowseMarketXLAdapViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val mViewPager: ViewPager = itemview.myViewPager
        val marketwatchimg: ImageView = itemview.marketwatchimg
        val marketprodnm: TextView = itemview.gridtext
        val marketproddess: TextView = itemview.gridtext1
        val marketresrvepricetxt: TextView = itemview.gridtext2
        val marketresrvepricee: TextView = itemview.gridtext3
        val marketbuynowtxt: TextView = itemview.gridtext4
        val marketbuynowpricee: TextView = itemview.gridtext5
        val mTabLayout: TabLayout? = itemview.mytabLayout

        init {
            itemview.setOnClickListener {
                onItemClick?.invoke(marketposts[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BrowseMarketXLAdapViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.browsecat_gridview, parent, false)
        return BrowseMarketXLAdapViewHolder(view)
    }

    override fun getItemCount() = marketposts.size

    override fun onBindViewHolder(holder: BrowseMarketXLAdapViewHolder, position: Int) {
        holder.marketwatchimg.setImageResource(R.drawable.watchlist)
        holder.marketprodnm.text = marketposts[position].marketprodname
        holder.marketproddess.text = marketposts[position].marketproddes
        holder.marketresrvepricetxt.text = marketposts[position].marketresrvepricetext
        holder.marketresrvepricee.text = marketposts[position].marketresrveprice
        holder.marketbuynowtxt.text = marketposts[position].marketbuynowtext
        holder.marketbuynowpricee.text = marketposts[position].marketbuynowprice

        /*val imageAdapter = ImageAdapterImageArray(context.requireContext(), marketposts[position].marketprodimg)
        holder.mViewPager.adapter = imageAdapter
        holder.mTabLayout!!.setupWithViewPager(holder.mViewPager, true)*/

        holder.marketwatchimg.setOnClickListener(View.OnClickListener {
            if(HelpFunctions.IsUserLoggedIn()) {
                marketposts[position].ItemInWatchlist =
                    HelpFunctions.AdAlreadyAddedToWatchList(marketposts[position].advid)
                if (marketposts[position].ItemInWatchlist) {
                    HelpFunctions.DeleteAdFromWatchlist(
                        marketposts[position].advid,
                        context = context
                    )
                } else {
                    watchListPopup(holder.marketwatchimg, marketposts[position].advid)
//                    HelpFunctions.InsertAdToWatchlist(
//                        marketposts[position].advid,0,
//                        context = context
//                    )
                }
            }
            else {
                HelpFunctions.ShowAlert(
                    context.requireContext(),
                    "Information",
                    "Please Log In"
                );
            }
        })
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