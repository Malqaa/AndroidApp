package com.malka.androidappp.botmnav_fragments.unsold_business

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.sold_business.Unsolditem
import com.malka.androidappp.network.constants.ApiConstants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.unsold_business_card.view.*

class AdapterUnsoldBusiness(
    val unsoldBusinesspost: List<Unsolditem>
) : RecyclerView.Adapter<AdapterUnsoldBusiness.AdapterUnsoldBusinessViewHolder>() {


    class AdapterUnsoldBusinessViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val unsoldBicon: ImageView = itemview.unsold_b_imgg
        val unsoldBtitle: TextView = itemview.unsold_b_title
        val unsoldBdescrip: TextView = itemview.unsold_b_desc
        val unsoldBreview: TextView = itemview.unsold_b_review
        val unsoldBbuyprice: TextView = itemview.unsold_b_buynowprice
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterUnsoldBusinessViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.unsold_business_card, parent, false)
        return AdapterUnsoldBusinessViewHolder(view)

    }

    override fun getItemCount() = unsoldBusinesspost.size

    override fun onBindViewHolder(holder: AdapterUnsoldBusinessViewHolder, position: Int) {

        if (unsoldBusinesspost != null && unsoldBusinesspost.isNotEmpty() && unsoldBusinesspost[position] != null) {

            //holder.unsoldBicon.setImageResource(unsoldBusinesspost[position].prodimg!!)
            //holder.unsoldBreview.text = unsoldBusinesspost[position].review

            if (unsoldBusinesspost[position].image != null)
                Picasso.get()
                    .load(ApiConstants.IMAGE_URL + unsoldBusinesspost[position].image!!.get(0))
                    .into(holder.unsoldBicon) else holder.unsoldBicon.setImageResource(R.drawable.cam)

            holder.unsoldBtitle.text =
                if (unsoldBusinesspost[position].title != null)
                    unsoldBusinesspost[position].title
                else ""

            holder.unsoldBdescrip.text =
                if (unsoldBusinesspost[position].description != null)
                    unsoldBusinesspost[position].description
                else ""

            holder.unsoldBbuyprice.text =
                if (unsoldBusinesspost[position].price != null)
                    unsoldBusinesspost[position].price
                else ""
        }

    }


}