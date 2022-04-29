package com.malka.androidappp.botmnav_fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.servicemodels.Negotiationmodel
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import kotlinx.android.synthetic.main.fragment_negotiation_offer.*
import kotlinx.android.synthetic.main.negotiation_offers_design.view.*
import kotlinx.android.synthetic.main.toolbar_main.*

class negotiation_offers : Fragment(R.layout.fragment_negotiation_offer) {
    val list: ArrayList<Negotiationmodel> = ArrayList()

    var isSelect: Boolean =false



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_title.text = getString(R.string.negotiation_offers)
        back_btn.setOnClickListener {
            requireActivity().onBackPressed()

        }


        list.add(Negotiationmodel("Electric - phone","RealMe 8 Pro Infinite","Riyadh", "1653",R.drawable.detailpic1,"Ahmed","Member since 5/23/2020",R.drawable.profiledp))
        list.add(Negotiationmodel("Electric - bus","RealMe 8 ","Dubai", "1567",R.drawable.car1,"Ali", "Member since 2/21/2020",R.drawable.profile_pic))
        list.add(Negotiationmodel("Electric - car","RealMe 8 Pro ","Dubai3", "1711",R.drawable.car5,"Ahmed2","Member since 12/11/2022",R.drawable.car2))
        list.add(Negotiationmodel("Electric - car3","RealMe 8 Infinite","Uk", "9832",R.drawable.car4,"Ahmed3","Member since 5/22/2021",R.drawable.profiledp))
        list.add(Negotiationmodel("Electric - jeep","Pro Infinite","Saudia", "6781",R.drawable.car2,"Ahmed5","Member since 1/28/2022",R.drawable.car4))

        NegotiationOffersAdaptor(list)




        sent.setOnClickListener {
            sent.background =  ContextCompat.getDrawable( requireContext(),
                R.drawable.round_btn
            )
            sent.setTextColor(Color.parseColor("#FFFFFF"))
            received.setTextColor(Color.parseColor("#45495E"))
//            btn.setText("Accept")
            from_buyer.setText("Sent to buyers")
            from_seller.setText("Sent to Seller")

            received.background = null
        }



        received.setOnClickListener {
            received.background =  ContextCompat.getDrawable( requireContext(),
                R.drawable.round_btn
            )
            received.setTextColor(Color.parseColor("#FFFFFF"))
            sent.setTextColor(Color.parseColor("#45495E"))

            from_buyer.setText("Received from buyers")
            from_seller.setText("Received from Sellers")

            sent.background = null
        }




        from_buyer.setOnClickListener {
            from_buyer.background =  ContextCompat.getDrawable( requireContext(),
                R.drawable.product_attribute_bg_linebg
            )
            from_seller.background =  ContextCompat.getDrawable( requireContext(),
                R.drawable.edittext_bg
            )

            from_seller.setTextColor(Color.parseColor("#45495E"))
            from_buyer.setTextColor(Color.parseColor("#EE6C4D"))
        }


        from_seller.setOnClickListener {
            from_seller.background =  ContextCompat.getDrawable( requireContext(),
                R.drawable.product_attribute_bg_linebg
            )
            from_buyer.background =  ContextCompat.getDrawable( requireContext(),
                R.drawable.edittext_bg
            )
            from_buyer.setTextColor(Color.parseColor("#45495E"))
            from_seller.setTextColor(Color.parseColor("#EE6C4D"))
        }

    }


    private fun NegotiationOffersAdaptor(list: List<Negotiationmodel>) {
        negotiation_rcv.adapter = object : GenericListAdapter<Negotiationmodel>(
            R.layout.negotiation_offers_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        product_type.text=protype
                        product_name.text=proname
                        product_city.text=procity
                        product_image.setImageResource(proimage)
                        person_name.text=pername
                        person_since.text=persince
                    }
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(
                list
            )
        }
    }


}