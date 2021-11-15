package com.malka.androidappp.botmnav_fragments.shoppingcart6_paymentmethod.payment_productlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.payment_productlist_cardview.view.*


class AdapterPaymentProductList(
    val placeholder1Productposts: ArrayList<ModelPaymentProductlist>
    //,var context: Shoppingcart2
) : RecyclerView.Adapter<AdapterPaymentProductList.AdapterPlaceholder1ProductListViewHolder>()
{


    class AdapterPlaceholder1ProductListViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        val listingParty : TextView = itemview.listing_party
        val closeproductCard : ImageButton = itemview.close_card_product
        val imgShopcartproduct: ImageView = itemview.img_shopcart
        val cartproductDescrip : TextView = itemview.tx_productdescription
        val pricebyQuantity : TextView = itemview.tx_after_descrip
        val totalPrice: TextView = itemview.total_price
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPlaceholder1ProductListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.payment_productlist_cardview,parent,false)
        return AdapterPlaceholder1ProductListViewHolder(view)

    }

    override fun getItemCount() = placeholder1Productposts.size

    override fun onBindViewHolder(holder: AdapterPlaceholder1ProductListViewHolder, position: Int) {

        holder.imgShopcartproduct.setImageResource(placeholder1Productposts[position].productImgCaart)
        holder.listingParty.text  = placeholder1Productposts[position].listingPartyy
        holder.cartproductDescrip.text = placeholder1Productposts[position].cartproductDescripp
        holder.pricebyQuantity.text = placeholder1Productposts[position].pricebyQuantityy
        holder.totalPrice.text = placeholder1Productposts[position].totalPrice
    }




}