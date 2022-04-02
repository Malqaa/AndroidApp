package com.malka.androidappp.activities_main.order

import android.os.Bundle
import android.widget.Filter
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.design.Models.cartmdel
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.addtocart.CartItemModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.cart_design.view.*

class CartActivity : BaseActivity() {
    val list: ArrayList<cartmdel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        HelpFunctions.GetUsersCartList {
            setCategoryAdaptor()
        }

        the_next.setOnClickListener {
            if (ConstantObjects.usercart.size > 0) {
                //findNavController().navigate(R.id.checkout_to_shipping_address)
            } else {
                showError(getString(R.string.empty_cart))
            }
        }
    }


    private fun setCategoryAdaptor() {
        var price=0.0
        ConstantObjects.usercart.forEach {
            price+= it.advertisements.price.toDouble()
        }
        price_total.text = price.toString()
        category_rcv.adapter = object : GenericListAdapter<CartItemModel>(
            R.layout.cart_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.advertisements.run {
                        //  prod_type.text=protype
                        //    prod_name.text=proname
                        //   prod_city.text=procity
                        prod_price.text = price
                        Picasso.get()
                            .load(ApiConstants.IMAGE_URL + image)
                            .into(prod_image)
                    }
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(
                ConstantObjects.usercart
            )
        }
    }
}