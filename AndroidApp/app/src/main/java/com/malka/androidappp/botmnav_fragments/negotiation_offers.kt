package com.malka.androidappp.botmnav_fragments

import android.os.Bundle
import android.view.View
import android.widget.Filter
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.design.Models.negotiationmodel
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import kotlinx.android.synthetic.main.fragment_negotiation_offer.*
import kotlinx.android.synthetic.main.negotiation_offers_design.view.*
import kotlinx.android.synthetic.main.toolbar_main.*

class negotiation_offers : Fragment(R.layout.fragment_negotiation_offer) {
    val list: ArrayList<negotiationmodel> = ArrayList()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_title.text = getString(R.string.negotiation_offers)
        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        list.add(negotiationmodel("Electric - phone","RealMe 8 Pro Infinite","Riyadh", "1653",R.drawable.detailpic1,"Ahmed","Member since 5/23/2020",R.drawable.profiledp))
        list.add(negotiationmodel("Electric - bus","RealMe 8 ","Dubai", "1567",R.drawable.car1,"Ali", "Member since 2/21/2020",R.drawable.profile_pic))
        list.add(negotiationmodel("Electric - car","RealMe 8 Pro ","Dubai3", "1711",R.drawable.car5,"Ahmed2","Member since 12/11/2022",R.drawable.car2))
        list.add(negotiationmodel("Electric - car3","RealMe 8 Infinite","Uk", "9832",R.drawable.car4,"Ahmed3","Member since 5/22/2021",R.drawable.profiledp))
        list.add(negotiationmodel("Electric - jeep","Pro Infinite","Saudia", "6781",R.drawable.car2,"Ahmed5","Member since 1/28/2022",R.drawable.car4))

        NegotiationOffersAdaptor(list)
    }





    private fun NegotiationOffersAdaptor(list: List<negotiationmodel>) {
        negotiation_rcv.adapter = object : GenericListAdapter<negotiationmodel>(
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
                        person_dp.setImageResource(perdp)
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