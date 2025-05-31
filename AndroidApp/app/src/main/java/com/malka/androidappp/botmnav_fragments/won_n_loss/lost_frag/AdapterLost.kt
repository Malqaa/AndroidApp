package com.malka.androidappp.botmnav_fragments.won_n_loss.lost_frag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.won_n_loss.model_wonloss.LostAuction
import kotlinx.android.synthetic.main.won_card.view.*

class AdapterLost(
    val LostPost: List<LostAuction>
) : RecyclerView.Adapter<AdapterLost.AdapterLostViewHolder>()
{


    class AdapterLostViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {

        val losticon: ImageView = itemview.won_imgg
        val losttitle : TextView = itemview.won_title
        val lostdescrip: TextView = itemview.won_desc
        val lostreview: TextView = itemview.won_review
        val lostbuyprice: TextView = itemview.won_buynowprice
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterLostViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.won_card,parent,false)
        return AdapterLostViewHolder(view)

    }

    override fun getItemCount() = LostPost.size

    override fun onBindViewHolder(holder: AdapterLostViewHolder, position: Int) {

        if (LostPost != null && LostPost.size > 0 && LostPost[position] != null) {

            //holder.soldBicon.setImageResource(soldBusinesspost[position].prodimg!!)
            //holder.soldBreview.text = soldBusinesspost[position].review


            holder.losttitle.text =
                if (LostPost[position].description!=null)
                    LostPost[position].description
                else ""

            holder.lostdescrip.text =
                if (LostPost[position].description!=null)
                    LostPost[position].description
                else ""


            holder.lostbuyprice.text =
                if (LostPost[position].auctionPrice!=null)
                    LostPost[position].auctionPrice
                else ""
        }
    }



}