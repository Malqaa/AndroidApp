package com.malka.androidappp.botmnav_fragments.sold_business

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.network.constants.ApiConstants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.sold_business_card.view.*


class AdapterSoldBusiness(
    val soldBusinesspost: List<Solditem>
) : RecyclerView.Adapter<AdapterSoldBusiness.AdapterSoldBusinessViewHolder>() {


    class AdapterSoldBusinessViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val soldBicon: ImageView = itemview.sold_b_imgg
        val soldBtitle: TextView = itemview.sold_b_title
        val soldBdescrip: TextView = itemview.sold_b_desc
        val soldBreview: TextView = itemview.sold_b_review
        val soldBbuyprice: TextView = itemview.sold_b_buynowprice
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterSoldBusinessViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.sold_business_card, parent, false)
        return AdapterSoldBusinessViewHolder(view)

    }

    override fun getItemCount() = soldBusinesspost.size

    override fun onBindViewHolder(holder: AdapterSoldBusinessViewHolder, position: Int) {

        if (soldBusinesspost != null && soldBusinesspost.isNotEmpty() && soldBusinesspost[position] != null) {

            //holder.soldBicon.setImageResource(soldBusinesspost[position].prodimg!!)
            //holder.soldBreview.text = soldBusinesspost[position].review

            if (soldBusinesspost[position].image != null)
                Picasso.get()
                    .load(ApiConstants.IMAGE_URL + soldBusinesspost[position].image!!.get(0))
                    .into(holder.soldBicon) else holder.soldBicon.setImageResource(R.drawable.cam)


            holder.soldBtitle.text =
                if (soldBusinesspost[position].title != null)
                    soldBusinesspost[position].title
                else ""

            holder.soldBdescrip.text =
                if (soldBusinesspost[position].description != null)
                    soldBusinesspost[position].description
                else ""


            holder.soldBbuyprice.text =
                if (soldBusinesspost[position].price != null)
                    soldBusinesspost[position].price
                else ""
        }
    }


}