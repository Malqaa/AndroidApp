package com.malka.androidappp.fragments.shoppingcart3_shippingaddress.shipping_addresslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.fragments.shoppingcart3_shippingaddress.ShippingAddress
import kotlinx.android.synthetic.main.shopcart_shipping_card.view.*

class AdapterCartShipppingAdress (
    val CartShipppingAdressPost: ArrayList<ModelCartShippingAddress>,
    var context: ShippingAddress
) : RecyclerView.Adapter<AdapterCartShipppingAdress.AdapterCartShipppingAdressViewHolder>()
{


    class AdapterCartShipppingAdressViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        val cartShipName : TextView = itemview.person_name_cartshipping
        val cartShipAddress: TextView = itemview.address_shipping_cart
        val cartShipPhone: TextView = itemview.phonenum_addressship
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterCartShipppingAdressViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.shopcart_shipping_card,parent,false)
        return AdapterCartShipppingAdressViewHolder(view)

    }

    override fun getItemCount() = CartShipppingAdressPost.size

    override fun onBindViewHolder(holder: AdapterCartShipppingAdressViewHolder, position: Int) {
        holder.cartShipName.text  = CartShipppingAdressPost[position].nameshipAdress
        holder.cartShipAddress.text = CartShipppingAdressPost[position].addressCartShipping
        holder.cartShipPhone.text = CartShipppingAdressPost[position].phonecartShipping
    }




}