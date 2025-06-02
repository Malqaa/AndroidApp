package com.malka.androidappp.botmnav_fragments.won_n_loss.won_frag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.won_n_loss.model_wonloss.WonAuction
import kotlinx.android.synthetic.main.won_card.view.*

class AdapterWon (
    val wonPost: List<WonAuction>
) : RecyclerView.Adapter<AdapterWon.AdapterWonViewHolder>()
{


    class AdapterWonViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {

        val wonicon: ImageView = itemview.won_imgg
        val wontitle : TextView = itemview.won_title
        val wondescrip: TextView = itemview.won_desc
        val wonreview: TextView = itemview.won_review
        val wonbuyprice: TextView = itemview.won_buynowprice
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterWonViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.won_card,parent,false)
        return AdapterWonViewHolder(view)

    }

    override fun getItemCount() = wonPost.size

    override fun onBindViewHolder(holder: AdapterWonViewHolder, position: Int) {

        if (wonPost != null && wonPost.size > 0 && wonPost[position] != null) {

            //holder.soldBicon.setImageResource(soldBusinesspost[position].prodimg!!)
            //holder.soldBreview.text = soldBusinesspost[position].review


            holder.wontitle.text =
                if (wonPost[position].description!=null)
                    wonPost[position].description
                else ""

            holder.wondescrip.text =
                if (wonPost[position].description!=null)
                    wonPost[position].description
                else ""


            holder.wonbuyprice.text =
                if (wonPost[position].auctionPrice!=null)
                    wonPost[position].auctionPrice
                else ""
        }
    }



}