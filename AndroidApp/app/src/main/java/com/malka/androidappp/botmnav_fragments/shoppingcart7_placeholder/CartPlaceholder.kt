package com.malka.androidappp.botmnav_fragments.shoppingcart7_placeholder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.shoppingcart6_paymentmethod.payment_productlist.AdapterPaymentProductList
import com.malka.androidappp.botmnav_fragments.shoppingcart6_paymentmethod.payment_productlist.ModelPaymentProductlist
import kotlinx.android.synthetic.main.fragment_cart_placeholder.*


class CartPlaceholder : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart_placeholder, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        toolbar_cart_placeholder.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_cart_placeholder.setNavigationOnClickListener(){requireActivity().onBackPressed()}
        ////////////////////////////Calling Again from previous screen ---- Cart item/product list///////////////////////////////////////////////

        val placeholderProductlist:ArrayList<ModelPlaceholderProductlist> = ArrayList()
        val placeholderProductListRecycler : RecyclerView = requireActivity().findViewById(R.id.placeholder_recycler)

        placeholderProductlist.add(
            ModelPlaceholderProductlist(R.drawable.mobile,"Samsung Galaxy S9 64GB Black (LocalWarranty) 100% NEW Unlocked","techXcape",
                "Shipping to","AED 18.95","Mohammed Saleh","+966-55-5335285","3367 an, nuzhah, Makkah 24225 7792, Saudi Arabia")
        )

        placeholderProductlist.add(
            ModelPlaceholderProductlist(R.drawable.mobile,"Samsung Galaxy S9 64GB Black (LocalWarranty) 100% NEW Unlocked","techXcape",
                "Shipping to","AED 18.95","Mohammed Saleh","+966-55-5335285","3367 an, nuzhah, Makkah 24225 7792, Saudi Arabia")
        )


        placeholderProductListRecycler.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,false)
        placeholderProductListRecycler.adapter = AdapterPlaceholderProductlist(placeholderProductlist)

        placeorder_btn2.setOnClickListener() {
            findNavController().navigate(R.id.checkout_to_home)
        }

    }
}