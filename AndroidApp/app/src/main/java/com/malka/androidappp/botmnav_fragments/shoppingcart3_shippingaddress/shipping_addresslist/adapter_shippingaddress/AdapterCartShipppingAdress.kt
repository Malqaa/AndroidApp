package com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.shipping_addresslist.adapter_shippingaddress

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.ShippingAddress
import com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ShippingAddressessData
import com.malka.androidappp.botmnav_fragments.shoppingcart6_paymentmethod.CartPaymentMethod
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.shopcart6_payment_cardview.view.*
import kotlinx.android.synthetic.main.shopcart_shipping_card.view.*

class AdapterCartShipppingAdress(
    val CartShipppingAdressPost: List<ShippingAddressessData>,
    var context: ShippingAddress
) : RecyclerView.Adapter<AdapterCartShipppingAdress.AdapterCartShipppingAdressViewHolder>() {


    class AdapterCartShipppingAdressViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val cartShipName: TextView = itemview.person_name_cartshipping
        val cartShipAddress: TextView = itemview.address_shipping_cart
        val cartShipPhone: TextView = itemview.phonenum_addressship
        val seladdrss: RadioButton = itemview.radio_btn_address
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterCartShipppingAdressViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopcart_shipping_card, parent, false)
        return AdapterCartShipppingAdressViewHolder(view)

    }

    override fun getItemCount() = CartShipppingAdressPost.size

    override fun onBindViewHolder(holder: AdapterCartShipppingAdressViewHolder, position: Int) {

        if (CartShipppingAdressPost != null && CartShipppingAdressPost.size > 0 && CartShipppingAdressPost[position] != null) {

            holder.cartShipName.text =
                if (CartShipppingAdressPost[position].firstName != null) CartShipppingAdressPost[position].firstName
                else ""
            holder.cartShipAddress.text =
                if (CartShipppingAdressPost[position].address != null) CartShipppingAdressPost[position].address
                else ""
            holder.cartShipPhone.text =
                if (CartShipppingAdressPost[position].mobileNo != null) CartShipppingAdressPost[position].mobileNo
                else ""

            holder.seladdrss.tag = position
            holder.seladdrss.isSelected = CartShipppingAdressPost[position].selected
            holder.seladdrss.isChecked = CartShipppingAdressPost[position].selected;
            holder.seladdrss.setOnClickListener {
                CartShipppingAdressPost[position].selected = true
                ConstantObjects.useraddresses!![position].selected = true;
                DeSelectAllOtherRadioButton(position);
                context.BindUserShippingAddresses()
                ConstantObjects.selected_address_index = position
            }
        }
    }

    fun DeSelectAllOtherRadioButton(position: Int) {
        if (CartShipppingAdressPost != null && CartShipppingAdressPost.size > 0) {
            for (i in 0..(CartShipppingAdressPost.size - 1)) {
                if (i == position) {
                    CartShipppingAdressPost[i].selected = true;
                    ConstantObjects.useraddresses!![i].selected = true;
                } else {
                    CartShipppingAdressPost[i].selected = false;
                    ConstantObjects.useraddresses!![i].selected = false;
                }
            }
        }
    }
}