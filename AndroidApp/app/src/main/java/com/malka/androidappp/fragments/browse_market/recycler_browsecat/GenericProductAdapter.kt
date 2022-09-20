package com.malka.androidappp.recycler_browsecat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.helper.BaseViewHolder
import com.malka.androidappp.helper.GenericAdaptor
import com.malka.androidappp.servicemodels.AdDetailModel
import com.malka.androidappp.servicemodels.model.Products


class GenericProductAdapter(
    var marketposts: List<Any>,
    var context: Context
) : RecyclerView.Adapter<BaseViewHolder>() {
    var isGrid:Boolean=true




    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return BaseViewHolder(view)
    }

    override fun getItemCount() = marketposts.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {


          GenericAdaptor().productAdaptor(marketposts.get(position) as Products, context, holder,isGrid)


//
//        holder.marketwatchimg.setImageResource(R.drawable.watchlist)
//        holder.marketprodnm.text = marketposts[position].marketprodname
//        holder.marketproddess.text = marketposts[position].marketproddes
//        holder.marketresrvepricetxt.text = marketposts[position].marketresrvepricetext
//        holder.marketresrvepricee.text = marketposts[position].marketresrveprice
//        holder.marketbuynowtxt.text = marketposts[position].marketbuynowtext
//        holder.marketbuynowpricee.text = marketposts[position].marketbuynowprice

  //   val imageAdapter = ImageAdapterImageArray(context.requireContext(), marketposts[position].marketprodimg)
//        holder.mViewPager.adapter = imageAdapter
//        holder.mTabLayout!!.setupWithViewPager(holder.mViewPager, true)*/
//
//        holder.marketwatchimg.setOnClickListener(View.OnClickListener {
//            if(HelpFunctions.IsUserLoggedIn()) {
//                marketposts[position].ItemInWatchlist =
//                    HelpFunctions.AdAlreadyAddedToWatchList(marketposts[position].advid)
//                if (marketposts[position].ItemInWatchlist) {
//                    HelpFunctions.DeleteAdFromWatchlist(
//                        marketposts[position].advid,
//                        context = context
//                    )
//                } else {
//                    watchListPopup(holder.marketwatchimg, marketposts[position].advid)
////                    HelpFunctions.InsertAdToWatchlist(
////                        marketposts[position].advid,0,
////                        context = context
////                    )
//                }
//            }
//            else {
//                HelpFunctions.ShowAlert(
//                    context.requireContext(),
//                    "Information",
//                    "Please Log In"
//                );
//            }
//        })
    }

//    private fun watchListPopup(view: View, AdvId: String) {
//        val popupMenu = PopupMenu(context.requireContext(), view)
//        popupMenu.setOnMenuItemClickListener { item ->
//            when (item.itemId) {
//                R.id.menu_dont_email -> {
//                    HelpFunctions.InsertAdToWatchlist(
//                        AdvId, 0,
//                        context = context
//                    )
//                    true
//                }
//                R.id.menu_email_everyday -> {
//                    HelpFunctions.InsertAdToWatchlist(
//                        AdvId, 1,
//                        context = context
//                    )
//                    true
//                }
//                R.id.menu_email_3day -> {
//                    HelpFunctions.InsertAdToWatchlist(
//                        AdvId, 3,
//                        context = context
//                    )
//                    true
//                }
//                R.id.menu_email_once_a_week -> {
//                    HelpFunctions.InsertAdToWatchlist(
//                        AdvId, 7,
//                        context = context
//                    )
//                    true
//                }
//                else -> false
//            }
//        }
//
//        popupMenu.inflate(R.menu.menu_watchlist)
//
//        try {
//            val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
//            fieldMPopup.isAccessible = true
//            val mPopup = fieldMPopup.get(popupMenu)
//            mPopup.javaClass
//                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
//                .invoke(mPopup, true)
//        } catch (e: Exception) {
//            Log.e("Main", "Error showing menu icons.", e)
//        } finally {
//            popupMenu.show()
//        }
//    }


    fun updateLayout(isGrid:Boolean){
        this.isGrid=isGrid
        notifyDataSetChanged()
    }

    fun updateData(marketposts:List<AdDetailModel>){
        this.marketposts=marketposts
        notifyDataSetChanged()
    }
}