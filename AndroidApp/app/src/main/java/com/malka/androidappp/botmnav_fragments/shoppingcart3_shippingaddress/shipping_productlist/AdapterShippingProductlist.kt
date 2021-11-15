package com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.shipping_productlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.shipping_productlist_cardview.view.*

class AdapterShippingProductlist (
    val shippingProductposts: ArrayList<ModelShippingProductlist>
    //,var context: Shoppingcart2
) : RecyclerView.Adapter<AdapterShippingProductlist.AdapterShippingProductlistViewHolder>()
{


    class AdapterShippingProductlistViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        val listingParty : TextView = itemview.listing_party
        val closeproductCard : ImageButton = itemview.close_card_product
        val imgShopcartproduct: ImageView = itemview.img_shopcart
        val cartproductDescrip : TextView = itemview.tx_productdescription
        val totalPrice: TextView = itemview.total_price
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterShippingProductlistViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.shipping_productlist_cardview,parent,false)
        return AdapterShippingProductlistViewHolder(view)

    }

    override fun getItemCount() = shippingProductposts.size

    override fun onBindViewHolder(holder: AdapterShippingProductlistViewHolder, position: Int) {

        holder.imgShopcartproduct.setImageResource(shippingProductposts[position].productImgCaart)
        holder.listingParty.text  = shippingProductposts[position].listingPartyy
        holder.cartproductDescrip.text = shippingProductposts[position].cartproductDescripp
        holder.totalPrice.text = shippingProductposts[position].totalPrice
    }




}