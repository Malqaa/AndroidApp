package com.malka.androidappp.fragments.shoppingcart6_paymentmethod.paymentmethod_bankscards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.fragments.shoppingcart6_paymentmethod.CartPaymentMethod
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.shopcart6_payment_cardview.view.*

class AdapterCartPaymentCard(
    val paymentCardposts: ArrayList<ModelCartPaymentCard>,
    var context: CartPaymentMethod
) : RecyclerView.Adapter<AdapterCartPaymentCard.AdapterCartPaymentCardViewHolder>() {


    class AdapterCartPaymentCardViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val icon: ImageView = itemview.img_paymentcard
        val textt: TextView = itemview.card_text
        val selcard: RadioButton = itemview.radio_btn_card
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterCartPaymentCardViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopcart6_payment_cardview, parent, false)
        return AdapterCartPaymentCardViewHolder(view)

    }

    override fun getItemCount() = paymentCardposts.size

    override fun onBindViewHolder(holder: AdapterCartPaymentCardViewHolder, position: Int) {
        holder.icon.setImageResource(paymentCardposts[position].cardImage!!)
        holder.textt.text = paymentCardposts[position].cardText
        holder.selcard.tag = position
        holder.selcard.isSelected = paymentCardposts[position].selected
        holder.selcard.isChecked = paymentCardposts[position].selected;
        holder.selcard.setOnClickListener {
            paymentCardposts[position].selected = true
            DeSelectAllOtherRadioButton(position);
            context.BindUserCreditCards()
            ConstantObjects.selected_credit_card_index = position
        }
    }

    fun DeSelectAllOtherRadioButton(position: Int) {
        if (paymentCardposts != null && paymentCardposts.size > 0) {
            for (i in 0..(paymentCardposts.size - 1)) {
                if (i == position) {
                    paymentCardposts[i].selected = true;
                    ConstantObjects.usercreditcard!![i].isSelected = true;
                } else {
                    paymentCardposts[i].selected = false;
                    ConstantObjects.usercreditcard!![i].isSelected = false;
                }
            }
        }
    }

}