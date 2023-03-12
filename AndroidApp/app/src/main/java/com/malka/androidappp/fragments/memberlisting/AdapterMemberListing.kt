package com.malka.androidappp.fragments.memberlisting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.fragments.sellerdetails.Advertisement
import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.seller_ads_adapter.view.*


class AdapterMemberListing(
    private val sellerAds: ArrayList<Advertisement>,
    var context: MemberListing
) : RecyclerView.Adapter<AdapterMemberListing.AdapterMemberListingViewHolder>() {


    inner class AdapterMemberListingViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview),
        View.OnClickListener {

        val mlimgg: ImageView = itemview.adImageView
//        val mlwatchimg: ImageView = itemview.marketwatchlist
        val mlprodnm: TextView = itemview.title
        val mlproddess: TextView = itemview.description
        val mlresrvepricetxt: TextView = itemview.reserveText
        val mlresrvepricee: TextView = itemview.reserveAmount
        val mlbuynowtxt: TextView = itemview.buyNowText
        val mlbuynowpricee: TextView = itemview.amount

//        init {
//            itemview.setOnClickListener(this)
//            myProductedit.setOnClickListener(this)
//        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
//                listener.OnItemClick(position)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterMemberListingViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.seller_ads_adapter, parent, false)
        return AdapterMemberListingViewHolder(view)

    }

    override fun getItemCount() = sellerAds.size

    override fun onBindViewHolder(holder: AdapterMemberListingViewHolder, position: Int) {

        if (sellerAds[position].homepageImage != null)
            Picasso.get()
                .load(Constants.IMAGE_URL + sellerAds[position].homepageImage)
                .into(holder.mlimgg) else holder.mlimgg.setImageResource(R.drawable.cam)

//        holder.mlwatchimg.setImageResource(R.drawable.watchlist)
        holder.mlprodnm.text = sellerAds[position].title
        holder.mlproddess.text = sellerAds[position].description
        holder.mlresrvepricetxt.text = "Reserve not met"
        holder.mlbuynowtxt.text = "Buy Now"
        holder.mlresrvepricee.text = sellerAds[position].reservePrice
        holder.mlbuynowpricee.text = sellerAds[position].price



//        holder.myProductedit.setOnClickListener(View.OnClickListener {
//            listener.onEdit(position)
//        })

    }

    interface OnItemClickListener {
        fun OnItemClick(position: Int) {}
        fun onEdit(position: Int) {}
    }







//    val memberslistposts: ArrayList<MemberListingModel>,
//    var context: MemberListing
//) : RecyclerView.Adapter<AdapterMemberListing.AdapterMemberListingViewHolder>() {
//
//
//    class AdapterMemberListingViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
//        val mlimgg: ImageView = itemview.marketimgg
//        val mlwatchimg: ImageView = itemview.marketwatchlist
//        val mlprodnm: TextView = itemview.text1st
//        val mlproddess: TextView = itemview.textv2nd
//        val mlresrvepricetxt: TextView = itemview.text3rd
//        val mlresrvepricee: TextView = itemview.amount2
//        val mlbuynowtxt: TextView = itemview.buynowww
//        val mlbuynowpricee: TextView = itemview.amount
//
//    }
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): AdapterMemberListingViewHolder {
//        val view: View = LayoutInflater.from(parent.context)
//            .inflate(R.layout.browsemarket_carditems, parent, false)
//        return AdapterMemberListingViewHolder(view)
//    }
//
//    override fun getItemCount() = memberslistposts.size
//
//    override fun onBindViewHolder(holder: AdapterMemberListingViewHolder, position: Int) {
//
//        memberslistposts[position].mlprodimg?.let { holder.mlimgg.setImageResource(it) }
//        memberslistposts[position].mlwatchlist?.let { holder.mlwatchimg.setImageResource(it) }
//        holder.mlprodnm.text = memberslistposts[position].mlprodname
//        holder.mlproddess.text = memberslistposts[position].mlproddes
//        holder.mlresrvepricetxt.text = memberslistposts[position].mlresrvepricetext
//        holder.mlresrvepricee.text = memberslistposts[position].mlresrveprice
//        holder.mlbuynowtxt.text = memberslistposts[position].mlbuynowtext
//        holder.mlbuynowpricee.text = memberslistposts[position].mlbuynowprice
//    }


}