package com.malka.androidappp.botmnav_fragments.shoppingcart7_placeholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.placeholder_productlist_cardvieww.view.*


class AdapterPlaceholderProductlist(
    val placeholderProductposts: ArrayList<ModelPlaceholderProductlist>
    //,var context: Shoppingcart2
) : RecyclerView.Adapter<AdapterPlaceholderProductlist.AdapterPlaceholderProductlistViewHolder>()
{


    class AdapterPlaceholderProductlistViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        val listingParty : TextView = itemview.listing_party
        val closeproductCard : ImageButton = itemview.close_card_product
        val imgShopcartproduct: ImageView = itemview.img_shopcart
        val cartproductDescrip : TextView = itemview.tx_productdescription
        val pricebyQuantity : TextView = itemview.tx_after_descrip
        val shipToPerson:TextView = itemview.shippiedto_personname
        val shipAddress:TextView = itemview.shippiedto_address
        val shiptoPersonNum:TextView = itemview.shippiedto_person_num
        val totalPrice: TextView = itemview.total_price
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPlaceholderProductlistViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.placeholder_productlist_cardvieww,parent,false)
        return AdapterPlaceholderProductlistViewHolder(view)

    }

    override fun getItemCount() = placeholderProductposts.size

    override fun onBindViewHolder(holder: AdapterPlaceholderProductlistViewHolder, position: Int) {

        holder.imgShopcartproduct.setImageResource(placeholderProductposts[position].productImgCaart)
        holder.listingParty.text  = placeholderProductposts[position].listingPartyy
        holder.cartproductDescrip.text = placeholderProductposts[position].cartproductDescripp
        holder.pricebyQuantity.text = placeholderProductposts[position].pricebyQuantityy
        holder.totalPrice.text = placeholderProductposts[position].totalPrice
        holder.shipToPerson.text = placeholderProductposts[position].shipToperson
        holder.shipAddress.text = placeholderProductposts[position].shipAddresss
        holder.shiptoPersonNum.text = placeholderProductposts[position].shiptoPersonNumm


    }




}